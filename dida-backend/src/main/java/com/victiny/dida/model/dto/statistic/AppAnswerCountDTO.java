package com.victiny.dida.model.dto.statistic;

import lombok.Data;

/**
 * @Author Victiny
 * @Version 1.0
 * @Date create in 2024/8/3 15:22
 */
@Data
public class AppAnswerCountDTO {

    private Long appId;

    /**
     * 用户提交答案数量
     */
    private Long answerCount;
}
