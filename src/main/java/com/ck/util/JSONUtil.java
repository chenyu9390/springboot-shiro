package com.ck.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JSONUtil {

    /**
     * 对象转string
     * @param object
     * @return
     * @throws JsonProcessingException
     */
    public static String writeValue(Object object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }

    /**
     * string 转 对象
     * @param json  string
     * @param t 类
     * @return
     * @throws JsonProcessingException
     */
    public static <T> T readValue(String json, Class<T> t) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, t);
    }
}
