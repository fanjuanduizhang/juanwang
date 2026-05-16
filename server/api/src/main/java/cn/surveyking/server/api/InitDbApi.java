package cn.surveyking.server.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

	@PostMapping("/public/init-db")
	public ResponseEntity<Map<String, Object>> initDatabase() {
		Map<String, Object> result = new LinkedHashMap<>();
		result.put("message", "SurveyKing DB Init");

		List<String> errors = new ArrayList<>();
		List<String> successes = new ArrayList<>();
		int total = 0;
		int successCount = 0;
		int errorCount = 0;

		try (Connection conn = dataSource.getConnection()) {
			conn.setAutoCommit(true);

			ClassPathResource resource = new ClassPathResource("scripts/init-mysql.sql");
			try (BufferedReader reader = new BufferedReader(
					new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8));
					Statement stmt = conn.createStatement()) {

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
						String rawStatement = sb.toString().trim();
						sb.setLength(0);
						total++;

						// 清理 SQL：修复损坏的字符串字面量
						String statement = sanitizeSql(rawStatement);

						try {
							stmt.execute(statement);
							successCount++;
							if (successes.size() < 3) {
								successes.add(truncateStatement(statement));
							}
						} catch (Exception e) {
							errorCount++;
							String errMsg = e.getMessage();
							if (errMsg != null && errMsg.length() > 500) {
								errMsg = errMsg.substring(0, 500);
							}
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
					String statement = sanitizeSql(sb.toString().trim());
					total++;
					try {
						stmt.execute(statement);
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

	/**
	 * 清理 SQL 语句中的编码损坏。
	 * 
	 * 原始 init-mysql.sql 中部分中文 COMMENT 值存在 UTF-8 截断损坏：
	 * - 完整中文 char 的末尾字节被替换为 '?'
	 * - 且丢失了闭合单引号，导致 MySQL syntax error
	 * 
	 * 例如: COMMENT '加密?,  应为  COMMENT '加密盐',
	 * 修复为: COMMENT '加密',    （截断但语法正确）
	 */
	private String sanitizeSql(String sql) {
		if (sql == null || sql.isEmpty()) {
			return sql;
		}

		StringBuilder result = new StringBuilder(sql.length());
		boolean inQuote = false;
		int length = sql.length();

		for (int i = 0; i < length; i++) {
			char c = sql.charAt(i);

			// 处理单引号（字符串边界）
			if (c == '\'') {
				// 检查转义引号 ''
				if (inQuote && i + 1 < length && sql.charAt(i + 1) == '\'') {
					result.append('\'');
					result.append('\'');
					i++; // 跳过下一个 '
					continue;
				}
				inQuote = !inQuote;
				result.append(c);
				continue;
			}

			// 不在引号内 → 直接保留
			if (!inQuote) {
				result.append(c);
				continue;
			}

			// 在引号内：检测 ? 后跟 , \n 或 end-of-statement（无闭合引号标志）
			if (c == '?' && i + 1 < length) {
				char next = sql.charAt(i + 1);
				// 如果 ? 后面是逗号、换行或分号，说明这是截断损坏
				// 跳过这个 ?，并提前关闭引号
				if (next == ',' || next == '\n' || next == '\r' || next == ';') {
					// 补上闭合引号
					result.append('\'');
					// 不消耗下一个字符（让外层循环正常处理 , \n ;）
					continue;
				}
			}

			// 引号内的普通字符，直接保留
			result.append(c);
		}

		// 如果还在引号内（行尾未闭合），补上闭合引号
		if (inQuote) {
			result.append('\'');
		}

		return result.toString();
	}

	private String truncateStatement(String sql) {
		sql = sql.replaceAll("\\s+", " ").trim();
		if (sql.length() > 80) {
			return sql.substring(0, 80) + "...";
		}
		return sql;
	}
}
