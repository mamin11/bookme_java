package theoneamin.bookings.backend.api.aws;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import theoneamin.bookings.backend.api.enums.BucketNames;
import theoneamin.bookings.backend.api.enums.FolderNames;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class StorageService {

    @Autowired private AmazonS3 s3;

    /**
     * Save file to a specified path
     * @param path where to save file
     * @param fileName name of file
     * @param metadata file metadata
     * @param inputStream file input stream
     */
    public void save(String path, String fileName, ObjectMetadata metadata, InputStream inputStream) {
        try {
            s3.putObject(new PutObjectRequest(path, fileName, inputStream, metadata).withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (AmazonServiceException e) {
            throw new IllegalStateException("Failed to store file", e);
        }
    }

    /**
     * Download file from specified path
     * @param path where file is saved
     * @param key file key
     * @return File object
     */
    public S3ObjectInputStream download(String path, String key) {
        try {
            S3Object object =  s3.getObject(path, key);
            return object.getObjectContent();
//            return IOUtils.toByteArray(object.getObjectContent());
        } catch (AmazonServiceException e) {
            throw new IllegalStateException("Failed to get requested file from storage", e);
        }
    }

    /**
     * Delete file from bucket
     * @param bucket where file is saved
     * @param key file identifier
     */
    public void delete(String bucket, String key) {
        try {
            DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucket, key);
            s3.deleteObject(deleteObjectRequest);
            log.warn("Deleted key: {} bucket: {}", key, bucket);
        } catch (AmazonServiceException e) {
            throw new IllegalStateException("failed to delete file", e);
        } catch (SdkClientException e) {
            // Amazon S3 couldn't be contacted for a response, or the client
            // couldn't parse the response from Amazon S3.
            log.error("Sdk client exception: {}", e.getMessage());
        }
    }

}
