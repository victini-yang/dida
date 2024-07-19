package com.victiny.dida.model.enums;

import cn.hutool.core.util.ObjectUtil;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author Victiny
 * @Version 1.0
 * @Date create in 2024/7/16 11:20
 */
public enum AppTypeEnum {
    SCORE("得分类",0),
    TEST("测评类",1);

    private final String text;

    private final int value;

    AppTypeEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 根据value获取枚举
     * @param value
     * @return
     */
    public static AppTypeEnum getEnumByValue(Integer value){
        if (ObjectUtil.isEmpty(value)){
            return null;
        }
        for (AppTypeEnum anEnum : AppTypeEnum.values()) {
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
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie");
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());

    }

    public String getText() {
        return text;
    }

    public int getValue() {
        return value;
    }
}
