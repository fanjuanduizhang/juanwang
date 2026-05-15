package cn.surveyking.server.service.impl;

import cn.surveyking.server.service.AiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;

@Service
public class AiServiceImpl implements AiService {

	@Value("${ai.api-key:}")
	private String apiKey;

	@Override
	public String convertToMarkdown(MultipartFile file) throws Exception {
		if (apiKey.isEmpty())
			return "请在配置文件中设置 API Key";

		byte[] bytes = file.getBytes();
		String base64Content = Base64.getEncoder().encodeToString(bytes);

		String json = "{\"contents\":[{\"parts\":[{\"text\":\"请解析该题库文件并输出为Markdown格式\"},{\"inlineData\":{\"mimeType\":\""
				+ file.getContentType() + "\",\"data\":\"" + base64Content + "\"}}]}]}";

		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create("模型地址key=" + apiKey))
				.header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(json)).build();

		return client.send(request, HttpResponse.BodyHandlers.ofString()).body();
	}

}
