package cn.surveyking.server.domain.mapper;

import cn.surveyking.server.domain.dto.RepoPartnerRequest;
import cn.surveyking.server.domain.dto.RepoPartnerView;
import cn.surveyking.server.domain.model.RepoPartner;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor"
)
@Component
public class RepoPartnerViewMapperImpl implements RepoPartnerViewMapper {

    @Override
    public RepoPartner fromRequest(RepoPartnerRequest arg0) {
        if ( arg0 == null ) {
            return null;
        }

        RepoPartner repoPartner = new RepoPartner();

        repoPartner.setRepoId( arg0.getRepoId() );

        return repoPartner;
    }

    @Override
    public List<RepoPartner> fromRequest(List<RepoPartnerRequest> arg0) {
        if ( arg0 == null ) {
            return null;
        }

        List<RepoPartner> list = new ArrayList<RepoPartner>( arg0.size() );
        for ( RepoPartnerRequest repoPartnerRequest : arg0 ) {
            list.add( fromRequest( repoPartnerRequest ) );
        }

        return list;
    }

    @Override
    public RepoPartnerView toView(RepoPartner arg0) {
        if ( arg0 == null ) {
            return null;
        }

        RepoPartnerView repoPartnerView = new RepoPartnerView();

        repoPartnerView.setId( arg0.getId() );
        repoPartnerView.setUserId( arg0.getUserId() );

        return repoPartnerView;
    }

    @Override
    public List<RepoPartnerView> toView(List<RepoPartner> arg0) {
        if ( arg0 == null ) {
            return null;
        }

        List<RepoPartnerView> list = new ArrayList<RepoPartnerView>( arg0.size() );
        for ( RepoPartner repoPartner : arg0 ) {
            list.add( toView( repoPartner ) );
        }

        return list;
    }
}
