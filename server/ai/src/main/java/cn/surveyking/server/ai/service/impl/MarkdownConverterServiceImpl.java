package cn.surveyking.server.ai.service.impl;

import cn.surveyking.server.ai.service.MarkdownConverterService;
import cn.surveyking.server.domain.dto.SurveySchema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Slf4j
public class MarkdownConverterServiceImpl implements MarkdownConverterService {

	@Override
	public String toMarkdown(SurveySchema surveySchema) {
		if (surveySchema == null) {
			return "";
		}

		StringBuilder markdown = new StringBuilder();

		if (StringUtils.hasText(surveySchema.getTitle())) {
			markdown.append("# ").append(surveySchema.getTitle()).append(" 【问卷】\n\n");
		}

		if (StringUtils.hasText(surveySchema.getDescription())) {
			markdown.append("> ").append(surveySchema.getDescription()).append("\n\n");
		}

		if (!CollectionUtils.isEmpty(surveySchema.getChildren())) {
			for (int i = 0; i < surveySchema.getChildren().size(); i++) {
				SurveySchema question = surveySchema.getChildren().get(i);
				markdown.append(generateQuestionMarkdown(question));
				if (i < surveySchema.getChildren().size() - 1) {
					markdown.append("\n");
				}
			}
		}

		return markdown.toString();
	}

	@Override
	public SurveySchema fromMarkdown(String markdown) {
		return null;
	}

	@Override
	public String generateFullMarkdown(SurveySchema surveySchema, boolean includeAnswers) {
		if (surveySchema == null) {
			return "";
		}

		StringBuilder markdown = new StringBuilder();

		String surveyType = "问卷";
		if (StringUtils.hasText(surveySchema.getTitle())) {
			markdown.append("# ").append(surveySchema.getTitle()).append(" 【").append(surveyType).append("】\n\n");
		}

		if (StringUtils.hasText(surveySchema.getDescription())) {
			markdown.append("**【欢迎语】**\n\n").append(surveySchema.getDescription()).append("\n\n");
		}

		if (!CollectionUtils.isEmpty(surveySchema.getChildren())) {
			for (int i = 0; i < surveySchema.getChildren().size(); i++) {
				SurveySchema question = surveySchema.getChildren().get(i);
				markdown.append("## ").append(i + 1).append(". ").append(question.getTitle()).append(" 【")
						.append(getQuestionTypeLabel(question)).append("】\n\n");

				if (!CollectionUtils.isEmpty(question.getChildren())) {
					for (SurveySchema option : question.getChildren()) {
						markdown.append("- ").append(option.getTitle()).append("\n");
					}
					markdown.append("\n");
				}
			}
		}

		return markdown.toString();
	}

	@Override
	public String generateQuestionMarkdown(SurveySchema question) {
		if (question == null) {
			return "";
		}

		StringBuilder markdown = new StringBuilder();

		markdown.append("## ").append(question.getTitle());
		String typeLabel = getQuestionTypeLabel(question);
		markdown.append(" 【").append(typeLabel).append("】\n\n");

		if (!CollectionUtils.isEmpty(question.getChildren())) {
			for (SurveySchema option : question.getChildren()) {
				markdown.append("- ").append(option.getTitle()).append("\n");
			}
			markdown.append("\n");
		}

		return markdown.toString();
	}

	@Override
	public boolean validateMarkdown(String markdown) {
		if (!StringUtils.hasText(markdown)) {
			return false;
		}
		return markdown.contains("#") && (markdown.contains("【") || markdown.contains("-"));
	}

	/**
	 * 获取题目类型的中文标签（适配 SurveySchema.QuestionType 枚举）
	 */
	private String getQuestionTypeLabel(SurveySchema question) {
		if (question == null || question.getType() == null) {
			return "文字描述";
		}

		return switch (question.getType()) {
		case Radio -> "单选题";
		case Checkbox -> "多选题";
		case FillBlank, Textarea, MultipleBlank -> "填空题";
		case Judge -> "判断题";
		case Score -> "评分题";
		case Upload -> "上传题";
		case Select -> "下拉题";
		case Nps -> "NPS题";
		case Signature -> "签名题";
		case Cascader -> "级联题";
		case MatrixRadio -> "矩阵单选";
		case MatrixCheckbox -> "矩阵多选";
		case Survey -> "问卷";
		case QuestionSet -> "题组";
		case Pagination -> "分页";
		case Remark -> "备注";
		case User -> "用户选择";
		case Dept -> "部门选择";
		case Address -> "地址";
		case Barcode -> "条码";
		case RichText -> "富文本";
		default -> "文字描述";
		};
	}

}
