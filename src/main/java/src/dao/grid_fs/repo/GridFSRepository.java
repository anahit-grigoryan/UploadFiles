package src.dao.grid_fs.repo;

import com.mongodb.DBCursor;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;

public class GridFSRepository {

    @Autowired
    private GridFS gridFS;

    public InputStream getFileInputStream(ObjectId objectId) {

        GridFSDBFile dbFile = this.gridFS.findOne(objectId);

        return dbFile.getInputStream();
    }

    public DBCursor getItearator() throws IOException {

        DBCursor iterator = this.gridFS.getFileList();

        return iterator;
    }

    public void addFile(InputStream inputStream, String fileName) {

        GridFSInputFile test = this.gridFS.createFile(inputStream, fileName);

        test.save();
    }
}
