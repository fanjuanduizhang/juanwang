package cn.surveyking.server.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiChatResponse {

	/**
	 * 响应状态：SUCCESS, ERROR, PENDING
	 */
	private String status;

	/**
	 * AI生成的问卷内容（原始响应）
	 */
	private String content;

	/**
	 * 转换后的问卷Schema
	 */
	private SurveySchema surveySchema;

	/**
	 * Markdown格式
	 */
	private String markdownContent;

	/**
	 * 解析的题目列表
	 */
	private List<SurveySchema> questions;

	/**
	 * 元数据（包含生成参数、模型信息等）
	 */
	@JsonFormat(shape = JsonFormat.Shape.OBJECT)
	private Map<String, Object> metadata;

	/**
	 * 错误信息
	 */
	private String error;

	/**
	 * 处理耗时（毫秒）
	 */
	private Long duration;

}
