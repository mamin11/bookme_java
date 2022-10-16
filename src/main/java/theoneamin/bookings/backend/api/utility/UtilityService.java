package theoneamin.bookings.backend.api.utility;


import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import theoneamin.bookings.backend.api.aws.StorageService;
import theoneamin.bookings.backend.api.entity.user.UserEntity;
import theoneamin.bookings.backend.api.enums.BucketNames;
import theoneamin.bookings.backend.api.enums.FolderNames;

import java.io.IOException;
import java.util.UUID;

/**
 * This class contains utility methods that can be used for various purposes
 */

@Slf4j
@Service
public class UtilityService {
    @Autowired StorageService storageService;

    /**
     * Get user image URI
     * @param userEntity user
     * @return URI
     */
    public String getImageLink(UserEntity userEntity) {
        if (userEntity.getImage() != null) {
            return this
                    .getLink(this.constructFilePath(
                                            BucketNames.BOOKING_APP_STORE,
                                            FolderNames.PROFILE_PICTURES,
                                            userEntity.getUserId()),
                            userEntity.getImage());
        } else {
            return null;
        }
    }

    /**
     * Get file link
     * @param path path
     * @param key file name
     * @return full URI
     */
    public String getLink(String path, String key) {
        return storageService.download(path, key).getHttpRequest().getURI().toString();
    }

    /**
     * Construct path from folder and user id
     * @param key user id
     * @param bucketName bucket name
     * @param folderName folder name
     * @return path
     */
    public String constructFilePath(BucketNames bucketName, FolderNames folderName, Integer key) {
        return String.format("%s/%s/%S", bucketName.getStringValue(), folderName, key);
    }

    /**
     * Handles profile picture upload
     * @param userEntity user
     * @param file file
     * @return image link
     */
    public String handleImageUpload(UserEntity userEntity, MultipartFile file) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        //store the file
        //create a path depending on the module course, so that all of a course's content is in the same bucket
        String path = constructFilePath(BucketNames.BOOKING_APP_STORE, FolderNames.PROFILE_PICTURES, userEntity.getUserId());
        //create a filename from original filename and random UUID
        String filename = String.format("%s-%s", UUID.randomUUID(), file.getOriginalFilename());

        try {
            storageService.save(path, filename, metadata, file.getInputStream());
            return filename;
        } catch (IOException e) {
            throw new IllegalStateException("error", e);
        }
    }
}
