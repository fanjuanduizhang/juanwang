package cn.surveyking.server.domain.mapper;

import cn.surveyking.server.domain.dto.AnswerRequest;
import cn.surveyking.server.domain.dto.AnswerView;
import cn.surveyking.server.domain.model.Answer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor"
)
@Component
public class AnswerViewMapperImpl implements AnswerViewMapper {

    @Override
    public Answer fromRequest(AnswerRequest arg0) {
        if ( arg0 == null ) {
            return null;
        }

        Answer answer = new Answer();

        answer.setUpdateBy( arg0.getUpdateBy() );
        LinkedHashMap<String, Object> linkedHashMap = arg0.getAnswer();
        if ( linkedHashMap != null ) {
            answer.setAnswer( new LinkedHashMap( linkedHashMap ) );
        }
        answer.setCreateBy( arg0.getCreateBy() );
        answer.setExamInfo( arg0.getExamInfo() );
        answer.setId( arg0.getId() );
        answer.setMetaInfo( arg0.getMetaInfo() );
        answer.setProjectId( arg0.getProjectId() );
        answer.setSurvey( arg0.getSurvey() );
        LinkedHashMap<String, Object> linkedHashMap1 = arg0.getTempAnswer();
        if ( linkedHashMap1 != null ) {
            answer.setTempAnswer( new LinkedHashMap( linkedHashMap1 ) );
        }
        answer.setTempSave( arg0.getTempSave() );

        return answer;
    }

    @Override
    public List<Answer> fromRequest(List<AnswerRequest> arg0) {
        if ( arg0 == null ) {
            return null;
        }

        List<Answer> list = new ArrayList<Answer>( arg0.size() );
        for ( AnswerRequest answerRequest : arg0 ) {
            list.add( fromRequest( answerRequest ) );
        }

        return list;
    }

    @Override
    public AnswerView toView(Answer arg0) {
        if ( arg0 == null ) {
            return null;
        }

        AnswerView answerView = new AnswerView();

        answerView.setId( arg0.getId() );
        answerView.setProjectId( arg0.getProjectId() );
        LinkedHashMap linkedHashMap = arg0.getAnswer();
        if ( linkedHashMap != null ) {
            answerView.setAnswer( new LinkedHashMap<String, Object>( linkedHashMap ) );
        }
        LinkedHashMap linkedHashMap1 = arg0.getTempAnswer();
        if ( linkedHashMap1 != null ) {
            answerView.setTempAnswer( new LinkedHashMap<String, Object>( linkedHashMap1 ) );
        }
        answerView.setSurvey( arg0.getSurvey() );
        answerView.setMetaInfo( arg0.getMetaInfo() );
        answerView.setExamScore( arg0.getExamScore() );
        answerView.setExamInfo( arg0.getExamInfo() );
        answerView.setTempSave( arg0.getTempSave() );
        answerView.setCreateAt( arg0.getCreateAt() );
        answerView.setCreateBy( arg0.getCreateBy() );
        answerView.setUpdateAt( arg0.getUpdateAt() );
        answerView.setUpdateBy( arg0.getUpdateBy() );

        return answerView;
    }

    @Override
    public List<AnswerView> toView(List<Answer> arg0) {
        if ( arg0 == null ) {
            return null;
        }

        List<AnswerView> list = new ArrayList<AnswerView>( arg0.size() );
        for ( Answer answer : arg0 ) {
            list.add( toView( answer ) );
        }

        return list;
    }
}
