package cn.surveyking.server.domain.mapper;

import cn.surveyking.server.domain.dto.FileView;
import cn.surveyking.server.domain.model.File;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor"
)
@Component
public class FileViewMapperImpl implements FileViewMapper {

    @Override
    public File fromRequest(Void arg0) {
        if ( arg0 == null ) {
            return null;
        }

        File file = new File();

        return file;
    }

    @Override
    public List<File> fromRequest(List<Void> arg0) {
        if ( arg0 == null ) {
            return null;
        }

        List<File> list = new ArrayList<File>( arg0.size() );
        for ( Void void1 : arg0 ) {
            list.add( fromRequest( void1 ) );
        }

        return list;
    }

    @Override
    public FileView toView(File arg0) {
        if ( arg0 == null ) {
            return null;
        }

        FileView fileView = new FileView();

        fileView.setId( arg0.getId() );
        fileView.setOriginalName( arg0.getOriginalName() );
        fileView.setFilePath( arg0.getFilePath() );

        return fileView;
    }

    @Override
    public List<FileView> toView(List<File> arg0) {
        if ( arg0 == null ) {
            return null;
        }

        List<FileView> list = new ArrayList<FileView>( arg0.size() );
        for ( File file : arg0 ) {
            list.add( toView( file ) );
        }

        return list;
    }
}
