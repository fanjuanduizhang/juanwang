package cn.surveyking.server.domain.mapper;

import cn.surveyking.server.domain.dto.UpdateUserRequest;
import cn.surveyking.server.domain.model.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor"
)
@Component
public class UserEditMapperImpl implements UserEditMapper {

    @Override
    public void update(UpdateUserRequest request, User user) {
        if ( request == null ) {
            return;
        }
    }
}
