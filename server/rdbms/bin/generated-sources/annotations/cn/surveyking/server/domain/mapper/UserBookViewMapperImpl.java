package cn.surveyking.server.domain.mapper;

import cn.surveyking.server.domain.dto.UserBookRequest;
import cn.surveyking.server.domain.dto.UserBookView;
import cn.surveyking.server.domain.model.UserBook;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor"
)
@Component
public class UserBookViewMapperImpl implements UserBookViewMapper {

    @Override
    public UserBook fromRequest(UserBookRequest arg0) {
        if ( arg0 == null ) {
            return null;
        }

        UserBook userBook = new UserBook();

        userBook.setId( arg0.getId() );
        userBook.setIsMarked( arg0.getIsMarked() );
        userBook.setName( arg0.getName() );
        userBook.setNote( arg0.getNote() );
        userBook.setStatus( arg0.getStatus() );
        userBook.setTemplateId( arg0.getTemplateId() );
        userBook.setType( arg0.getType() );
        userBook.setWrongTimes( arg0.getWrongTimes() );

        return userBook;
    }

    @Override
    public List<UserBook> fromRequest(List<UserBookRequest> arg0) {
        if ( arg0 == null ) {
            return null;
        }

        List<UserBook> list = new ArrayList<UserBook>( arg0.size() );
        for ( UserBookRequest userBookRequest : arg0 ) {
            list.add( fromRequest( userBookRequest ) );
        }

        return list;
    }

    @Override
    public UserBookView toView(UserBook arg0) {
        if ( arg0 == null ) {
            return null;
        }

        UserBookView userBookView = new UserBookView();

        userBookView.setId( arg0.getId() );
        userBookView.setTemplateId( arg0.getTemplateId() );
        userBookView.setName( arg0.getName() );
        userBookView.setWrongTimes( arg0.getWrongTimes() );
        userBookView.setNote( arg0.getNote() );
        userBookView.setStatus( arg0.getStatus() );
        userBookView.setType( arg0.getType() );
        userBookView.setCreateAt( arg0.getCreateAt() );
        userBookView.setUpdateAt( arg0.getUpdateAt() );

        return userBookView;
    }

    @Override
    public List<UserBookView> toView(List<UserBook> arg0) {
        if ( arg0 == null ) {
            return null;
        }

        List<UserBookView> list = new ArrayList<UserBookView>( arg0.size() );
        for ( UserBook userBook : arg0 ) {
            list.add( toView( userBook ) );
        }

        return list;
    }
}
