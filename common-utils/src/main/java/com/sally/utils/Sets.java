package com.sally.utils;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Sets {
    public static <T> Set<T> of(T... elements) {
        return Stream.of(elements).filter(Objects::nonNull).collect(Collectors.toSet());
    }
}
