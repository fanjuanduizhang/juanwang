package cn.surveyking.server.domain.mapper;

import cn.surveyking.server.domain.dto.PositionRequest;
import cn.surveyking.server.domain.dto.PositionView;
import cn.surveyking.server.domain.model.Position;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor"
)
@Component
public class PositionDtoMapperImpl implements PositionDtoMapper {

    @Override
    public Position fromRequest(PositionRequest arg0) {
        if ( arg0 == null ) {
            return null;
        }

        Position position = new Position();

        position.setId( arg0.getId() );
        position.setCode( arg0.getCode() );
        position.setDataPermissionType( arg0.getDataPermissionType() );
        position.setIsVirtual( arg0.getIsVirtual() );
        position.setName( arg0.getName() );

        return position;
    }

    @Override
    public List<Position> fromRequest(List<PositionRequest> arg0) {
        if ( arg0 == null ) {
            return null;
        }

        List<Position> list = new ArrayList<Position>( arg0.size() );
        for ( PositionRequest positionRequest : arg0 ) {
            list.add( fromRequest( positionRequest ) );
        }

        return list;
    }

    @Override
    public PositionView toView(Position arg0) {
        if ( arg0 == null ) {
            return null;
        }

        PositionView positionView = new PositionView();

        positionView.setId( arg0.getId() );
        positionView.setName( arg0.getName() );
        positionView.setCode( arg0.getCode() );
        positionView.setIsVirtual( arg0.getIsVirtual() );
        positionView.setDataPermissionType( arg0.getDataPermissionType() );

        return positionView;
    }

    @Override
    public List<PositionView> toView(List<Position> arg0) {
        if ( arg0 == null ) {
            return null;
        }

        List<PositionView> list = new ArrayList<PositionView>( arg0.size() );
        for ( Position position : arg0 ) {
            list.add( toView( position ) );
        }

        return list;
    }
}
