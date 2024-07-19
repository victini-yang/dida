package com.victiny.dida.scoring;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.victiny.dida.model.dto.question.QuestionContentDTO;
import com.victiny.dida.model.entity.App;
import com.victiny.dida.model.entity.Question;
import com.victiny.dida.model.entity.ScoringResult;
import com.victiny.dida.model.entity.UserAnswer;
import com.victiny.dida.model.vo.QuestionVO;
import com.victiny.dida.service.QuestionService;
import com.victiny.dida.service.ScoringResultService;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * @Author Victiny
 * @Version 1.0
 * @Date create in 2024/7/18 10:12
 */
@ScoringStrategyConfig(appType = 1, scoringStrategy = 0)
public class CustomTestScoringStrategy implements ScoringStrategy {

    @Resource
    private QuestionService questionService;

    @Resource
    private ScoringResultService scoringResultService;

    @Override
    public UserAnswer doScore(List<String> choices, App app) throws Exception {

//        1. 根据id查询到题目和题目结果信息
        Long appId = app.getId();
        Question question = questionService.getOne(
                Wrappers.lambdaQuery(Question.class).eq(Question::getAppId, appId)
        );

        List<ScoringResult> scoringResultList = scoringResultService.list(
                Wrappers.lambdaQuery(ScoringResult.class).eq(ScoringResult::getAppId, appId)
        );

//        2. 统计用户每个选择对应属性的个数，如I=10个E=5个
//        定义map用map减少一次循环，用空间换时间
        HashMap<String, Integer> optionCount = new HashMap<>();

        QuestionVO questionVO = QuestionVO.objToVo(question);
        List<QuestionContentDTO> questionContent = questionVO.getQuestionContent();

//        遍历题目列表
        for (QuestionContentDTO questionContentDTO : questionContent) {
//            遍历答案列表
            for (String answer : choices) {
//                遍历题目中的选项
                for (QuestionContentDTO.Option option : questionContentDTO.getOptions()) {
//                    如果用户选的答案和选项中的key相等
                    if (option.getKey().equals(answer)) {
//                        获取选项的result属性
                        String result = option.getResult();
//                        如果result属性不在optionCount中，初始化为0
                        if (!optionCount.containsKey(result)) {
                            optionCount.put(result, 0);
                        }
//                        在optionCount中增加计数
                        optionCount.put(result, optionCount.get(result) + 1);
                    }
                }
            }
        }

        int maxScore = 0;
        ScoringResult maxScoringResult = scoringResultList.get(0);
//        遍历评分结果列表
        for (ScoringResult scoringResult : scoringResultList) {
            List<String> resultProp = JSONUtil.toList(scoringResult.getResultProp(), String.class);
//            计算当前评分结果的分数
            int score = resultProp.stream()
                    .mapToInt(prop -> optionCount.getOrDefault(prop, 0))
                    .sum();
//            如果分数高于当前最高分数，更新最高分数对应的评分结果
            if (score > maxScore){
//                更新最高分数
                maxScore = score;
//                更新评分结果
                maxScoringResult = scoringResult;
            }
        }

//        4. 构造返回值，填充答案对象的属性
        UserAnswer userAnswer = new UserAnswer();
        userAnswer.setAppId(appId);
        userAnswer.setAppType(app.getAppType());
        userAnswer.setScoringStrategy(app.getScoringStrategy());
        userAnswer.setChoices(JSONUtil.toJsonStr(choices));
        userAnswer.setResultId(maxScoringResult.getId());
        userAnswer.setResultName(maxScoringResult.getResultName());
        userAnswer.setResultDesc(maxScoringResult.getResultDesc());
        userAnswer.setResultPicture(maxScoringResult.getResultPicture());

        return userAnswer;
    }
}
