package cn.surveyking.server.ai.service;

import cn.surveyking.server.domain.dto.SurveySchema;

public interface MarkdownConverterService {

	/**
	 * 将问卷Schema转换为Markdown格式
	 * @param surveySchema 问卷Schema
	 * @return Markdown字符串
	 */
	String toMarkdown(SurveySchema surveySchema);

	/**
	 * 从Markdown字符串解析为问卷Schema
	 * @param markdown Markdown字符串
	 * @return 问卷Schema
	 */
	SurveySchema fromMarkdown(String markdown);

	/**
	 * 生成完整Markdown（包含题目和选项）
	 * @param surveySchema 问卷Schema
	 * @param includeAnswers 是否包含答案
	 * @return 完整的Markdown字符串
	 */
	String generateFullMarkdown(SurveySchema surveySchema, boolean includeAnswers);

	/**
	 * 生成单个题目的Markdown
	 * @param question 题目Schema
	 * @return 题目的Markdown字符串
	 */
	String generateQuestionMarkdown(SurveySchema question);

	/**
	 * 验证Markdown格式是否有效
	 * @param markdown Markdown字符串
	 * @return 是否有效
	 */
	boolean validateMarkdown(String markdown);

}
