package cn.surveyking.server.flow.domain.mapper;

import cn.surveyking.server.flow.domain.dto.FlowOperationView;
import cn.surveyking.server.flow.domain.model.FlowOperation;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor"
)
@Component
public class FlowOperationModelMapperImpl implements FlowOperationModelMapper {

    @Override
    public FlowOperation fromRequest(Void arg0) {
        if ( arg0 == null ) {
            return null;
        }

        FlowOperation flowOperation = new FlowOperation();

        return flowOperation;
    }

    @Override
    public List<FlowOperation> fromRequest(List<Void> arg0) {
        if ( arg0 == null ) {
            return null;
        }

        List<FlowOperation> list = new ArrayList<FlowOperation>( arg0.size() );
        for ( Void void1 : arg0 ) {
            list.add( fromRequest( void1 ) );
        }

        return list;
    }

    @Override
    public FlowOperationView toView(FlowOperation arg0) {
        if ( arg0 == null ) {
            return null;
        }

        FlowOperationView flowOperationView = new FlowOperationView();

        flowOperationView.setActivityId( arg0.getActivityId() );
        flowOperationView.setApprovalType( arg0.getApprovalType() );
        flowOperationView.setComment( arg0.getComment() );
        flowOperationView.setCreateAt( arg0.getCreateAt() );
        flowOperationView.setCreateBy( arg0.getCreateBy() );
        flowOperationView.setId( arg0.getId() );
        flowOperationView.setNewActivityId( arg0.getNewActivityId() );

        return flowOperationView;
    }

    @Override
    public List<FlowOperationView> toView(List<FlowOperation> arg0) {
        if ( arg0 == null ) {
            return null;
        }

        List<FlowOperationView> list = new ArrayList<FlowOperationView>( arg0.size() );
        for ( FlowOperation flowOperation : arg0 ) {
            list.add( toView( flowOperation ) );
        }

        return list;
    }
}
