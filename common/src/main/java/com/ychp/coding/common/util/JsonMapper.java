package com.ychp.coding.common.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

/**
 * Desc:
 * Author: <a href="ychp@terminus.io">应程鹏</a>
 * Date: 16/7/23
 */
public class JsonMapper {

    public static JsonMapper JSON_NON_DEFAULT_MAPPER;

    private ObjectMapper mapper = new ObjectMapper();

    static {
        JSON_NON_DEFAULT_MAPPER = new JsonMapper(JsonInclude.Include.NON_DEFAULT);
    }

    public JsonMapper(JsonInclude.Include include){
        this.mapper.setSerializationInclusion(include);
        this.mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        this.mapper.registerModule(new GuavaModule());
    }

    public <T> T fromJson(String json, Class<T> type){
        if(StringUtils.isEmpty(json)){
            return null;
        }
        try {
            return this.mapper.readValue(json, type);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public <T> T fromJson(String json,JavaType javaType) {
        if(StringUtils.isEmpty(json)){
            return null;
        }
        try {
            return this.mapper.readValue(json, javaType);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String toJson(Object object) throws JsonProcessingException {
        try {
            return this.mapper.writeValueAsString(object);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public JavaType createCollectionType(Class<?> collectionClass, Class... elementClasses) {
        return this.mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }


}
