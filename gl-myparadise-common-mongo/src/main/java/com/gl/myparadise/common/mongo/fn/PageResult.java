package com.gl.myparadise.common.mongo.fn;

import lombok.Getter;
import org.apache.commons.lang3.ObjectUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PageResult<T> extends ArrayList<T> {

    @Getter
    private long total = 0;

    public <I> PageResult(Collection<I> result, long total, Function<I, T> mapping) {
        if (!ObjectUtils.isEmpty(result)) {
            this.addAll(result.stream().map(mapping).collect(Collectors.toList()));
            this.total = total;
        }
    }

    public List<T> getResult() {
        return this;
    }

}