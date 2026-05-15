package cn.surveyking.server.domain.mapper;

import cn.surveyking.server.domain.dto.DeptRequest;
import cn.surveyking.server.domain.dto.DeptView;
import cn.surveyking.server.domain.model.Dept;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor"
)
@Component
public class DeptDtoMapperImpl implements DeptDtoMapper {

    @Override
    public Dept fromRequest(DeptRequest arg0) {
        if ( arg0 == null ) {
            return null;
        }

        Dept dept = new Dept();

        dept.setId( arg0.getId() );
        dept.setCode( arg0.getCode() );
        dept.setManagerId( arg0.getManagerId() );
        dept.setName( arg0.getName() );
        dept.setParentId( arg0.getParentId() );
        dept.setPropertyJson( arg0.getPropertyJson() );
        dept.setRemark( arg0.getRemark() );
        dept.setShortName( arg0.getShortName() );

        return dept;
    }

    @Override
    public List<Dept> fromRequest(List<DeptRequest> arg0) {
        if ( arg0 == null ) {
            return null;
        }

        List<Dept> list = new ArrayList<Dept>( arg0.size() );
        for ( DeptRequest deptRequest : arg0 ) {
            list.add( fromRequest( deptRequest ) );
        }

        return list;
    }

    @Override
    public DeptView toView(Dept arg0) {
        if ( arg0 == null ) {
            return null;
        }

        DeptView deptView = new DeptView();

        deptView.setId( arg0.getId() );
        deptView.setParentId( arg0.getParentId() );
        deptView.setName( arg0.getName() );
        deptView.setShortName( arg0.getShortName() );
        deptView.setCode( arg0.getCode() );
        deptView.setManagerId( arg0.getManagerId() );
        deptView.setRemark( arg0.getRemark() );
        deptView.setPropertyJson( arg0.getPropertyJson() );

        return deptView;
    }

    @Override
    public List<DeptView> toView(List<Dept> arg0) {
        if ( arg0 == null ) {
            return null;
        }

        List<DeptView> list = new ArrayList<DeptView>( arg0.size() );
        for ( Dept dept : arg0 ) {
            list.add( toView( dept ) );
        }

        return list;
    }
}
