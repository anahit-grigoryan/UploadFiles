package src.service;

import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import src.dao.GridFSRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class CloudMigration {

    private ExecutorService executorService = Executors.newFixedThreadPool(32);

    FromMongoToCloud fromMongoToCloud = new FromMongoToCloud();

    private GridFSRepository gridFSRepo = new GridFSRepository();
    private static int threadsCount = 0;


    public void run() throws IOException, InterruptedException {

        List<Future> futures = new ArrayList<>();

        DBCursor iterator = gridFSRepo.getItearator();

        for (DBObject dbObject : iterator) {

            executorService.execute(process(dbObject));

            threadsCount++;
            if(threadsCount > 100){
                executorService.shutdown();
                executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.MINUTES);
                threadsCount = 0;
                executorService = Executors.newFixedThreadPool(32);
            }
        }

    }

    private Runnable process(DBObject dbObject) {
        return ()-> {
                try {
                    fromMongoToCloud.moveImage(dbObject);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            };
    }
}
