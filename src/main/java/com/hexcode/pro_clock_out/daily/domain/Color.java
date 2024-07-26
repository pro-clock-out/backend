package com.hexcode.pro_clock_out.daily.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Color {
    WORK( "#7AA2E3"),
    REST( "#7A7EE3"),
    SLEEP( "#6AD4DD"),
    PERSONAL("#97E7E1"),
    HEALTH( "#F8F6E3");

    private final String hexcolor;

    @JsonCreator
    public static Color deserializer(String hexcolor)  {
        for (Color color : Color.values()) {
            if (color.getHexcolor().equals(hexcolor)) {
                return color;
            }
        }
        return null;
    }

    @JsonValue
    public String serializer() {
        return hexcolor;
    }
}

