package src.dao.grid_fs.config;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.gridfs.GridFS;

public class GridFsFuctory {

    public static GridFS create(String host, int port, String dbName,String bucket){

        MongoClient mongoClient = new MongoClient(host,port);

        DB db = mongoClient.getDB(dbName);

        GridFS gridFS = new GridFS(db,bucket);
        return  gridFS;
    }
}
