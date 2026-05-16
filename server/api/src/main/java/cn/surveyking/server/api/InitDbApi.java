package cn.surveyking.server.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 数据库初始化接口（Railway 部署专用）
 * POST /api/public/init-db 触发执行
 * 仅在 Railway 等云环境需要时使用，本地开发不需要
 */
@Slf4j
@RestController
@RequestMapping("${api.prefix}")
public class InitDbApi {

	@Autowired
	private DataSource dataSource;

	private static final Pattern STATEMENT_DELIMITER = Pattern.compile(
			";\\s*$", Pattern.MULTILINE);

	@PostMapping("/public/init-db")
	public ResponseEntity<Map<String, Object>> initDatabase() {
		Map<String, Object> result = new LinkedHashMap<>();
		result.put("message", "SurveyKing DB Init");

		List<String> errors = new ArrayList<>();
		List<String> successes = new ArrayList<>();
		int total = 0;
		int successCount = 0;
		int errorCount = 0;

		try {
			JdbcTemplate jdbc = new JdbcTemplate(dataSource);
			
			ClassPathResource resource = new ClassPathResource("scripts/init-mysql.sql");
			try (BufferedReader reader = new BufferedReader(
					new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
				
				StringBuilder sb = new StringBuilder();
				String line;
				while ((line = reader.readLine()) != null) {
					String trimmed = line.trim();
					// 跳过空行和注释
					if (trimmed.isEmpty() || trimmed.startsWith("--")) {
						continue;
					}
					sb.append(line).append("\n");
					
					// 以分号结尾的行 → 执行完整语句
					if (trimmed.endsWith(";")) {
						String statement = sb.toString().trim();
						sb.setLength(0);
						total++;
						
						try {
							jdbc.execute(statement);
							successCount++;
							// 记录前3个成功语句的摘要
							if (successes.size() < 3) {
								successes.add(truncateStatement(statement));
							}
						} catch (Exception e) {
							errorCount++;
							String errMsg = e.getMessage();
							if (errMsg != null && errMsg.length() > 120) {
								errMsg = errMsg.substring(0, 120);
							}
							// 只记录前5个错误
							if (errors.size() < 5) {
								errors.add("[" + total + "] " + truncateStatement(statement) 
										+ " => " + errMsg);
							}
							log.warn("[Init DB] Statement #{} failed: {}", total, errMsg);
						}
					}
				}
				
				// 处理最后一个没有分号的语句（如果有）
				if (sb.length() > 0) {
					String statement = sb.toString().trim();
					total++;
					try {
						jdbc.execute(statement);
						successCount++;
					} catch (Exception e) {
						errorCount++;
						log.warn("[Init DB] Last statement failed: {}", e.getMessage());
					}
				}
			}

			result.put("totalStatements", total);
			result.put("successCount", successCount);
			result.put("errorCount", errorCount);
			result.put("status", errorCount == 0 ? "ALL_OK" : 
					(successCount > 0 ? "PARTIAL" : "FAILED"));
			
			if (!successes.isEmpty()) {
				result.put("sampleSuccesses", successes);
			}
			if (!errors.isEmpty()) {
				result.put("errors", errors);
			}
			
			log.info("[Init DB] Complete: {}/{}/{} (total/success/error)", 
					total, successCount, errorCount);

		} catch (Exception e) {
			result.put("status", "FATAL");
			result.put("error", e.getMessage());
			log.error("[Init DB] Fatal: ", e);
		}

		return ResponseEntity.ok(result);
	}

	private String truncateStatement(String sql) {
		sql = sql.replaceAll("\\s+", " ").trim();
		if (sql.length() > 80) {
			return sql.substring(0, 80) + "...";
		}
		return sql;
	}
}
