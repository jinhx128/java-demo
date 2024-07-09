package com.jinhx.java.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.CollectionUtils;

import java.beans.PropertyDescriptor;
import java.util.*;

/**
 * bean相关的操作
 */
@Slf4j
public class BeanUtil {


    /**
     * 属性复制（浅拷贝）
     *
     * @param src    数据源
     * @param target 目标对象
     */
    public static void copy(Object src, Object target) {
        BeanUtils.copyProperties(src, target);
    }

    /**
     * 属性复制（浅拷贝）,忽略数据源的空值字段
     *
     * @param src    数据源
     * @param target 目标对象
     */
    public static void copyIgnoreNull(Object src, Object target) {
        BeanUtils.copyProperties(src, target, getNullPropertyNames(src));
    }

    /**
     * 找到属性值为空的所有字段
     *
     * @param source 数据源
     * @return 字段值为空的字段名
     */
    public static String[] getNullPropertyNames(Object source) {
        // 借助spring提供的BeanWrapper来访问一个对象
        final BeanWrapper src = new BeanWrapperImpl(source);
        // 获取到所有的属性
        PropertyDescriptor[] pds = src.getPropertyDescriptors();
        Set<String> emptyNames = new HashSet<>();
        for (PropertyDescriptor pd : pds) {
            // 这里类似于通过反射获取值
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }
        return emptyNames.toArray(new String[] {});
    }


    /**
     * 转换
     *
     * @param src        数据源
     * @param targetType 目标类型
     * @return 目标类型实例
     */
    public static <T> T covert(Object src, Class<T> targetType) {
        return covertIgnore(src, targetType, null);
    }


    /**
     * 转换，支持指定忽略的对象
     *
     * @param src              数据源
     * @param targetType       目标类型
     * @param ignoreProperties 需要忽略的属性
     * @return 目标类型实例
     */
    public static <T> T covertIgnore(Object src, Class<T> targetType, String[] ignoreProperties) {
        try {
            T target = targetType.newInstance();
            BeanUtils.copyProperties(src, target, ignoreProperties);
            return target;
        } catch (Exception e) {
            log.error("covertIgnore src={} to target={} failed, ignoreProperties={}", src, targetType.getSimpleName(), ignoreProperties, e);
            throw new RuntimeException("系统内部异常", e);
        }
    }

    /**
     * 批量转换
     *
     * @param srcList    数据源集合
     * @param targetType 目标类型
     * @return 目标实例集合
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> batchCovert(List<?> srcList, Class<T> targetType) {
        try {
            if (CollectionUtils.isEmpty(srcList)){
                return Collections.emptyList();
            }
            T targetObject;
            List<T> targetList = new ArrayList<>();
            for (Object src : srcList) {
                targetObject = covert(src, targetType);
                targetList.add(targetObject);
            }
            return targetList;
        } catch (Exception e) {
            log.error("batchCovert srcList={} to target={} failed", srcList.getClass(), targetType, e);
            throw new RuntimeException("系统内部异常", e);
        }
    }

}