package com.github.springwind.common.aspect;

import com.github.springwind.common.annotation.DistributedLock;
import com.github.springwind.common.exception.CommonException;
import com.github.springwind.common.exception.DistributedLockException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author pengnian
 * @version V1.0
 * @date 2019/9/16 15:28
 * @Desc
 */
@Component
@Aspect
@Order(0)
@Slf4j
public class DistributedLockAspect {

    @Resource
    private StringRedisTemplate redisTemplate;

    private static final String GET = "get";

    private static final String LOCK_VALUE = "lock";

    private static final Random RANDOM = new Random();

    /**
     * 纳秒和毫秒之间的转换率
     */
    private static final long MILLI_NANO_TIME = 1000 * 1000L;

    @Before(value = "execution(* com.github.springwind..*.*(..)) && @annotation(distributedLock)")
    public void lock(JoinPoint joinPoint, DistributedLock distributedLock) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        GetLockKey getLockKey = new GetLockKey(joinPoint, distributedLock).invoke();
        int expiredTime = getLockKey.getExpiredTime();
        String lockTrueKey = getLockKey.getLockTrueKey();
        long timeout = getLockKey.getTimeout();
        long nanoTimeOut = timeout * MILLI_NANO_TIME;
        long nanoTime = System.nanoTime();
        try {
            while (System.nanoTime() - nanoTime < nanoTimeOut) {
                Boolean lock = redisTemplate.opsForValue().setIfAbsent(lockTrueKey, LOCK_VALUE, expiredTime, TimeUnit.SECONDS);
                if (lock != null && lock) {
                    log.info("redis lock success, lockKey: {}", lockTrueKey);
                    return;
                }
                log.info("分布式锁获取等待中······");
                Thread.sleep(10, RANDOM.nextInt(100));
            }
            log.warn("获取分布式锁超时！lockKey: {}", lockTrueKey);
            throw new DistributedLockException("获取分布式锁超时!");
        } catch (DistributedLockException e) {
            log.error("获取分布式锁超时！", e);
            throw new DistributedLockException("获取分布式锁超时！");
        } catch (Exception e) {
            log.error("设置分布式锁出错！", e);
            throw new CommonException("设置分布式锁出错！");
        } finally {
            redisTemplate.delete(lockTrueKey);
        }
    }

    @After(value = "execution(* com.github.springwind..*.*(..)) && @annotation(distributedLock)")
    public void unlock(JoinPoint joinPoint, DistributedLock distributedLock) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        GetLockKey getLockKey = new GetLockKey(joinPoint, distributedLock).invoke();
        try {
            redisTemplate.delete(getLockKey.getLockTrueKey());
            log.info("释放分布式锁成功！lockKey: {}", getLockKey.getLockTrueKey());
        } catch (Exception e) {
            log.error("释放分布式锁失败！", e);
            throw new CommonException("释放分布式锁失败！");
        }
    }

    @AfterThrowing(value = "execution(* com.github.springwind..*.*(..)) && @annotation(distributedLock)")
    public void throwUnlock(JoinPoint joinPoint, DistributedLock distributedLock) {
        log.info("66666666666666666666666666666666666666666");
    }

    private static class GetLockKey {
        private JoinPoint joinPoint;
        private DistributedLock distributedLock;
        private int expiredTime;
        private long timeout;
        private String lockTrueKey;

        private GetLockKey(JoinPoint joinPoint, DistributedLock distributedLock) {
            this.joinPoint = joinPoint;
            this.distributedLock = distributedLock;
        }

        private int getExpiredTime() {
            return expiredTime;
        }

        private long getTimeout() {
            return timeout;
        }

        private String getLockTrueKey() {
            return lockTrueKey;
        }

        private GetLockKey invoke() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
            StringBuilder lockKey = new StringBuilder(distributedLock.lockKey());
            expiredTime = distributedLock.expiredTime();
            timeout = distributedLock.timeout();
            String prefix = distributedLock.lockKeyPrefix();
            String suffix = distributedLock.lockKeySuffix();
            String[] beanNames = distributedLock.lockKeyByBeanNames();
            String interval = distributedLock.interval();

            Assert.state(expiredTime > 0, "expiredTime 不能小于等于0");
            Assert.state(timeout > 0, "timeout 不能小于等于0");

            if (StringUtils.isBlank(lockKey.toString())) {
                Assert.notEmpty(beanNames, "lockKey 和 lockKeyByBeanNames 都为空！");
                Object[] args = joinPoint.getArgs();
                Assert.notEmpty(args, "目标方法无参数！");
                for (Object object : args) {
                    for (String beanName : beanNames) {
                        Assert.hasText(beanName, "beanName 为空！");
                        String upperCase = beanName.substring(0, 1).toUpperCase();
                        String substring = beanName.substring(1);
                        Method method = object.getClass().getMethod(GET + upperCase + substring);
                        Object invoke = method.invoke(object);
                        lockKey.append(invoke.toString()).append(interval);
                    }
                }
            }

            lockTrueKey = prefix + lockKey + suffix;
            if (StringUtils.isBlank(suffix) && lockTrueKey.length() > 0) {
                lockTrueKey = lockTrueKey.substring(0, lockTrueKey.length() - 1);
            }
            return this;
        }
    }

}
