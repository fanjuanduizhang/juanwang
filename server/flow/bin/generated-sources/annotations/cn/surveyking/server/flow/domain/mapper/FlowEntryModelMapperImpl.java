package cn.surveyking.server.flow.domain.mapper;

import cn.surveyking.server.flow.domain.dto.FlowEntryNodeView;
import cn.surveyking.server.flow.domain.dto.FlowEntryView;
import cn.surveyking.server.flow.domain.model.FlowEntry;
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
public class FlowEntryModelMapperImpl implements FlowEntryModelMapper {

    @Override
    public FlowEntry fromRequest(Void arg0) {
        if ( arg0 == null ) {
            return null;
        }

        FlowEntry flowEntry = new FlowEntry();

        return flowEntry;
    }

    @Override
    public List<FlowEntry> fromRequest(List<Void> arg0) {
        if ( arg0 == null ) {
            return null;
        }

        List<FlowEntry> list = new ArrayList<FlowEntry>( arg0.size() );
        for ( Void void1 : arg0 ) {
            list.add( fromRequest( void1 ) );
        }

        return list;
    }

    @Override
    public FlowEntryView toView(FlowEntry arg0) {
        if ( arg0 == null ) {
            return null;
        }

        FlowEntryView flowEntryView = new FlowEntryView();

        flowEntryView.setBpmnXml( arg0.getBpmnXml() );
        flowEntryView.setIcon( arg0.getIcon() );
        flowEntryView.setId( arg0.getId() );
        flowEntryView.setNodes( flowEntryNodeListToFlowEntryNodeViewList( arg0.getNodes() ) );
        flowEntryView.setProjectId( arg0.getProjectId() );

        return flowEntryView;
    }

    @Override
    public List<FlowEntryView> toView(List<FlowEntry> arg0) {
        if ( arg0 == null ) {
            return null;
        }

        List<FlowEntryView> list = new ArrayList<FlowEntryView>( arg0.size() );
        for ( FlowEntry flowEntry : arg0 ) {
            list.add( toView( flowEntry ) );
        }

        return list;
    }

    protected FlowEntryNodeView flowEntryNodeToFlowEntryNodeView(FlowEntryNode flowEntryNode) {
        if ( flowEntryNode == null ) {
            return null;
        }

        FlowEntryNodeView flowEntryNodeView = new FlowEntryNodeView();

        flowEntryNodeView.setExpression( flowEntryNode.getExpression() );
        LinkedHashMap<String, Integer> linkedHashMap = flowEntryNode.getFieldPermission();
        if ( linkedHashMap != null ) {
            flowEntryNodeView.setFieldPermission( new LinkedHashMap<String, Integer>( linkedHashMap ) );
        }
        flowEntryNodeView.setId( flowEntryNode.getId() );
        String[] identity = flowEntryNode.getIdentity();
        if ( identity != null ) {
            flowEntryNodeView.setIdentity( Arrays.copyOf( identity, identity.length ) );
        }
        flowEntryNodeView.setName( flowEntryNode.getName() );
        flowEntryNodeView.setProjectId( flowEntryNode.getProjectId() );
        flowEntryNodeView.setSetting( flowEntryNode.getSetting() );

        return flowEntryNodeView;
    }

    protected List<FlowEntryNodeView> flowEntryNodeListToFlowEntryNodeViewList(List<FlowEntryNode> list) {
        if ( list == null ) {
            return null;
        }

        List<FlowEntryNodeView> list1 = new ArrayList<FlowEntryNodeView>( list.size() );
        for ( FlowEntryNode flowEntryNode : list ) {
            list1.add( flowEntryNodeToFlowEntryNodeView( flowEntryNode ) );
        }

        return list1;
    }
}
