package cn.surveyking.server.domain.dto;

import java.util.List;

/**
 * 从其他题库复制题目到目标题库的请求
 */
public class CopyFromReposRequest {

    private String targetRepoId;
    private List<String> sourceRepoIds;
    private List<String> questionTypes;
    private List<String> tags;
    private Integer limitPerRepo;

    public String getTargetRepoId() {
        return targetRepoId;
    }

    public void setTargetRepoId(String targetRepoId) {
        this.targetRepoId = targetRepoId;
    }

    public List<String> getSourceRepoIds() {
        return sourceRepoIds;
    }

    public void setSourceRepoIds(List<String> sourceRepoIds) {
        this.sourceRepoIds = sourceRepoIds;
    }

    public List<String> getQuestionTypes() {
        return questionTypes;
    }

    public void setQuestionTypes(List<String> questionTypes) {
        this.questionTypes = questionTypes;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Integer getLimitPerRepo() {
        return limitPerRepo;
    }

    public void setLimitPerRepo(Integer limitPerRepo) {
        this.limitPerRepo = limitPerRepo;
    }
}
