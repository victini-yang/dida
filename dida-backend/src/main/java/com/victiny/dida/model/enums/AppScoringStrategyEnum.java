package com.victiny.dida.model.enums;

import cn.hutool.core.util.ObjectUtil;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author Victiny
 * @Version 1.0
 * @Date create in 2024/7/16 16:53
 */
public enum AppScoringStrategyEnum {
    CUSTOM("自定义",0),
    AI("AI",1);

    private final String text;
    private final int value;

    AppScoringStrategyEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 根据value获取enum
     * @param value
     * @return
     */
    public static AppScoringStrategyEnum getEnumByValue(Integer value){
        if (ObjectUtil.isEmpty(value)){
            return null;
        }
        for (AppScoringStrategyEnum anEnum : AppScoringStrategyEnum.values()) {
            if (anEnum.value == value){
                return anEnum;
            }
        }
        return null;
    }

    /**
     * 获取值列表
     * @return
     */
    public static List<Integer> getValues(){
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    public String getText() {
        return text;
    }

    public int getValue() {
        return value;
    }
}
