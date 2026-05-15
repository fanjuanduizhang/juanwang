package cn.surveyking.server.domain.mapper;

import cn.surveyking.server.domain.dto.ProjectPartnerRequest;
import cn.surveyking.server.domain.dto.ProjectPartnerView;
import cn.surveyking.server.domain.model.ProjectPartner;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor"
)
@Component
public class ProjectPartnerViewMapperImpl implements ProjectPartnerViewMapper {

    @Override
    public ProjectPartner fromRequest(ProjectPartnerRequest arg0) {
        if ( arg0 == null ) {
            return null;
        }

        ProjectPartner projectPartner = new ProjectPartner();

        projectPartner.setProjectId( arg0.getProjectId() );
        projectPartner.setType( arg0.getType() );

        return projectPartner;
    }

    @Override
    public List<ProjectPartner> fromRequest(List<ProjectPartnerRequest> arg0) {
        if ( arg0 == null ) {
            return null;
        }

        List<ProjectPartner> list = new ArrayList<ProjectPartner>( arg0.size() );
        for ( ProjectPartnerRequest projectPartnerRequest : arg0 ) {
            list.add( fromRequest( projectPartnerRequest ) );
        }

        return list;
    }

    @Override
    public ProjectPartnerView toView(ProjectPartner arg0) {
        if ( arg0 == null ) {
            return null;
        }

        ProjectPartnerView projectPartnerView = new ProjectPartnerView();

        projectPartnerView.setId( arg0.getId() );
        projectPartnerView.setType( arg0.getType() );
        projectPartnerView.setUserName( arg0.getUserName() );
        projectPartnerView.setStatus( arg0.getStatus() );
        projectPartnerView.setUserId( arg0.getUserId() );

        return projectPartnerView;
    }

    @Override
    public List<ProjectPartnerView> toView(List<ProjectPartner> arg0) {
        if ( arg0 == null ) {
            return null;
        }

        List<ProjectPartnerView> list = new ArrayList<ProjectPartnerView>( arg0.size() );
        for ( ProjectPartner projectPartner : arg0 ) {
            list.add( toView( projectPartner ) );
        }

        return list;
    }
}
