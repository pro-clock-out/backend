package com.hexcode.pro_clock_out.daily.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Satisfaction {
    VERY_BAD(1),
    BAD(2),
    NORMAL (3),
    GOOD(4),
    VERY_GOOD(5);

    private final int value;

    @JsonCreator
    public static Satisfaction deserializer(int value) {
        for (Satisfaction satisfaction : Satisfaction.values()) {
            if (satisfaction.getValue().equals(value)) {
                return satisfaction;
            }
        }
        return null;
    }

    @JsonValue
    public int serializer() {
        return value;
    }
}


