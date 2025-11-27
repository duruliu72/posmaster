package com.osudpotro.posmaster.common;

import org.springframework.data.jpa.domain.Specification;

import java.util.Map;

public class SpecificationBuilder<T> {
    public Specification<T> build(Map<String, String> params, Class<T> type) {
        return null;
    }
}
