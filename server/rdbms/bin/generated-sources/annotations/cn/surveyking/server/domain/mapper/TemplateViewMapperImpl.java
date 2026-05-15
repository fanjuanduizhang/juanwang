package cn.surveyking.server.domain.mapper;

import cn.surveyking.server.domain.dto.TemplateRequest;
import cn.surveyking.server.domain.dto.TemplateView;
import cn.surveyking.server.domain.model.Template;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor"
)
@Component
public class TemplateViewMapperImpl implements TemplateViewMapper {

    @Override
    public List<Template> fromRequest(List<TemplateRequest> arg0) {
        if ( arg0 == null ) {
            return null;
        }

        List<Template> list = new ArrayList<Template>( arg0.size() );
        for ( TemplateRequest templateRequest : arg0 ) {
            list.add( fromRequest( templateRequest ) );
        }

        return list;
    }

    @Override
    public List<TemplateView> toView(List<Template> arg0) {
        if ( arg0 == null ) {
            return null;
        }

        List<TemplateView> list = new ArrayList<TemplateView>( arg0.size() );
        for ( Template template : arg0 ) {
            list.add( toView( template ) );
        }

        return list;
    }

    @Override
    public Template fromRequest(TemplateRequest request) {
        if ( request == null ) {
            return null;
        }

        Template template = new Template();

        template.setId( request.getId() );
        template.setCategory( request.getCategory() );
        template.setMode( request.getMode() );
        template.setName( request.getName() );
        template.setPreviewUrl( request.getPreviewUrl() );
        template.setPriority( request.getPriority() );
        template.setQuestionType( request.getQuestionType() );
        template.setRepoId( request.getRepoId() );
        template.setSerialNo( request.getSerialNo() );
        template.setShared( request.getShared() );
        String[] tag = request.getTag();
        if ( tag != null ) {
            template.setTag( Arrays.copyOf( tag, tag.length ) );
        }
        template.setTemplate( request.getTemplate() );

        return template;
    }

    @Override
    public TemplateView toView(Template item) {
        if ( item == null ) {
            return null;
        }

        TemplateView templateView = new TemplateView();

        templateView.setOwner( itemToOwner( item ) );
        templateView.setId( item.getId() );
        templateView.setName( item.getName() );
        templateView.setQuestionType( item.getQuestionType() );
        templateView.setTemplate( item.getTemplate() );
        templateView.setMode( item.getMode() );
        String[] tag = item.getTag();
        if ( tag != null ) {
            templateView.setTag( Arrays.copyOf( tag, tag.length ) );
        }
        templateView.setCategory( item.getCategory() );
        templateView.setPreviewUrl( item.getPreviewUrl() );
        templateView.setCreateAt( item.getCreateAt() );
        templateView.setRepoId( item.getRepoId() );

        return templateView;
    }
}
