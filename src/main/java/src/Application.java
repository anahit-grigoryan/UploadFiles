package src;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import src.config.AppConfig;
import src.service.ImagesFromMongoToCloud;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Application {

    public static void main(String[] args) throws FileNotFoundException {

        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
        ImagesFromMongoToCloud moveService = applicationContext.getBean(ImagesFromMongoToCloud.class);

        try {
            moveService.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
