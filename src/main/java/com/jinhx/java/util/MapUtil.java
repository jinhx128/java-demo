package com.jinhx.java.util;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * MapUtil
 *
 * @author jinhx
 * @since 2022-05-06
 */
@Slf4j
public class MapUtil {

    /**
     * object转换map
     *
     * @param obj obj
     * @return Map<String, Object>
     */
    public static <T> Map<String, Object> objectToMap(T obj) {
        if (Objects.isNull(obj)) {
            return Collections.emptyMap();
        }

        try {
            Field[] fields = obj.getClass().getDeclaredFields();
            Map<String, Object> map = new HashMap<>();
            for (Field field : fields) {
                field.setAccessible(true);
                map.put(field.getName(), field.get(obj));
            }
            return map;
        } catch (Exception e) {
            log.error("MapUtil objectToMap fail error=", e);
            return Collections.emptyMap();
        }
    }

    /**
     * map转换object
     *
     * @param map map
     * @param clazz clazz
     * @return T
     */
    public static <T> T mapToObject(Map<String, Object> map, Class<T> clazz) {
        if (Objects.isNull(map)) {
            return null;
        }

        try {
            T obj = clazz.newInstance();
            Field[] fields = obj.getClass().getDeclaredFields();
            for (Field field : fields) {
                int mod = field.getModifiers();
                if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                    continue;
                }
                field.setAccessible(true);
                field.set(obj, map.get(field.getName()));
            }
            return obj;
        } catch (Exception e) {
            log.error("MapUtil mapToObject fail error=", e);
            return null;
        }
    }

}
