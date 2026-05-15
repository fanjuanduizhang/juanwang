package cn.surveyking.server.ai.service.impl;

import cn.surveyking.server.ai.service.AiIntegrationService;
import cn.surveyking.server.ai.service.MarkdownConverterService;
import cn.surveyking.server.domain.dto.AiChatRequest;
import cn.surveyking.server.domain.dto.AiChatResponse;
import cn.surveyking.server.domain.dto.SurveySchema;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class AiIntegrationServiceImpl implements AiIntegrationService {

	private final RestTemplate restTemplate;

	private final ObjectMapper objectMapper;

	private final MarkdownConverterService markdownConverterService;

	// SiliconFlow 配置
	@Value("${surveyking.ai.siliconflow.enabled:true}")
	private boolean siliconflowEnabled;

	@Value("${surveyking.ai.siliconflow.base-url:https://api.siliconflow.cn}")
	private String siliconflowBaseUrl;

	@Value("${surveyking.ai.siliconflow.token:}")
	private String siliconflowToken;

	@Value("${surveyking.ai.siliconflow.model-id:deepseek-chat}")
	private String siliconflowModelId;

	// 通用 AI 配置
	@Value("${surveyking.ai.max-tokens:2000}")
	private int maxTokens;

	@Value("${surveyking.ai.temperature:0.7}")
	private double temperature;

	// 动态配置
	private String aiApiUrl;

	private String aiApiKey;

	private String aiModel;

	@PostConstruct
	public void init() {
		if (siliconflowEnabled) {
			this.aiApiUrl = siliconflowBaseUrl + "/v1/chat/completions";
			this.aiApiKey = siliconflowToken;
			this.aiModel = siliconflowModelId;
			log.info("AI服务初始化 - SiliconFlow: url={}, model={}", aiApiUrl, aiModel);
		}
		else {
			this.aiApiUrl = "https://api.openai.com/v1/chat/completions";
			this.aiApiKey = "";
			this.aiModel = "gpt-3.5-turbo";
			log.info("AI服务初始化 - OpenAI: url={}, model={}", aiApiUrl, aiModel);
		}
	}

	private static final String SURVEY_SYSTEM_PROMPT = """
			你是一个专业的问卷设计专家。请根据用户的需求生成结构化的问卷。

			问卷应包含以下元素：
			1. 问卷标题
			2. 问卷说明/欢迎语
			3. 题目列表，每道题包含：
			   - 题目标题
			   - 题目类型（single单选/multiple多选/text填空/judge判断）
			   - 选项列表（如果是选择题）

			请以JSON格式返回，结构如下：
			{
			  "title": "问卷标题",
			  "description": "问卷说明",
			  "children": [
			    {
			      "title": "题目标题",
			      "questionType": "single",
			      "children": [
			        {"title": "选项A"},
			        {"title": "选项B"}
			      ]
			    }
			  ]
			}

			确保题目清晰、选项合理、逻辑连贯。
			""";

	@Override
	public AiChatResponse chat(AiChatRequest request) {
		long startTime = System.currentTimeMillis();

		try {
			String userMessage = request.getMessage();
			String surveyType = request.getSurveyType() != null ? request.getSurveyType() : "问卷";

			// 构建更具体的提示词
			String enhancedPrompt = buildEnhancedPrompt(userMessage, surveyType);

			// 调用AI API
			String aiResponse = callAiApi(enhancedPrompt);

			// 解析AI响应
			SurveySchema surveySchema = parseAiResponse(aiResponse);

			// 生成Markdown
			String markdown = markdownConverterService.generateFullMarkdown(surveySchema, false);

			// 构建元数据
			Map<String, Object> metadata = new HashMap<>();
			metadata.put("model", aiModel);
			metadata.put("surveyType", surveyType);
			metadata.put("generatedAt", LocalDateTime.now().toString());

			return AiChatResponse.builder().status("SUCCESS").content(aiResponse).surveySchema(surveySchema)
					.markdownContent(markdown).questions(surveySchema != null ? surveySchema.getChildren() : null)
					.metadata(metadata).duration(System.currentTimeMillis() - startTime).build();

		}
		catch (Exception e) {
			log.error("AI对话失败", e);
			return AiChatResponse.builder().status("ERROR").error("AI对话失败: " + e.getMessage())
					.duration(System.currentTimeMillis() - startTime).build();
		}
	}

	@Override
	public CompletableFuture<AiChatResponse> chatAsync(AiChatRequest request) {
		return CompletableFuture.supplyAsync(() -> chat(request));
	}

	@Override
	public AiChatResponse generateSurvey(String prompt, String surveyType) {
		AiChatRequest request = AiChatRequest.builder().message(prompt).surveyType(surveyType).build();
		return chat(request);
	}

	@Override
	public AiChatResponse generateSurveyFromFile(MultipartFile file, String additionalPrompt) {
		long startTime = System.currentTimeMillis();

		try {
			// 读取文件内容
			String fileContent = readFileContent(file);

			// 构建提示词
			String prompt = String.format("请根据以下文件内容生成一份问卷：\n\n文件内容：\n%s\n\n额外要求：%s", fileContent,
					additionalPrompt != null ? additionalPrompt : "请根据内容设计合适的问卷");

			AiChatRequest request = AiChatRequest.builder().message(prompt).build();

			return chat(request);

		}
		catch (Exception e) {
			log.error("从文件生成问卷失败", e);
			return AiChatResponse.builder().status("ERROR").error("文件处理失败: " + e.getMessage())
					.duration(System.currentTimeMillis() - startTime).build();
		}
	}

	@Override
	public AiChatResponse optimizeSurvey(String surveyContent, String optimizationPrompt) {
		long startTime = System.currentTimeMillis();

		try {
			String prompt = String.format("请优化以下问卷内容：\n\n%s\n\n优化要求：%s\n\n请保持原有结构，只进行优化调整。", surveyContent,
					optimizationPrompt != null ? optimizationPrompt : "使题目更加清晰、选项更加合理");

			AiChatRequest request = AiChatRequest.builder().message(prompt).build();

			return chat(request);

		}
		catch (Exception e) {
			log.error("优化问卷失败", e);
			return AiChatResponse.builder().status("ERROR").error("优化失败: " + e.getMessage())
					.duration(System.currentTimeMillis() - startTime).build();
		}
	}

	@Override
	public boolean healthCheck() {
		try {
			// 发送简单的测试请求
			Map<String, Object> testRequest = new HashMap<>();
			testRequest.put("model", aiModel);

			List<Map<String, String>> messages = List.of(Map.of("role", "user", "content", "ping"));
			testRequest.put("messages", messages);
			testRequest.put("max_tokens", 5);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.setBearerAuth(aiApiKey);

			HttpEntity<Map<String, Object>> entity = new HttpEntity<>(testRequest, headers);

			restTemplate.postForEntity(aiApiUrl, entity, String.class);
			log.info("AI服务健康检查通过: {}", aiApiUrl);
			return true;
		}
		catch (Exception e) {
			log.warn("AI服务健康检查失败: {}", e.getMessage());
			return false;
		}
	}

	/**
	 * 切换AI模型
	 * @param modelId 模型ID
	 */
	public void switchModel(String modelId) {
		log.info("切换AI模型: {} -> {}", this.aiModel, modelId);
		this.aiModel = modelId;
	}

	/**
	 * 获取当前模型
	 * @return 模型名称
	 */
	public String getCurrentModel() {
		return this.aiModel;
	}

	/**
	 * 获取可用模型列表
	 * @return 模型格式：displayName|modelId|description
	 */
	public List<String> getAvailableModels() {
		return List.of("DeepSeek Chat|deepseek-chat|DeepSeek 对话模型",
				"Qwen2.5-72B|Qwen/Qwen2.5-72B-Instruct|Qwen2.5 72B 指令模型",
				"Llama-3.1-70B|meta-llama/Meta-Llama-3.1-70B-Instruct|Llama 3.1 70B 指令模型");
	}

	private String buildEnhancedPrompt(String userMessage, String surveyType) {
		return String.format("%s\n\n用户需求：设计一份%s\n具体要求：%s\n\n请按照指定的JSON格式返回问卷内容。", SURVEY_SYSTEM_PROMPT, surveyType,
				userMessage);
	}

	private String callAiApi(String prompt) {
		Map<String, Object> request = new HashMap<>();
		request.put("model", aiModel);

		List<Map<String, String>> messages = List.of(Map.of("role", "user", "content", prompt));
		request.put("messages", messages);
		request.put("max_tokens", maxTokens);
		request.put("temperature", temperature);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(aiApiKey);

		HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

		try {
			log.debug("调用AI API: url={}, model={}", aiApiUrl, aiModel);

			Map<String, Object> response = restTemplate.postForObject(aiApiUrl, entity, Map.class);

			if (response != null && response.containsKey("choices")) {
				List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
				if (!choices.isEmpty()) {
					Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
					String content = (String) message.get("content");
					log.debug("AI响应长度: {} 字符", content != null ? content.length() : 0);
					return content;
				}
			}

			throw new RuntimeException("AI返回格式异常: " + response);

		}
		catch (Exception e) {
			log.error("调用AI API失败: url={}, model={}, error={}", aiApiUrl, aiModel, e.getMessage());
			throw new RuntimeException("AI API调用失败: " + e.getMessage());
		}
	}

	private SurveySchema parseAiResponse(String aiResponse) {
		try {
			// 尝试从响应中提取JSON
			String jsonContent = extractJsonFromResponse(aiResponse);
			log.debug("提取的JSON长度: {} 字符", jsonContent.length());
			return objectMapper.readValue(jsonContent, SurveySchema.class);
		}
		catch (Exception e) {
			log.warn("解析AI响应失败，返回空Schema: {}", e.getMessage());
			return new SurveySchema();
		}
	}

	private String extractJsonFromResponse(String response) {
		// 查找JSON代码块
		int jsonStart = response.indexOf("```json");
		if (jsonStart >= 0) {
			jsonStart = response.indexOf('\n', jsonStart);
			if (jsonStart >= 0) {
				int jsonEnd = response.indexOf("```", jsonStart);
				if (jsonEnd >= 0) {
					return response.substring(jsonStart, jsonEnd).trim();
				}
			}
		}

		// 尝试直接查找JSON对象
		jsonStart = response.indexOf('{');
		if (jsonStart >= 0) {
			int jsonEnd = response.lastIndexOf('}');
			if (jsonEnd >= 0) {
				return response.substring(jsonStart, jsonEnd + 1).trim();
			}
		}

		return response;
	}

	private String readFileContent(MultipartFile file) throws Exception {
		StringBuilder content = new StringBuilder();
		try (BufferedReader reader = new BufferedReader(
				new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
			String line;
			while ((line = reader.readLine()) != null) {
				content.append(line).append("\n");
			}
		}
		return content.toString();
	}

}
