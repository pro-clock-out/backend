package com.hexcode.pro_clock_out.wolibal.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Prefix {
    VERY_GOOD("최고의 워라밸을 가진"),
    GOOD("균형이 좋은"),
    NORMAL("보통인"),
    BAD("개선이 필요한"),
    VERY_BAD("균형이 심각한");

    private final String value;

    @JsonCreator
    public static Prefix deserializer(String value) {
        for(Prefix prefix : Prefix.values()){
            if(prefix.getValue().equals(value)) {
                return prefix;
            }
        }
        return null;
    }

    @JsonValue
    public String serializer(){
        return value;
    }
}
