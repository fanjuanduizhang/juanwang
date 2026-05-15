package cn.surveyking.server.domain.mapper;

import cn.surveyking.server.domain.dto.UserInfo;
import cn.surveyking.server.domain.dto.UserRequest;
import cn.surveyking.server.domain.dto.UserView;
import cn.surveyking.server.domain.model.Account;
import cn.surveyking.server.domain.model.User;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor"
)
@Component
public class UserViewMapperImpl implements UserViewMapper {

    @Override
    public User fromRequest(UserRequest arg0) {
        if ( arg0 == null ) {
            return null;
        }

        User user = new User();

        user.setId( arg0.getId() );
        user.setAvatar( arg0.getAvatar() );
        user.setCorrectTimes( arg0.getCorrectTimes() );
        user.setDeptId( arg0.getDeptId() );
        user.setEmail( arg0.getEmail() );
        user.setGender( arg0.getGender() );
        user.setName( arg0.getName() );
        user.setPhone( arg0.getPhone() );
        user.setProfile( arg0.getProfile() );
        user.setStatus( arg0.getStatus() );

        return user;
    }

    @Override
    public List<User> fromRequest(List<UserRequest> arg0) {
        if ( arg0 == null ) {
            return null;
        }

        List<User> list = new ArrayList<User>( arg0.size() );
        for ( UserRequest userRequest : arg0 ) {
            list.add( fromRequest( userRequest ) );
        }

        return list;
    }

    @Override
    public UserView toView(User arg0) {
        if ( arg0 == null ) {
            return null;
        }

        UserView userView = new UserView();

        userView.setId( arg0.getId() );
        userView.setName( arg0.getName() );
        userView.setPhone( arg0.getPhone() );
        userView.setEmail( arg0.getEmail() );
        userView.setAvatar( arg0.getAvatar() );
        userView.setGender( arg0.getGender() );
        userView.setStatus( arg0.getStatus() );
        userView.setCreateAt( arg0.getCreateAt() );
        userView.setDeptId( arg0.getDeptId() );

        return userView;
    }

    @Override
    public List<UserView> toView(List<User> arg0) {
        if ( arg0 == null ) {
            return null;
        }

        List<UserView> list = new ArrayList<UserView>( arg0.size() );
        for ( User user : arg0 ) {
            list.add( toView( user ) );
        }

        return list;
    }

    @Override
    public UserInfo toUserInfo(User user) {
        if ( user == null ) {
            return null;
        }

        UserInfo userInfo = new UserInfo();

        userInfo.setUserId( user.getId() );
        userInfo.setName( user.getName() );
        userInfo.setAvatar( user.getAvatar() );
        userInfo.setDeptId( user.getDeptId() );
        userInfo.setGender( user.getGender() );
        userInfo.setPhone( user.getPhone() );
        userInfo.setEmail( user.getEmail() );
        userInfo.setProfile( user.getProfile() );
        userInfo.setStatus( user.getStatus() );
        userInfo.setCorrectTimes( user.getCorrectTimes() );

        return userInfo;
    }

    @Override
    public UserInfo toUserView(Account account) {
        if ( account == null ) {
            return null;
        }

        UserInfo userInfo = new UserInfo();

        userInfo.setUsername( account.getAuthAccount() );
        userInfo.setPassword( account.getAuthSecret() );
        userInfo.setUserId( account.getUserId() );
        userInfo.setStatus( account.getStatus() );

        return userInfo;
    }

    @Override
    public Account toAccount(UserRequest request) {
        if ( request == null ) {
            return null;
        }

        Account account = new Account();

        account.setAuthAccount( request.getUsername() );
        account.setUserId( request.getId() );
        account.setId( request.getId() );
        account.setStatus( request.getStatus() );

        return account;
    }
}
