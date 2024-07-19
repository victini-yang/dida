package com.victiny.dida.common;

import lombok.Data;

/**
 * @Author Victiny
 * @Version 1.0
 * @Date create in 2024/7/17 23:11
 */
@Data
public class ReviewRequest {

    /**
     * id
     */
    private Long id;

    /**
     * 审核状态0:待审核，1：通过，2：拒绝
     */
    private Integer reviewStatus;

    /**
     * 审核信息
     */
    private String reviewMessage;

    private static final long seriaVersionUID = 1L;
}
