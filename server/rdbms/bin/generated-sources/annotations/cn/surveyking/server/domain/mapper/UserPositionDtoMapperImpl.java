package cn.surveyking.server.domain.mapper;

import cn.surveyking.server.domain.dto.UserPositionRequest;
import cn.surveyking.server.domain.dto.UserPositionView;
import cn.surveyking.server.domain.model.UserPosition;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor"
)
@Component
public class UserPositionDtoMapperImpl implements UserPositionDtoMapper {

    @Override
    public UserPosition fromRequest(UserPositionRequest arg0) {
        if ( arg0 == null ) {
            return null;
        }

        UserPosition userPosition = new UserPosition();

        userPosition.setDeptId( arg0.getDeptId() );
        userPosition.setPositionId( arg0.getPositionId() );

        return userPosition;
    }

    @Override
    public List<UserPosition> fromRequest(List<UserPositionRequest> arg0) {
        if ( arg0 == null ) {
            return null;
        }

        List<UserPosition> list = new ArrayList<UserPosition>( arg0.size() );
        for ( UserPositionRequest userPositionRequest : arg0 ) {
            list.add( fromRequest( userPositionRequest ) );
        }

        return list;
    }

    @Override
    public UserPositionView toView(UserPosition arg0) {
        if ( arg0 == null ) {
            return null;
        }

        UserPositionView userPositionView = new UserPositionView();

        userPositionView.setId( arg0.getId() );
        userPositionView.setUserId( arg0.getUserId() );
        userPositionView.setDeptId( arg0.getDeptId() );
        userPositionView.setPositionId( arg0.getPositionId() );

        return userPositionView;
    }

    @Override
    public List<UserPositionView> toView(List<UserPosition> arg0) {
        if ( arg0 == null ) {
            return null;
        }

        List<UserPositionView> list = new ArrayList<UserPositionView>( arg0.size() );
        for ( UserPosition userPosition : arg0 ) {
            list.add( toView( userPosition ) );
        }

        return list;
    }
}
