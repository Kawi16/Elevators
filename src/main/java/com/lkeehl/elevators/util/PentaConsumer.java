package com.lkeehl.elevators.util;

public interface PentaConsumer<K, V, S, T, P> {

    void accept(K k, V v, S s, T t, P p);
}

