package com.jinhx.java.util;

import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * ListUtil
 *
 * @author jinhx
 * @since 2022-01-10
 */
@Slf4j
public class ListUtil {

    /**
     * 分割list集合
     *
     * @param list 集合数据
     * @param splitSize 每组分割几个
     * @return 分割后的集合
     */
    public static <T> List<List<T>> splitByInsideListSize(List<T> list, int splitSize) {
        // 判断集合是否为空
        if (Objects.isNull(list) || list.size() < 1){
            return Collections.emptyList();
        }

        if (splitSize < 1){
            return Collections.singletonList(list);
        }

        // 计算分割后的大小
        int maxSize = (list.size() + splitSize - 1) / splitSize;

        return Stream.iterate(0, n -> n + 1)
                .limit(maxSize)
                .map(a -> list.stream().skip((long) a * splitSize).limit(splitSize).collect(Collectors.toList()))
                .filter(b -> !b.isEmpty())
                .collect(Collectors.toList());
    }

    /**
     * 分割list集合
     *
     * @param list 集合数据
     * @param splitSize 均分为几组
     * @return 分割后的集合
     */
    public static <T> List<List<T>> splitByOutsideListSize(List<T> list, int splitSize) {
        // 判断集合是否为空
        if (Objects.isNull(list) || list.size() < 1){
            return Collections.emptyList();
        }

        if (splitSize < 2){
            return Collections.singletonList(list);
        }

        // 计算分割后的大小
        int maxSize = (list.size() + splitSize - 1) / splitSize;

        return Stream.iterate(0, n -> n + 1)
                .limit(splitSize)
                .map(a -> list.stream().skip((long) a * maxSize).limit(maxSize).collect(Collectors.toList()))
                .filter(b -> !b.isEmpty())
                .collect(Collectors.toList());
    }

}
