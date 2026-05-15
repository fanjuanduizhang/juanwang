package cn.surveyking.server.domain.mapper;

import cn.surveyking.server.domain.dto.DashboardRequest;
import cn.surveyking.server.domain.dto.DashboardView;
import cn.surveyking.server.domain.model.Dashboard;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor"
)
@Component
public class DashboardViewMapperImpl implements DashboardViewMapper {

    @Override
    public Dashboard fromRequest(DashboardRequest arg0) {
        if ( arg0 == null ) {
            return null;
        }

        Dashboard dashboard = new Dashboard();

        dashboard.setId( arg0.getId() );
        dashboard.setSetting( arg0.getSetting() );

        return dashboard;
    }

    @Override
    public List<Dashboard> fromRequest(List<DashboardRequest> arg0) {
        if ( arg0 == null ) {
            return null;
        }

        List<Dashboard> list = new ArrayList<Dashboard>( arg0.size() );
        for ( DashboardRequest dashboardRequest : arg0 ) {
            list.add( fromRequest( dashboardRequest ) );
        }

        return list;
    }

    @Override
    public DashboardView toView(Dashboard arg0) {
        if ( arg0 == null ) {
            return null;
        }

        DashboardView dashboardView = new DashboardView();

        dashboardView.setId( arg0.getId() );
        dashboardView.setSetting( arg0.getSetting() );

        return dashboardView;
    }

    @Override
    public List<DashboardView> toView(List<Dashboard> arg0) {
        if ( arg0 == null ) {
            return null;
        }

        List<DashboardView> list = new ArrayList<DashboardView>( arg0.size() );
        for ( Dashboard dashboard : arg0 ) {
            list.add( toView( dashboard ) );
        }

        return list;
    }
}
