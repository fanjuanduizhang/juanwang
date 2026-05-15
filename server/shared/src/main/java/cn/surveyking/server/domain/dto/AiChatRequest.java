package cn.surveyking.server.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiChatRequest {

	/**
	 * 用户消息
	 */
	private String message;

	/**
	 * 问卷类型（问卷、考试、投票等）
	 */
	private String surveyType;

	/**
	 * 使用的AI模型
	 */
	private String model;

	/**
	 * 对话历史
	 */
	private String conversationHistory;

	/**
	 * 额外参数
	 */
	private String extraParams;

}
