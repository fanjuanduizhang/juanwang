package cn.surveyking.server.domain.mapper;

import cn.surveyking.server.domain.dto.RoleRequest;
import cn.surveyking.server.domain.dto.RoleView;
import cn.surveyking.server.domain.model.Role;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor"
)
@Component
public class RoleViewMapperImpl implements RoleViewMapper {

    @Override
    public Role fromRequest(RoleRequest arg0) {
        if ( arg0 == null ) {
            return null;
        }

        Role role = new Role();

        role.setId( arg0.getId() );
        role.setCode( arg0.getCode() );
        role.setName( arg0.getName() );
        role.setRemark( arg0.getRemark() );
        role.setStatus( arg0.getStatus() );

        afterMappingRole( arg0, role );

        return role;
    }

    @Override
    public List<Role> fromRequest(List<RoleRequest> arg0) {
        if ( arg0 == null ) {
            return null;
        }

        List<Role> list = new ArrayList<Role>( arg0.size() );
        for ( RoleRequest roleRequest : arg0 ) {
            list.add( fromRequest( roleRequest ) );
        }

        return list;
    }

    @Override
    public RoleView toView(Role arg0) {
        if ( arg0 == null ) {
            return null;
        }

        RoleView roleView = new RoleView();

        roleView.setId( arg0.getId() );
        roleView.setName( arg0.getName() );
        roleView.setCode( arg0.getCode() );
        roleView.setRemark( arg0.getRemark() );
        roleView.setCreateAt( arg0.getCreateAt() );
        roleView.setStatus( arg0.getStatus() );

        afterMapping( arg0, roleView );

        return roleView;
    }

    @Override
    public List<RoleView> toView(List<Role> arg0) {
        if ( arg0 == null ) {
            return null;
        }

        List<RoleView> list = new ArrayList<RoleView>( arg0.size() );
        for ( Role role : arg0 ) {
            list.add( toView( role ) );
        }

        return list;
    }
}
