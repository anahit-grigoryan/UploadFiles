package src.service;

import com.google.api.services.storage.model.StorageObject;
import com.mongodb.DBObject;
import org.bson.types.ObjectId;
import src.dao.CloudStorageRepository;
import src.dao.GridFSRepository;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FromMongoToCloud {


    private String BUCKET_NAME = System.getenv("GOOGLE_BUCKET_NAME");//"my-first-bucket-11";

    private GridFSRepository gridFSRepo = new GridFSRepository();

    private CloudStorageRepository cloudStorage = new CloudStorageRepository();

    private static final Logger logger = Logger.getLogger(CloudMigration.class.getName());

    public StorageObject moveImage(DBObject dbObject) throws IOException {

        logger.log(Level.INFO,"GET IMAGE FROM MONGO. ID: " + dbObject.get("_id") + dbObject.get("filename"));

        String contentType = dbObject.get("contentType") != null ? (String) dbObject.get("contentType") : "image/jpg";

        InputStream inputStream = gridFSRepo.getFileInputStream((ObjectId) dbObject.get("_id"));

        logger.log(Level.INFO,"SAVE IMAGE TO CLOUD STORAGE. IMAGE ID: " + dbObject.get("_id") +dbObject.get("filename"));

        StorageObject storageObject = cloudStorage.uploadFile(BUCKET_NAME,inputStream, (String) dbObject.get("filename"),contentType);

        return storageObject;

    }
}
