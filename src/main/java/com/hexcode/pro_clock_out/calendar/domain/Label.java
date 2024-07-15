package com.hexcode.pro_clock_out.calendar.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Label {
    WORK("작업"),
    REST("휴식"),
    SLEEP("수면"),
    PERSONAL("개인생활"),
    HEALTH("건강");

    private final String value;

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
