package com.farmover.server.farmover.entities;

import java.util.stream.Stream;

public enum Crops {
    RICE,
    WHEAT,
    MAIZE,
    SUGARCANE,
    COTTON,
    SOYBEAN,
    CHICKPEA,
    GROUNDNUT,
    MUSTARD,
    BARLEY,
    MILLETS,
    PULSES,
    TEA,
    COFFEE,
    RUBBER,
    TOBACCO,
    BANANA,
    MANGO,
    COCONUT,
    TURMERIC;

    Stream<? extends Object> stream() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'stream'");
    }
}
