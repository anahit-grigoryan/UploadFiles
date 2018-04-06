package src.dao.cloud;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.storage.Storage;
import com.google.api.services.storage.model.ObjectAccessControl;
import com.google.api.services.storage.model.StorageObject;
import com.google.cloud.storage.BlobInfo;

import java.io.*;
import java.util.Arrays;

public class new_version {
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static HttpTransport httpTransport;
    private static Storage storage = null;

    static {
        try {
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            GoogleCredential credential = GoogleCredential.getApplicationDefault();
            storage = new Storage(httpTransport, JSON_FACTORY, credential);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public BlobInfo uploadFile(final String bucketName, String filePath, final String fileName, String contentType) throws IOException {
        File initialFile = new File(filePath);
        InputStream fileStream = new FileInputStream(initialFile);

        InputStreamContent contentStream = new InputStreamContent(
                contentType, fileStream);

        StorageObject objectMetadata = new StorageObject()
                .setName("test")
                .setAcl(Arrays.asList(
                        new ObjectAccessControl().setEntity("allUsers").setRole("READER")));

        Storage.Objects.Insert insertRequest = storage.objects().insert(bucketName,objectMetadata,contentStream);

        insertRequest.execute();
        return null;
    }

//    public void downloadFile(final String bucketName, final String fileName, String filePath) throws IOException {
//        Blob blob = storage.get(bucketName, fileName);
//        ReadChannel readChannel = blob.reader();
//        FileOutputStream fileOuputStream = new FileOutputStream(filePath);
//        fileOuputStream.getChannel().transferFrom(readChannel, 0, Long.MAX_VALUE);
//        fileOuputStream.close();
//
//    }
}