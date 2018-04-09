package src.dao;

import com.mongodb.DBCursor;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import org.bson.types.ObjectId;

import java.io.*;

public class GridFSRepository {
    private final String MONGO_HOST_NAME = System.getenv("MONGO_HOST_NAME");
    private final int MONGO_PORT = Integer.parseInt(System.getenv("MONGO_PORT"));
    private final String MONGO_DB_NAME = System.getenv("MONGO_DB_NAME");
    private final String MONGO_BUCKET_NAME = System.getenv("MONGO_BUCKET_NAME");

    private GridFS gridFS = GridFsFuctory.create(MONGO_HOST_NAME,MONGO_PORT,MONGO_DB_NAME,MONGO_BUCKET_NAME);

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
