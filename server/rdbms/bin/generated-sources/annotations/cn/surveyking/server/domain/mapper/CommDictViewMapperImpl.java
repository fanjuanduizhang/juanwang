package cn.surveyking.server.domain.mapper;

import cn.surveyking.server.domain.dto.CommDictRequest;
import cn.surveyking.server.domain.dto.CommDictView;
import cn.surveyking.server.domain.model.CommDict;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor"
)
@Component
public class CommDictViewMapperImpl implements CommDictViewMapper {

    @Override
    public CommDict fromRequest(CommDictRequest arg0) {
        if ( arg0 == null ) {
            return null;
        }

        CommDict commDict = new CommDict();

        commDict.setId( arg0.getId() );
        commDict.setCode( arg0.getCode() );
        commDict.setDictType( arg0.getDictType() );
        commDict.setName( arg0.getName() );
        commDict.setRemark( arg0.getRemark() );

        return commDict;
    }

    @Override
    public List<CommDict> fromRequest(List<CommDictRequest> arg0) {
        if ( arg0 == null ) {
            return null;
        }

        List<CommDict> list = new ArrayList<CommDict>( arg0.size() );
        for ( CommDictRequest commDictRequest : arg0 ) {
            list.add( fromRequest( commDictRequest ) );
        }

        return list;
    }

    @Override
    public CommDictView toView(CommDict arg0) {
        if ( arg0 == null ) {
            return null;
        }

        CommDictView commDictView = new CommDictView();

        commDictView.setId( arg0.getId() );
        commDictView.setCode( arg0.getCode() );
        commDictView.setName( arg0.getName() );
        commDictView.setRemark( arg0.getRemark() );
        commDictView.setDictType( arg0.getDictType() );
        commDictView.setCreateAt( arg0.getCreateAt() );

        return commDictView;
    }

    @Override
    public List<CommDictView> toView(List<CommDict> arg0) {
        if ( arg0 == null ) {
            return null;
        }

        List<CommDictView> list = new ArrayList<CommDictView>( arg0.size() );
        for ( CommDict commDict : arg0 ) {
            list.add( toView( commDict ) );
        }

        return list;
    }
}
