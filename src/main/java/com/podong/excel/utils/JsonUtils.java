package com.podong.excel.utils;

import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.podong.excel.CommonException;
import java.io.IOException;
import java.util.Map;

public class JsonUtils {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static String toJson(Object object) {
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, true);

        String json = "";
        try {
            json = mapper.writeValueAsString(object);
        } catch (Exception e) {
            new CommonException(e.getMessage(), e);

        }

        return json;
    }

    public static <T> T toObject(String json, TypeReference<T> valueTypeRef) {
        mapper.configure(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature(), true);

        T object = null;

        try {
            object = mapper.readValue(json, valueTypeRef);
        } catch (Exception e) {
            new CommonException(e.getMessage(), e);
        }

        return object;
    }

    public static Map<String, Object> toMap(String json) {
        try {
            return mapper.readValue(json, new TypeReference<Map<String, Object>>() {});
        } catch (IOException e) {
            new CommonException(e.getMessage(), e);
        }
        return null;
    }



}
