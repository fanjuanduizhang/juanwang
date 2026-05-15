package cn.surveyking.server.domain.dto;

import lombok.Data;

import java.util.Map;

/**
 * 人工评分请求
 */
@Data
public class ManualScoreRequest {

    private String answerId;

    private String questionId;

    private Double score;

    private Map<String, Object> answer;
}