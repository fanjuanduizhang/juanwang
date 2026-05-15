package cn.surveyking.server.core.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import jakarta.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author javahuang
 * @date 2021/8/11
 */
@Configuration
@RequiredArgsConstructor
@RestController
public class WebConfig implements WebMvcConfigurer {

	private final ObjectMapper objectMapper;

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		// 允许 controller 直接返回 string
		converters.add(0, new MappingJackson2HttpMessageConverter(objectMapper));
	}

	// 匹配类型的静态资源都会被 ResourceHandler 来处理
	public static final String[] STATIC_RESOURCES = { "/**/*.css", "/**/*.js", "/**/*.jpg", "/**/*.png", "/**/*.svg", // 图片
			"/**/*.eot", "/**/*.ttf", "/**/*.woff", "/**/favicon.ico" };

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.setOrder(-1) // 设置静态资源映射优先级高于下面配置的 @GetMapping
				.addResourceHandler(STATIC_RESOURCES).addResourceLocations("classpath:/static/")
				.setCachePeriod(3600 * 24);
	}

	@Value("classpath:/static/index.html")
	private Resource indexHtml;

	/**
	 * 首页 - 直接输出 index.html 内容（解决 JAR 包中 Resource.getFile() 失效问题）
	 */
	@GetMapping
	public void index(HttpServletResponse response) throws Exception {
		response.setContentType("text/html;charset=UTF-8");
		response.setStatus(HttpStatus.OK.value());
		StreamUtils.copy(indexHtml.getInputStream(), response.getOutputStream());
	}

	/**
	 * 健康检查端点 - 供 Railway/容器编排使用
	 */
	@GetMapping("/healthz")
	public ResponseEntity<String> healthz() {
		return new ResponseEntity<>("OK", HttpStatus.OK);
	}

}
