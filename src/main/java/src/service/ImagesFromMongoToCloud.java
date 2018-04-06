package src.service;

import com.google.api.services.storage.model.StorageObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import src.dao.cloud.CloudStorage;
import src.dao.grid_fs.repo.GridFSRepository;
import src.model.CloudObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.logging.Logger;

public class ImagesFromMongoToCloud {

    private ExecutorService executorService = Executors.newFixedThreadPool(32);

    private static final Logger logger = Logger.getLogger(ImagesFromMongoToCloud.class.getName());

    private String BUCKET_NAME = "my-first-bucket-11";

    @Autowired
    private GridFSRepository gridFSRepo;

    @Autowired
    private CloudStorage cloudStorage;

    public void run() throws IOException {

        List<Future> futures = new ArrayList<>();

        List<CloudObject> cloudObjects = new ArrayList<>();

        DBCursor iterator = gridFSRepo.getItearator();
        for (DBObject dbObject : iterator) {
            CloudObject cloudObject = new CloudObject();
            String contentType = dbObject.get("contentType") != null ? (String) dbObject.get("contentType") : "image/jpg";
            InputStream inputStream = gridFSRepo.getFileInputStream((ObjectId) dbObject.get("_id"));
            logger.info("GET IMAGE FROM MONGO. ID: " + dbObject.get("_id"));
            cloudObject.setContentType(contentType);
            cloudObject.setFileName((String) dbObject.get("filename"));
            cloudObject.setInputStream(inputStream);
            cloudObjects.add(cloudObject);
            if (cloudObjects.size() > 100) {
                futures.add(process(cloudObjects));
                cloudObjects = new ArrayList<>();
            }
            if (futures.size() > 100) {
                for (Future future : futures) {
                    try {
                        future.get();
                    } catch (InterruptedException | ExecutionException e) {
                        logger.info("Exception while getting future: " + e.getMessage());
                    }
                }
                futures = new ArrayList<>();
            }
        }
        if (cloudObjects.size() > 0) {
            logger.info("Add last butch");
            futures.add(process(cloudObjects));
        }

    }

    private Future process(List<CloudObject> cloudObjects) {

        return executorService.submit(() -> {
            try {
                saveCloudObjects(cloudObjects);
            } catch (IOException e) {
                logger.info("Exception while saving CloudObject: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    private void saveCloudObjects(List<CloudObject> cloudObjects) throws IOException {
        for (CloudObject cloudObject : cloudObjects) {
            StorageObject storageObject = cloudStorage.uploadFile(BUCKET_NAME, cloudObject.getInputStream(), cloudObject.getFileName(), cloudObject.getContentType());
            logger.info("SAVE IMAGE TO CLOUD STORAGE. IMAGE ID: " + storageObject.getId());
        }
    }
}
