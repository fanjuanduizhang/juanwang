package cn.surveyking.server.domain.mapper;

import cn.surveyking.server.domain.dto.RepoRequest;
import cn.surveyking.server.domain.dto.RepoView;
import cn.surveyking.server.domain.model.Repo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor"
)
@Component
public class RepoViewMapperImpl implements RepoViewMapper {

    @Override
    public Repo fromRequest(RepoRequest arg0) {
        if ( arg0 == null ) {
            return null;
        }

        Repo repo = new Repo();

        repo.setId( arg0.getId() );
        repo.setCategory( arg0.getCategory() );
        repo.setDescription( arg0.getDescription() );
        repo.setIsPractice( arg0.getIsPractice() );
        repo.setMode( arg0.getMode() );
        repo.setName( arg0.getName() );
        repo.setPriority( arg0.getPriority() );
        repo.setSetting( arg0.getSetting() );
        repo.setShared( arg0.getShared() );
        String[] tag = arg0.getTag();
        if ( tag != null ) {
            repo.setTag( Arrays.copyOf( tag, tag.length ) );
        }

        return repo;
    }

    @Override
    public List<Repo> fromRequest(List<RepoRequest> arg0) {
        if ( arg0 == null ) {
            return null;
        }

        List<Repo> list = new ArrayList<Repo>( arg0.size() );
        for ( RepoRequest repoRequest : arg0 ) {
            list.add( fromRequest( repoRequest ) );
        }

        return list;
    }

    @Override
    public RepoView toView(Repo arg0) {
        if ( arg0 == null ) {
            return null;
        }

        RepoView repoView = new RepoView();

        repoView.setId( arg0.getId() );
        repoView.setName( arg0.getName() );
        repoView.setDescription( arg0.getDescription() );
        repoView.setMode( arg0.getMode() );
        String[] tag = arg0.getTag();
        if ( tag != null ) {
            repoView.setTag( Arrays.copyOf( tag, tag.length ) );
        }
        repoView.setPriority( arg0.getPriority() );
        repoView.setSetting( arg0.getSetting() );
        repoView.setShared( arg0.getShared() );
        repoView.setCategory( arg0.getCategory() );
        repoView.setIsPractice( arg0.getIsPractice() );
        repoView.setCreateBy( arg0.getCreateBy() );

        return repoView;
    }

    @Override
    public List<RepoView> toView(List<Repo> arg0) {
        if ( arg0 == null ) {
            return null;
        }

        List<RepoView> list = new ArrayList<RepoView>( arg0.size() );
        for ( Repo repo : arg0 ) {
            list.add( toView( repo ) );
        }

        return list;
    }
}
