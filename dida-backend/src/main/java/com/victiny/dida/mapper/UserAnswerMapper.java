package com.victiny.dida.mapper;

import com.victiny.dida.model.dto.statistic.AppAnswerCountDTO;
import com.victiny.dida.model.dto.statistic.AppAnswerResultCountDTO;
import com.victiny.dida.model.entity.UserAnswer;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author 82659
* @description 针对表【user_answer(用户答题记录)】的数据库操作Mapper
* @createDate 2024-07-16 10:11:59
* @Entity com.victiny.dida.model.entity.UserAnswer
*/
public interface UserAnswerMapper extends BaseMapper<UserAnswer> {

    @Select("select appId , count(userId) as answerCount from user_answer\n" +
            "    group by appId order by answerCount desc limit 10;")
    List<AppAnswerCountDTO> doAnswerCount();

    @Select("select resultName,count(resultName) as resultCount from user_answer\n" +
            "   where appId = #{appId}" +
            "    group by resultName order by resultCount desc;")
    List<AppAnswerResultCountDTO> doAppAnswerResultCount(Long appId);
}




