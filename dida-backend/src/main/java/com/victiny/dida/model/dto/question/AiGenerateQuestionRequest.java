package com.victiny.dida.model.dto.question;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author Victiny
 * @Version 1.0
 * @Date create in 2024/7/23 16:47
 * AI 生成题目请求
 */
@Data
public class AiGenerateQuestionRequest implements Serializable {
//    涉及Json字符串转换要实现序列化接口

    /**
     * 应用 id
     */
    private Long appId;

    /**
     * 题目数
     */
    int questionNumber = 10;

    /**
     * 选项数
     */
    int optionNumber = 2;

    private static final long serialVersionUID = 1L;
}
