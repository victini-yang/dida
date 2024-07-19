package com.victiny.dida.scoring;

import com.victiny.dida.model.entity.App;
import com.victiny.dida.model.entity.UserAnswer;

import java.util.List;

/**
 * @Author Victiny
 * @Version 1.0
 * @Date create in 2024/7/18 10:08
 * 评分策略
 */
public interface ScoringStrategy {

    UserAnswer doScore(List<String> choices, App app) throws Exception;
}
