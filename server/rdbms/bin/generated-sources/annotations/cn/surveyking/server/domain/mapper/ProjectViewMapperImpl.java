package cn.surveyking.server.domain.mapper;

import cn.surveyking.server.domain.dto.ProjectRequest;
import cn.surveyking.server.domain.dto.ProjectSetting;
import cn.surveyking.server.domain.dto.ProjectSetting.AnswerSetting;
import cn.surveyking.server.domain.dto.ProjectSetting.ProjectSettingBuilder;
import cn.surveyking.server.domain.dto.ProjectView;
import cn.surveyking.server.domain.dto.PublicProjectView;
import cn.surveyking.server.domain.model.Project;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor"
)
@Component
public class ProjectViewMapperImpl implements ProjectViewMapper {

    @Override
    public Project fromRequest(ProjectRequest arg0) {
        if ( arg0 == null ) {
            return null;
        }

        Project project = new Project();

        project.setId( arg0.getId() );
        project.setMode( arg0.getMode() );
        project.setName( arg0.getName() );
        project.setParentId( arg0.getParentId() );
        project.setSetting( arg0.getSetting() );
        project.setStatus( arg0.getStatus() );
        project.setSurvey( arg0.getSurvey() );

        return project;
    }

    @Override
    public List<Project> fromRequest(List<ProjectRequest> arg0) {
        if ( arg0 == null ) {
            return null;
        }

        List<Project> list = new ArrayList<Project>( arg0.size() );
        for ( ProjectRequest projectRequest : arg0 ) {
            list.add( fromRequest( projectRequest ) );
        }

        return list;
    }

    @Override
    public ProjectView toView(Project arg0) {
        if ( arg0 == null ) {
            return null;
        }

        ProjectView projectView = new ProjectView();

        projectView.setId( arg0.getId() );
        projectView.setName( arg0.getName() );
        projectView.setMode( arg0.getMode() );
        projectView.setStatus( arg0.getStatus() );
        projectView.setSurvey( arg0.getSurvey() );
        projectView.setSetting( arg0.getSetting() );
        projectView.setCreateAt( arg0.getCreateAt() );
        projectView.setUpdateAt( arg0.getUpdateAt() );
        projectView.setParentId( arg0.getParentId() );

        return projectView;
    }

    @Override
    public List<ProjectView> toView(List<Project> arg0) {
        if ( arg0 == null ) {
            return null;
        }

        List<ProjectView> list = new ArrayList<ProjectView>( arg0.size() );
        for ( Project project : arg0 ) {
            list.add( toView( project ) );
        }

        return list;
    }

    @Override
    public PublicProjectView toPublicProjectView(ProjectView project) {
        if ( project == null ) {
            return null;
        }

        PublicProjectView publicProjectView = new PublicProjectView();

        publicProjectView.setSetting( projectSettingToProjectSetting( project.getSetting() ) );
        publicProjectView.setId( project.getId() );
        publicProjectView.setSurvey( project.getSurvey() );
        publicProjectView.setStatus( project.getStatus() );
        publicProjectView.setName( project.getName() );
        publicProjectView.setMode( project.getMode() );
        publicProjectView.setCreateAt( project.getCreateAt() );
        publicProjectView.setAnswerId( project.getAnswerId() );
        LinkedHashMap<String, Object> linkedHashMap = project.getTempAnswer();
        if ( linkedHashMap != null ) {
            publicProjectView.setTempAnswer( new LinkedHashMap<String, Object>( linkedHashMap ) );
        }

        calledWithSourceAndTargetType( project, publicProjectView );

        return publicProjectView;
    }

    protected AnswerSetting answerSettingToAnswerSetting(AnswerSetting answerSetting) {
        if ( answerSetting == null ) {
            return null;
        }

        AnswerSetting answerSetting1 = new AnswerSetting();

        answerSetting1.setProgressBar( answerSetting.getProgressBar() );
        answerSetting1.setLoginRequired( answerSetting.getLoginRequired() );
        answerSetting1.setQuestionNumber( answerSetting.getQuestionNumber() );
        answerSetting1.setAutoSave( answerSetting.getAutoSave() );
        LinkedHashMap linkedHashMap = answerSetting.getInitialValues();
        if ( linkedHashMap != null ) {
            answerSetting1.setInitialValues( new LinkedHashMap( linkedHashMap ) );
        }
        answerSetting1.setMaxAnswers( answerSetting.getMaxAnswers() );
        answerSetting1.setEndTime( answerSetting.getEndTime() );
        answerSetting1.setWechatOnly( answerSetting.getWechatOnly() );
        answerSetting1.setOnePageOneQuestion( answerSetting.getOnePageOneQuestion() );
        answerSetting1.setAnswerSheetVisible( answerSetting.getAnswerSheetVisible() );
        answerSetting1.setWhitelistType( answerSetting.getWhitelistType() );
        answerSetting1.setWhitelistLimit( answerSetting.getWhitelistLimit() );
        answerSetting1.setTriggerType( answerSetting.getTriggerType() );
        answerSetting1.setCopyEnabled( answerSetting.getCopyEnabled() );
        answerSetting1.setDefaultLocale( answerSetting.getDefaultLocale() );

        return answerSetting1;
    }

    protected ProjectSetting projectSettingToProjectSetting(ProjectSetting projectSetting) {
        if ( projectSetting == null ) {
            return null;
        }

        ProjectSettingBuilder projectSetting1 = ProjectSetting.builder();

        projectSetting1.status( projectSetting.getStatus() );
        projectSetting1.mode( projectSetting.getMode() );
        projectSetting1.answerSetting( answerSettingToAnswerSetting( projectSetting.getAnswerSetting() ) );
        projectSetting1.submittedSetting( projectSetting.getSubmittedSetting() );
        projectSetting1.examSetting( projectSetting.getExamSetting() );

        return projectSetting1.build();
    }
}
