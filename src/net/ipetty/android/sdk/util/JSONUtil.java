/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ipetty.android.sdk.util;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import net.ipetty.android.sdk.core.APIException;

import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

/**
 *
 * @author Administrator
 */
public class JSONUtil {

    private final String TAG = "JSONUtil";

    public static String toJson(Object o) {
        ObjectMapper mapper = new ObjectMapper();
        Writer str = new StringWriter();
        try {
            mapper.writeValue(str, o);
        } catch (IOException ex) {
            throw new APIException(ex);
        }
        return str.toString();
    }

    /**
     * TypeReference<List<String>> valueTypeRef = new
     * TypeReference<List<String>>(){}; fromJSON(valueTypeRef);
     *
     * @param <T>
     * @param str
     * @param valueTypeRef
     * @return
     */
    public static <T> T fromJSON(String str, TypeReference<T> valueTypeRef) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        T t;
        try {
            t = mapper.readValue(str, valueTypeRef);
        } catch (IOException ex) {
            throw new APIException(ex);
        }
        return t;
    }

    /**
     * fromJSON(String.class); fromJSON(Class.forName("xx.yy.ZZ"))
     *
     * @param <T>
     * @param str
     * @param type
     * @return
     */
    public static <T> T fromJSON(String str, Class<T> type) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        T t;
        try {
            t = mapper.readValue(str, type);
        } catch (IOException ex) {
            throw new APIException(ex);
        }
        return t;
    }

    public static <T> T fromJSON(String str, String classType) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Class clazz;
        try {
            clazz = Class.forName(classType);
        } catch (ClassNotFoundException ex) {
            throw new APIException("无法找到类型", ex);
        }
        T t;
        try {
            t = (T) mapper.readValue(str, clazz);
        } catch (IOException ex) {
            throw new APIException(ex);
        }
        return t;
    }
}
