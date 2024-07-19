package com.hexcode.pro_clock_out.daily.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Satisfaction {
    VERY_BAD("매우 만족"),
    BAD("만족"),
    NORMAL ("보통"),
    GOOD("불만족"),
    VERY_GOOD("매우 불만족");

    private final String value;

    @JsonCreator
    public static Satisfaction deserializer(String value) {
        for(Satisfaction satisfaction : Satisfaction.values()){
            if(satisfaction.getValue().equals(value)) {
                return satisfaction;
            }
        }
        return null;
    }

    @JsonValue
    public String serializer() {
        return value;
    }
}


