package com.atguigu.gmall.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.StringUtils;

import java.util.Map;

public class Jsons {
    private static ObjectMapper mapper = new ObjectMapper();
    /**
     * 把对象转为json字符串
     * @param object
     * @return
     */
    public static String toStr(Object object) {
        //jackson
        try {
            String s = mapper.writeValueAsString(object);
            return s;
        } catch (JsonProcessingException e) {
            return null;
        }
    }
    /**
     * 把json字符串转为对象
     * @param
     * @return
     */
    public static<T> T toObj(String strValue,Class<T> clazz) {
        if (StringUtils.isEmpty(strValue)){
            return null;
        }
        //jackson
        T t = null;
        try {
            t = mapper.readValue(strValue, clazz);
            return t;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
