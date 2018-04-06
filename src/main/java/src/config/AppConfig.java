package src.config;

import com.mongodb.gridfs.GridFS;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import src.dao.cloud.CloudStorage;
import src.dao.grid_fs.config.GridFsFuctory;
import src.dao.grid_fs.repo.GridFSRepository;
import src.service.ImagesFromMongoToCloud;

@Configuration
public class AppConfig {
    @Bean
    public GridFSRepository gridFSRepo() {
        return new GridFSRepository();
    }

    @Bean
    public ImagesFromMongoToCloud moveService() {
        return new ImagesFromMongoToCloud();
    }

    @Bean
    public CloudStorage cloudStorage() {
        return new CloudStorage();
    }

    @Bean
    public GridFS gridFs() {
        return GridFsFuctory.create("localhost", 27017, "myFiles", "fs");
    }
}