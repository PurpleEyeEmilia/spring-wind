package com.github.springwind.common.utils;

import com.github.springwind.common.exception.CommonException;
import com.github.springwind.core.canal.entity.CanalChangeInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author pengnian
 * @version V1.0
 * @date 2019/10/9 13:39
 * @Desc
 */
@Slf4j
public class CanalEntityParser {

    private static Pattern linePattern = Pattern.compile("_(\\w)");

    public static <T> T parse(List<CanalChangeInfo> canalChangeData, Class<T> dataClass) {
        if (CollectionUtils.isEmpty(canalChangeData) || dataClass == null) {
            return null;
        }

        final T instance;
        try {
            instance = dataClass.newInstance();
        } catch (Exception e) {
            log.error("生成实体对象异常！", e);
            throw new CommonException("生成实体对象异常！");
        }

        final Field[] declaredFields = instance.getClass().getDeclaredFields();
        canalChangeData.forEach(canalChangeInfo -> {
            String columnName = convertColumnName(canalChangeInfo.getColumnName());
            for (Field declaredField : declaredFields) {
                declaredField.setAccessible(true);
                if (declaredField.getName().equals(columnName)) {
                    Object columnValue = typeConvert(canalChangeInfo.getColumnValue(), declaredField);
                    try {
                        declaredField.set(instance, columnValue);
                    } catch (IllegalAccessException e) {
                        log.error("生成实体对象异常！", e);
                        throw new CommonException("设置实体对象字段异常！");
                    }
                }
            }
        });

        return instance;
    }

    private static Object typeConvert(String value, Field declaredField) {
        Class<?> paramType = declaredField.getType();

        if (paramType.equals(String.class)) {
            return value;
        }
        if (StringUtils.isBlank(value)) {
            return null;
        }

        if (paramType.equals(Integer.class)) {
            return Integer.valueOf(value);
        } else if (paramType.equals(Long.class)) {
            return Long.valueOf(value);
        } else if (paramType.equals(Boolean.class)) {
            return Boolean.valueOf(value);
        } else if (paramType.equals(Byte.class)) {
            return Byte.valueOf(value);
        } else if (paramType.equals(Short.class)) {
            return Short.valueOf(value);
        } else if (paramType.equals(Double.class)) {
            return Double.valueOf(value);
        } else if (paramType.equals(Float.class)) {
            return Float.valueOf(value);
        } else if (paramType.equals(Character.class)) {
            return value.charAt(0);
        } else if (paramType.equals(Date.class)){
            SimpleDateFormat fmt=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                return fmt.parse(value);
            } catch (ParseException e) {
                return null;
            }
        }else{
            return null;
        }
    }

    public static <T> T parse(T source, List<CanalChangeInfo> canalChangeData, Class<T> dataClass) {
        if (CollectionUtils.isEmpty(canalChangeData) || dataClass == null) {
            return null;
        }

        final T instance;
        try {
            instance = dataClass.newInstance();
        } catch (Exception e) {
            log.error("生成实体对象异常！", e);
            throw new CommonException("生成实体对象异常！");
        }
        BeanUtils.copyProperties(source, instance);
        final Field[] declaredFields = instance.getClass().getDeclaredFields();
        canalChangeData.forEach(canalChangeInfo -> {
            String columnName = convertColumnName(canalChangeInfo.getColumnName());
            for (Field declaredField : declaredFields) {
                declaredField.setAccessible(true);
                if (declaredField.getName().equals(columnName)) {
                    try {
                        declaredField.set(instance, canalChangeInfo.getColumnValue());
                    } catch (IllegalAccessException e) {
                        log.error("生成实体对象异常！", e);
                        throw new CommonException("设置实体对象字段异常！");
                    }
                }
            }
        });

        return instance;
    }


    private static String convertColumnName(String columnName) {
        Matcher matcher = linePattern.matcher(columnName);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

}
