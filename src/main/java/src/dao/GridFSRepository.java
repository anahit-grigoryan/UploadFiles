package src.dao;

import com.mongodb.DBCursor;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import org.bson.types.ObjectId;

import java.io.*;

public class GridFSRepository {

    private GridFS gridFS = GridFsFuctory.create("localhost",27017,"myFiles","fs");

    public InputStream getFileInputStream(ObjectId objectId) {

        GridFSDBFile dbFile = this.gridFS.findOne(objectId);

        return dbFile.getInputStream();
    }

    public DBCursor getItearator() throws IOException {

        DBCursor iterator = this.gridFS.getFileList();

        return iterator;
    }

    public void addFile(File file, String fileName) throws IOException {
        GridFSInputFile test = this.gridFS.createFile(file);
        test.setFilename(fileName);
        test.save();
    }
}
