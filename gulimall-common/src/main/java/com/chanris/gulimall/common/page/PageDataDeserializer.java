package com.chanris.gulimall.common.page;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.List;

/**
 * @author chenyue7@foxmail.com
 * @date 2024/6/2
 * @description
 */
public class PageDataDeserializer<T> extends JsonDeserializer<PageData<T>> {
    @Override
    public PageData<T> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        List<T> list = (List<T>) (node.get("list"));

        long total = node.get("total").asLong();
        return new PageData<T>(list, total);
    }
}
