package cn.surveyking.server.domain.mapper;

import cn.surveyking.server.domain.dto.CommDictItemRequest;
import cn.surveyking.server.domain.dto.CommDictItemView;
import cn.surveyking.server.domain.model.CommDictItem;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor"
)
@Component
public class CommDictItemViewMapperImpl implements CommDictItemViewMapper {

    @Override
    public CommDictItem fromRequest(CommDictItemRequest arg0) {
        if ( arg0 == null ) {
            return null;
        }

        CommDictItem commDictItem = new CommDictItem();

        commDictItem.setId( arg0.getId() );
        commDictItem.setDictCode( arg0.getDictCode() );
        commDictItem.setItemLevel( arg0.getItemLevel() );
        commDictItem.setItemName( arg0.getItemName() );
        commDictItem.setItemOrder( arg0.getItemOrder() );
        commDictItem.setItemValue( arg0.getItemValue() );
        commDictItem.setParentItemValue( arg0.getParentItemValue() );

        return commDictItem;
    }

    @Override
    public List<CommDictItem> fromRequest(List<CommDictItemRequest> arg0) {
        if ( arg0 == null ) {
            return null;
        }

        List<CommDictItem> list = new ArrayList<CommDictItem>( arg0.size() );
        for ( CommDictItemRequest commDictItemRequest : arg0 ) {
            list.add( fromRequest( commDictItemRequest ) );
        }

        return list;
    }

    @Override
    public CommDictItemView toView(CommDictItem arg0) {
        if ( arg0 == null ) {
            return null;
        }

        CommDictItemView commDictItemView = new CommDictItemView();

        commDictItemView.setId( arg0.getId() );
        commDictItemView.setDictCode( arg0.getDictCode() );
        commDictItemView.setItemName( arg0.getItemName() );
        commDictItemView.setItemValue( arg0.getItemValue() );
        commDictItemView.setParentItemValue( arg0.getParentItemValue() );
        commDictItemView.setItemLevel( arg0.getItemLevel() );
        commDictItemView.setItemOrder( arg0.getItemOrder() );
        commDictItemView.setCreateAt( arg0.getCreateAt() );

        return commDictItemView;
    }

    @Override
    public List<CommDictItemView> toView(List<CommDictItem> arg0) {
        if ( arg0 == null ) {
            return null;
        }

        List<CommDictItemView> list = new ArrayList<CommDictItemView>( arg0.size() );
        for ( CommDictItem commDictItem : arg0 ) {
            list.add( toView( commDictItem ) );
        }

        return list;
    }
}
