package src.dao;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.storage.Storage;
import com.google.api.services.storage.model.ObjectAccessControl;
import com.google.api.services.storage.model.StorageObject;

import java.io.*;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CloudStorageRepository {

    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    private static final Logger logger = Logger.getLogger(CloudStorageRepository.class.getName());

    private static HttpTransport httpTransport;

    private static Storage storage = null;

    static {

        try {

            httpTransport = GoogleNetHttpTransport.newTrustedTransport();

            GoogleCredential credential = GoogleCredential.getApplicationDefault();

            storage = new com.google.api.services.storage.Storage(httpTransport, JSON_FACTORY, credential);


        } catch (Exception e) {

            logger.log(Level.SEVERE,"Error in static initialization block", e.getMessage());

        }

    }

    public StorageObject uploadFile(final String bucketName, InputStream inputStream, final String fileName, String contentType) throws IOException {

        InputStreamContent contentStream = new InputStreamContent(contentType, inputStream);

        StorageObject objectMetadata = new StorageObject().setName(fileName).setAcl(Arrays.asList(new ObjectAccessControl().setEntity("allUsers").setRole("READER")));

        Storage.Objects.Insert insertRequest = storage.objects().insert(bucketName, objectMetadata, contentStream);

        return insertRequest.execute();

    }

}