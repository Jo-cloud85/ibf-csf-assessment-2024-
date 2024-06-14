package ibf2023.csf.backend.repositories;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import ibf2023.csf.backend.utils.Utils;

@Repository
public class ImageRepository {

	// TODO Task 4.1
	// You may change the method signature by adding parameters and/or the return type
	// You may throw any exception 

	@Autowired
    private AmazonS3 s3;

	@Autowired
	private PictureRepository pictureRepo;

	@Value("${s3.bucket.threshold}")
	private Long bucketThreshold;

	@Value("${s3.bucket.threshold.unit}")
	private String bucketThresholdUnit;

	// Save single file
    public String save(MultipartFile file, LocalDateTime uploadDate) throws IOException{

		long bucketThresholdInBytes = bucketThreshold*1024*1024;

		if (checkIfCanUpload(uploadDate, file.getSize(), bucketThresholdInBytes)) {
			Map<String, String> userData = new HashMap<>();
        
			userData.put("upload-timestamp", (new Date()).toString());        
			userData.put("filename", file.getOriginalFilename());
			
			// metadata for the file
			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentType(file.getContentType());
			metadata.setContentLength(file.getSize());
			metadata.setUserMetadata(userData);

			String key = UUID.randomUUID().toString().substring(0, 8);
			// take 4 parameter (bucketname, keyname | filename, inputstream, metadata)
			PutObjectRequest putReq = new PutObjectRequest(Utils.S3_BUCKET_NAME, key, file.getInputStream(), metadata);

			// make object publically available (ppl without key also can access)
			putReq.withCannedAcl(CannedAccessControlList.PublicRead);

			// PutObjectResult result = s3.putObject(putReq);
			s3.putObject(putReq);
			
			return s3.getUrl(Utils.S3_BUCKET_NAME, key).toString();
		} else {
			return "Error saving to S3";
		}        
    }

	// Calculate size of all files in a given month
	// public Long getSpaceUsedInS3Bucket(Integer month) {
	// 	ListObjectsRequest request = new ListObjectsRequest();
	// 	request.setBucketName(Utils.S3_BUCKET_NAME);
	// 	ObjectListing response = s3.listObjects(request);
	// 	Long totalSize = 0L;
	// 	for (S3ObjectSummary o : response.getObjectSummaries()) {
	// 		// Get the date of object first and check if it matches the integer month
	// 		// if matched, get the size and total up
	// 		Date s3ObjectDate = o.getLastModified();
	// 		LocalDate s3ObjectLocalDate = s3ObjectDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	// 		int monthVal = s3ObjectLocalDate.getMonthValue();
	// 		if (monthVal == month) {
	// 			totalSize += o.getSize();
	// 		}
	// 	}
	// 	System.out.println("Total Size of bucket " + Utils.S3_BUCKET_NAME + " is " + Math.round(totalSize / 1024.0 / 1024.0) + " MB");
	// 	return totalSize;
	// }

	public boolean checkIfCanUpload(LocalDateTime uploadDate, Long fileSizeInBytes, Long threshold) {
		System.out.println("FROM IMAGEREPO, CHECKING FROM MONGO TOTAL FILE SIZE: " + pictureRepo.getTotalFileSizeInMonth(uploadDate));
		if (pictureRepo.getTotalFileSizeInMonth(uploadDate).isEmpty()) {
			System.out.println("No record in Mongo yet");
			return false;
		} else {
			return ((fileSizeInBytes + pictureRepo.getTotalFileSizeInMonth(uploadDate).get()) <=  threshold);
		}
	}

	// public void save() {
	// 	// IMPORTANT: Write the native mongo query in the comments above this method
	// }
}

