package com.github.springwind.core.canal.service.abs;

import com.alibaba.fastjson.JSON;
import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import com.github.springwind.common.exception.CommonException;
import com.github.springwind.core.canal.entity.CanalChangeInfo;
import com.github.springwind.core.canal.entity.CanalMsg;
import com.github.springwind.core.mq.constants.AmqpContants;
import com.github.springwind.core.mq.producer.MessageSender;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.SystemUtils;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author pengnian
 * @version V1.0
 * @date 2019/9/6 15:02
 * @Desc
 */
@Slf4j
public abstract class AbstractCanalService {

    private static final int BATCH_SIZE = 5 * 1024;

    private volatile boolean running = false;

    private Thread worker;

    private String destination;

    private CanalConnector canalConnector;

    private Thread.UncaughtExceptionHandler handler = (t, e) -> log.error("Canal parse event has an error", e);

    private static final String SEP = SystemUtils.LINE_SEPARATOR;

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private static String contextFormat;

    private static String rowFormat;

    private static String transactionFormat;

    @Resource
    private MessageSender messageSender;

    static {
        contextFormat = SEP + "****************************************************" + SEP;
        contextFormat += "* Batch Id: [{}] ,Count : [{}] , Mem Size : [{}] , Time : {}" + SEP;
        contextFormat += "* Start : [{}] " + SEP;
        contextFormat += "* End : [{}] " + SEP;
        contextFormat += "****************************************************" + SEP;
        rowFormat = SEP + "----------------> binlog[{}:{}] , name[{},{}] , eventType : {} , executeTime : {} , delay : {}ms" + SEP;
        transactionFormat = SEP + "================> binlog[{}:{}] , executeTime : {} , delay : {}ms" + SEP;
    }

    protected void start() {
        Assert.notNull(canalConnector, "CanalConnector must not be null");
        worker = new Thread(() -> process());
        worker.setUncaughtExceptionHandler(handler);
        worker.start();
        running = true;
        log.info("Canal started successfully!");
    }

    private void process() {
        while (running) {
            try {
                canalConnector.connect();
                canalConnector.subscribe();
                canalConnector.rollback();
                while (running) {
                    //双重判断
                    Message message = canalConnector.getWithoutAck(BATCH_SIZE);
                    long batchId = message.getId();
                    int size = message.getEntries().size();
                    if (batchId == 0 || size <= 0) {
                        log.debug("Empty canal message");
                    } else {
                        processEntry(message.getEntries());
                    }
                    canalConnector.ack(batchId);
                }
            } catch (Exception e) {
                log.error("Canal Client Exception", e);
            } finally {
                canalConnector.disconnect();
            }
        }
    }

    private void processEntry(List<CanalEntry.Entry> entries) {
        List<CanalMsg> canalMsgList = convert(entries);
        //发往mq
        canalMsgList.forEach(canalMsg -> messageSender.sendFanoutMsg(AmqpContants.CANAL_FANOUT_EXCHANGE, JSON.toJSONString(canalMsg)));
    }

    private List<CanalMsg> convert(List<CanalEntry.Entry> entries) {
        List<CanalMsg> canalMsgList = new ArrayList<>();
        entries.forEach(entry -> {
            //执行时间
            long executeTime = entry.getHeader().getExecuteTime();
            long delayTime = System.currentTimeMillis() - executeTime;
            if (CanalEntry.EntryType.TRANSACTIONBEGIN == entry.getEntryType() || CanalEntry.EntryType.TRANSACTIONEND == entry.getEntryType()) {
                return;
            }

            if (CanalEntry.EntryType.ROWDATA == entry.getEntryType()) {
                CanalEntry.RowChange rowChange;
                try {
                    rowChange = CanalEntry.RowChange.parseFrom(entry.getStoreValue());
                } catch (Exception e) {
                    log.error("Error parse {}", entry.toString(), e);
                    throw new CommonException("Error parse entry!");
                }

                CanalEntry.EventType eventType = rowChange.getEventType();

                //打印查询sql和ddl语句
                if (CanalEntry.EventType.QUERY == eventType || rowChange.getIsDdl()) {
                    log.info(" sql ----> " + rowChange.getSql() + SEP);
                    return;
                }

                //bin-log event log
                log.info(rowFormat, entry.getHeader().getLogfileName(),
                        entry.getHeader().getLogfileOffset(),
                        entry.getHeader().getSchemaName(),
                        entry.getHeader().getTableName(),
                        eventType,
                        entry.getHeader().getExecuteTime(),
                        delayTime);

                rowChange.getRowDatasList().forEach(rowData -> {
                    CanalMsg canalMsg = new CanalMsg();
                    canalMsg.setDbName(entry.getHeader().getSchemaName());
                    canalMsg.setTableName(entry.getHeader().getTableName());
                    canalMsg.setEventType(eventType.toString());
                    canalMsg.setBeforeData(convertCanalChangeInfo(rowData.getBeforeColumnsList()));
                    canalMsg.setAfterData(convertCanalChangeInfo(rowData.getAfterColumnsList()));
                    canalMsgList.add(canalMsg);
                });

            }
        });

        return canalMsgList;
    }

    private List<CanalChangeInfo> convertCanalChangeInfo(List<CanalEntry.Column> columnsList) {
        List<CanalChangeInfo> canalChangeInfoList = new ArrayList<>();
        columnsList.forEach(column -> {
            CanalChangeInfo canalChangeInfo = new CanalChangeInfo();
            canalChangeInfo.setUpdate(column.getUpdated());
            canalChangeInfo.setColumnName(column.getName());
            canalChangeInfo.setColumnValue(column.getValue());
            canalChangeInfoList.add(canalChangeInfo);
        });
        return canalChangeInfoList;
    }

    protected void stop() {
        if (!running) {
            return;
        }
        running = false;
        if (worker != null) {
            try {
                worker.join();
            } catch (Exception e) {
                log.error("Worker Stop InterruptedException", e);
            }
        }
    }

    protected void setDestination(String instance) {
        this.destination = instance;
    }

    protected void setCanalConnector(CanalConnector canalConnector) {
        this.canalConnector = canalConnector;
    }
}
