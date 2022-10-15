package theoneamin.bookings.backend.api.utility;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import theoneamin.bookings.backend.api.aws.StorageService;
import theoneamin.bookings.backend.api.entity.user.UserEntity;
import theoneamin.bookings.backend.api.enums.BucketNames;
import theoneamin.bookings.backend.api.enums.FolderNames;

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
}
