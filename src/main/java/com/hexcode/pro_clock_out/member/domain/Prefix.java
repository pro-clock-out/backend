package com.hexcode.pro_clock_out.member.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Prefix {
    NORMAL("일반인"),
    BEST("최고의 워라밸을 가진"),
    VALANCED("균형 잡힌 삶을 사는"),
    AVERAGE("대한민국 평균"),
    SLEEPER("잠꾸러기 미녀"),
    OVER_REST("과다 휴식"),
    IMPOVERISHED("피폐한 삶을 사는"),
    DILIGENT("부지런한 개미"),
    HEALTHY("튼튼한");

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
