package cn.surveyking.server.flow.domain.mapper;

import cn.surveyking.server.flow.domain.dto.FlowEntryNodeRequest;
import cn.surveyking.server.flow.domain.dto.FlowEntryNodeView;
import cn.surveyking.server.flow.domain.model.FlowEntryNode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor"
)
@Component
public class FlowEntryElementModelMapperImpl implements FlowEntryElementModelMapper {

    @Override
    public FlowEntryNode fromRequest(FlowEntryNodeRequest arg0) {
        if ( arg0 == null ) {
            return null;
        }

        FlowEntryNode flowEntryNode = new FlowEntryNode();

        flowEntryNode.setExpression( arg0.getExpression() );
        LinkedHashMap<String, Integer> linkedHashMap = arg0.getFieldPermission();
        if ( linkedHashMap != null ) {
            flowEntryNode.setFieldPermission( new LinkedHashMap<String, Integer>( linkedHashMap ) );
        }
        flowEntryNode.setId( arg0.getId() );
        String[] identity = arg0.getIdentity();
        if ( identity != null ) {
            flowEntryNode.setIdentity( Arrays.copyOf( identity, identity.length ) );
        }
        flowEntryNode.setName( arg0.getName() );
        flowEntryNode.setProjectId( arg0.getProjectId() );
        flowEntryNode.setSetting( arg0.getSetting() );
        flowEntryNode.setTaskType( arg0.getTaskType() );

        return flowEntryNode;
    }

    @Override
    public List<FlowEntryNode> fromRequest(List<FlowEntryNodeRequest> arg0) {
        if ( arg0 == null ) {
            return null;
        }

        List<FlowEntryNode> list = new ArrayList<FlowEntryNode>( arg0.size() );
        for ( FlowEntryNodeRequest flowEntryNodeRequest : arg0 ) {
            list.add( fromRequest( flowEntryNodeRequest ) );
        }

        return list;
    }

    @Override
    public FlowEntryNodeView toView(FlowEntryNode arg0) {
        if ( arg0 == null ) {
            return null;
        }

        FlowEntryNodeView flowEntryNodeView = new FlowEntryNodeView();

        flowEntryNodeView.setExpression( arg0.getExpression() );
        LinkedHashMap<String, Integer> linkedHashMap = arg0.getFieldPermission();
        if ( linkedHashMap != null ) {
            flowEntryNodeView.setFieldPermission( new LinkedHashMap<String, Integer>( linkedHashMap ) );
        }
        flowEntryNodeView.setId( arg0.getId() );
        String[] identity = arg0.getIdentity();
        if ( identity != null ) {
            flowEntryNodeView.setIdentity( Arrays.copyOf( identity, identity.length ) );
        }
        flowEntryNodeView.setName( arg0.getName() );
        flowEntryNodeView.setProjectId( arg0.getProjectId() );
        flowEntryNodeView.setSetting( arg0.getSetting() );

        return flowEntryNodeView;
    }

    @Override
    public List<FlowEntryNodeView> toView(List<FlowEntryNode> arg0) {
        if ( arg0 == null ) {
            return null;
        }

        List<FlowEntryNodeView> list = new ArrayList<FlowEntryNodeView>( arg0.size() );
        for ( FlowEntryNode flowEntryNode : arg0 ) {
            list.add( toView( flowEntryNode ) );
        }

        return list;
    }
}
