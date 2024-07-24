package com.victiny.dida.model.dto.question;

import lombok.Data;

/**
 * @Author Victiny
 * @Version 1.0
 * @Date create in 2024/7/24 11:22
 */
@Data
public class QuestionAnswerDTO {

    /**
     * 题目
     */
    private String Title;

    /**
     * 用户答案
     */
    private String userAnswer;
}
