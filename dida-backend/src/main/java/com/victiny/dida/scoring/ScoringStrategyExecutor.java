package com.victiny.dida.scoring;

import com.victiny.dida.common.ErrorCode;
import com.victiny.dida.exception.BusinessException;
import com.victiny.dida.model.entity.App;
import com.victiny.dida.model.entity.UserAnswer;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author Victiny
 * @Version 1.0
 * @Date create in 2024/7/18 20:13
 * 评分策略执行器
 */
@Service
public class ScoringStrategyExecutor {

//    策略列表
    @Resource
    private List<ScoringStrategy> scoringStrategyList;

    public UserAnswer doScore(List<String > choiceList, App app) throws Exception {
        Integer appType = app.getAppType();
        Integer appScoringStrategy = app.getScoringStrategy();
        if (appType == null || appScoringStrategy == null){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"应用配置有误，未找到匹配的策略");
        }

//        根据注解获取策略
        for (ScoringStrategy strategy : scoringStrategyList) {
//            先匹配到配置的评分配置类
            if (strategy.getClass().isAnnotationPresent(ScoringStrategyConfig.class)){
//                得到注解内容
                ScoringStrategyConfig scoringStrategyConfig = strategy.getClass().getAnnotation(ScoringStrategyConfig.class);
//                和配置的注解匹配
                if (scoringStrategyConfig.appType() == appType && scoringStrategyConfig.scoringStrategy() == appScoringStrategy){
                    return strategy.doScore(choiceList,app);
                }
            }
        }
        throw new BusinessException(ErrorCode.SYSTEM_ERROR,"应用配置有误，未找到匹配的策略");
    }
}
