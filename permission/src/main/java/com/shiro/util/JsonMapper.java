package com.shiro.util;

import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.ser.impl.SimpleFilterProvider;
import org.codehaus.jackson.type.TypeReference;

import java.io.Serializable;

/**
 * @Author: Mo
 * @Date: Created in  2018/6/4 13:30
 */
@Slf4j
public class JsonMapper {

    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.setFilters(new SimpleFilterProvider().setFailOnUnknownId(false));
        objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_EMPTY);
    }

    /**
     * 对象转字符串
     * @param src
     * @param <T>
     * @return
     */
    public static <T> String obj2String(T src) {
        if (src == null) {
            return null;
        }
        try {
            return src instanceof String ? (String)src : objectMapper.writeValueAsString(src);
        } catch (Exception ex) {
            log.warn("parse object to string exception, error:{}", ex);
            return null;
        }
    }

    /**
     * 字符串装对象
     * @param src
     * @param typeReference 需要转成的对象类型
     * @param <T>
     * @return
     */
    public static <T> T string2Obj(String src, TypeReference<T> typeReference) {
        if (src == null || typeReference == null) {
            return null;
        }
        try {
            return (T)(typeReference.getType().equals(String.class) ? src : objectMapper.readValue(src, typeReference));
        } catch (Exception ex) {
           log.warn("parse string to object exception, src:{},typeReference:{},error:{}", src, typeReference, ex);
           return null;
        }
    }

}
