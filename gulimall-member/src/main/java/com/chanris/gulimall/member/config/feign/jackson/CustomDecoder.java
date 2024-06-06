package com.chanris.gulimall.member.config.feign.jackson;


import com.chanris.gulimall.common.page.PageData;
import com.chanris.gulimall.common.to.OrderTo;
import com.chanris.gulimall.common.utils.Result;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import feign.Response;
import feign.codec.DecodeException;
import feign.codec.Decoder;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

/**
 * @author chenyue7@foxmail.com
 * @date 2024/6/2
 * @description 解决feign无法解码嵌套泛型对象的问题
 */
public class CustomDecoder implements Decoder {
    private final ObjectMapper om;

    public CustomDecoder() {
        this.om = new ObjectMapper();
    }

    /**
     *
     * @param response 从http请求中接收到的响应，包含响应的所有细节，状态吗、头信息以及响应体
     * @param type feign客户端希望将响应体的数据转换成的目标对象
     * @return
     * @throws IOException
     * @throws DecodeException
     * @throws FeignException
     */
    @Override
    public Object decode(Response response, Type type) throws IOException, DecodeException, FeignException {
        if (response.body() == null) {
            return null;
        }
        try {
            if (type instanceof Class<?>) {
                return om.readValue(response.body().asInputStream(), (Class<?>) type);
            }else {
                //处理 泛型类型
                /*if (type instanceof ParameterizedType) {
                    return handleGenerics(response, type);
                }else {
                    throw new RuntimeException("response body decoding unsupported");
                }*/
                TypeReference<?> typeReference = new TypeReference<Result<PageData<OrderTo>>>() {};
                return om.readValue(response.body().asInputStream(), typeReference);
            }
        }catch (IOException e) {
            throw new DecodeException(response.status(),"Error decoding response body", response.request());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object handleGenerics(Response response, Type type) throws Exception{
        JsonNode jsonNode = om.readTree(response.body().asInputStream());
        return doHandle(jsonNode, type);
    }

    private Object doHandle(JsonNode jsonNode, Type type) throws Exception {
        int len = ((ParameterizedType) type).getActualTypeArguments().length;
        for (int i = 0; i < len; i++) {
            doHandle(jsonNode, ((ParameterizedType) type).getActualTypeArguments()[i]);
        }
        String className = type.getTypeName();
        Class<?> rClazz = Class.forName(className);
        Constructor<?> declaredConstructor = rClazz.getDeclaredConstructor();
        declaredConstructor.newInstance();

        return null;
    }

    private static String inputStreamToString(InputStream inputStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            stringBuilder.append(new String(buffer, 0, bytesRead, StandardCharsets.UTF_8));
        }
        return stringBuilder.toString();
    }
}
