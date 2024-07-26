package com.hexcode.pro_clock_out.member.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Lifestyle {
    RICH("부유한 삶"),
    COMFORT("편안한 삶"),
    HARMONIOUS("화목한 삶"),
    RELAXED("여유로운 삶"),
    LOVING("사랑이 가득한 삶"),
    WELL_SLEPT("숙면하는 삶"),
    CONSIDERATE("배려하는 삶"),
    LESS_WORKING("적게 일하는 삶"),
    RIGHT("바른 삶"),
    HEALTHY("건강한 삶");

    private final String value;

    @JsonCreator
    public static Lifestyle deserializer(String value) {
        for(Lifestyle lifeStyle : Lifestyle.values()){
            if(lifeStyle.getValue().equals(value)) {
                return lifeStyle;
            }
        }
        return null;
    }

    @JsonValue
    public String serializer(){
        return value;
    }
}
