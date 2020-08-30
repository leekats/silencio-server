package com.silencio.server.models.enums;

public enum Indication {
    RED(0), GREEN(1), GOLD(2);

    private final int value;

    Indication(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
