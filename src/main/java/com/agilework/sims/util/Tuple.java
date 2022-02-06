package com.agilework.sims.util;

import lombok.Getter;

@Getter
public class Tuple <A, B> {
    private final A first;
    private final B second;

    public Tuple(A a, B b) {
        this.first = a;
        this.second = b;
    }
}
