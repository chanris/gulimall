package com.chanris.gulimall.member;

import com.chanris.gulimall.common.page.PageData;
import com.chanris.gulimall.common.to.OrderTo;
import com.chanris.gulimall.common.utils.Result;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author chenyue7@foxmail.com
 * @date 2024/6/2
 * @description
 */
public class ReflectTests {
    public static void main(String[] args) {
        Result<PageData<OrderTo>> res = new Result<>();
    }

    static class GenericTypeResolver {

        public static Class<?> getNestedGenericType(Class<?> clazz, int level) {
            return getNestedGenericType(clazz, level, 0);
        }

        private static Class<?> getNestedGenericType(Class<?> clazz, int targetLevel, int currentLevel) {
            Type genericSuperclass = clazz.getGenericSuperclass();

            if (genericSuperclass instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
                Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();

                if (currentLevel == targetLevel - 1) {
                    return (Class<?>) actualTypeArguments[0];
                } else {
                    return getNestedGenericType((Class<?>) actualTypeArguments[0], targetLevel, currentLevel + 1);
                }
            }

            return null;
        }
    }
}
