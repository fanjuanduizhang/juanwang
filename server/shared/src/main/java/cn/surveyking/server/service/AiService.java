package cn.surveyking.server.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * [Smart Learning 修改] 定义 AI 识别接口，用于多模型扩展
 */
public interface AiService {

	String convertToMarkdown(MultipartFile file) throws Exception;

}
