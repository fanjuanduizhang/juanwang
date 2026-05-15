package cn.surveyking.server.ai.service;

import cn.surveyking.server.domain.dto.AiChatRequest;
import cn.surveyking.server.domain.dto.AiChatResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.CompletableFuture;

public interface AiIntegrationService {

	/**
	 * 与AI进行对话交互
	 * @param request AI聊天请求
	 * @return AI聊天响应
	 */
	AiChatResponse chat(AiChatRequest request);

	/**
	 * 异步与AI进行对话交互
	 * @param request AI聊天请求
	 * @return 异步AI聊天响应
	 */
	CompletableFuture<AiChatResponse> chatAsync(AiChatRequest request);

	/**
	 * 根据提示词生成问卷
	 * @param prompt 提示词
	 * @param surveyType 问卷类型
	 * @return AI聊天响应
	 */
	AiChatResponse generateSurvey(String prompt, String surveyType);

	/**
	 * 基于文件内容生成问卷
	 * @param file 上传的文件
	 * @param additionalPrompt 额外提示词
	 * @return AI聊天响应
	 */
	AiChatResponse generateSurveyFromFile(MultipartFile file, String additionalPrompt);

	/**
	 * 优化已有问卷
	 * @param surveyContent 问卷内容（Markdown格式）
	 * @param optimizationPrompt 优化提示词
	 * @return AI聊天响应
	 */
	AiChatResponse optimizeSurvey(String surveyContent, String optimizationPrompt);

	/**
	 * 检查AI服务健康状态
	 * @return 是否可用
	 */
	boolean healthCheck();

}
