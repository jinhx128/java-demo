package com.jinhx.java.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CommonUtil {

    public static <T, R> List<R> toList(Collection<T> collection, Function<T, R> function) {
        return collection == null || collection.isEmpty() ? new ArrayList<>(0) :
                collection.stream().map(function).collect(Collectors.toList());
    }

}