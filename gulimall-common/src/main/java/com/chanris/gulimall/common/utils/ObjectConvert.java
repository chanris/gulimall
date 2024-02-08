package com.chanris.gulimall.common.utils;

import io.swagger.models.auth.In;
import org.springframework.lang.Nullable;

/**
 * @author chenyue7@foxmail.com
 * @date 6/2/2024
 * @description
 */
public class ObjectConvert {

    @Nullable
    public static Long toLong(Object origin) {
        Long target = null;
        if(origin instanceof String && ! origin.equals("")) {
            target = Long.parseLong((String) origin);
        } else if(origin instanceof Number) {
            target = (Long) origin;
        }
        return target;
    }

    @Nullable
    public static Integer toInteger(Object origin) {
        Integer target = null;
        if(origin instanceof  String && ! origin.equals("")) {
            target = Integer.parseInt((String) origin);
        }else if(origin instanceof  Number) {
            target = (Integer) origin;
        }
        return target;
    }
}
