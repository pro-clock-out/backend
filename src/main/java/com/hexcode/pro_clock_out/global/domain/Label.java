package com.hexcode.pro_clock_out.global.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Label {
    TOTAL("종합", "#000000"),
    WORK("작업","#7AA2E3"),
    REST("휴식","#7A7EE3"),
    SLEEP("수면","#6AD4DD"),
    PERSONAL("개인생활","#97E7E1"),
    HEALTH("건강","#F8F6E3"),
    ETC("기타","#696969");

    private final String value;
    private final String color;

    @JsonCreator
    public static Label deserializer(String value){
        for(Label label : Label.values()){
            if(label.getValue().equals(value)) {
                return label;
            }
        }
        return null;
    }
    @JsonValue
    public String serializer() { return  value; }
}
