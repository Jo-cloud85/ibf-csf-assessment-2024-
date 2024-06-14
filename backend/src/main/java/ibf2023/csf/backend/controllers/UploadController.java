package ibf2023.csf.backend.controllers;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import ibf2023.csf.backend.models.Post;
import ibf2023.csf.backend.services.PictureService;
import jakarta.json.Json;
import jakarta.json.JsonObject;

// You can add addtional methods and annotations to this controller. 
// You cannot remove any existing annotations or methods from UploadController
@Controller
@RequestMapping(path="/api")
public class UploadController {

	@Autowired
	private PictureService pictureSvc;

	@Value("${s3.bucket.threshold}")
	private Long bucketThreshold;

	@Value("${s3.bucket.threshold.unit}")
	private String bucketThresholdUnit;

	// TODO Task 5.2
	// You may change the method signature by adding additional parameters and annotations.
	// You cannot remove any any existing annotations and parameters from postUpload()
	@PostMapping(path="/image/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<String> postUpload(
		@RequestPart String title,
		@RequestPart(required = false) String comments,
		@RequestPart("picture") MultipartFile picture
	) throws IOException {

		System.out.println("picture input stream >>> " + picture.getInputStream().toString());
		System.out.println("picture original filename >>> " + picture.getOriginalFilename().toString());
		System.out.println("picture size >>> " + picture.getSize());

		Post post = new Post();

		// get file size and check against current Mongo
		long pictureInBytes = picture.getSize();

		LocalDateTime uploadDate = LocalDateTime.now();

		// Saving to s3 first
		String s3Url = pictureSvc.save(picture, uploadDate);

		if (s3Url != "") {
			post.setDate(uploadDate);
			post.setTitle(title);
			post.setComments(comments);
			post.setUrl(s3Url);
			post.setPictureSize(pictureInBytes);

			// Then save to mongo
			String id = pictureSvc.saveToMongo(post);

			JsonObject jsonObj = Json.createObjectBuilder()
				.add("id", id)
				.build();
			return ResponseEntity.ok(jsonObj.toString());

		} else if (s3Url.equals("Error saving to S3")) {
			String errMsg = "This upload has exceeded your monthly upload quota of the " + bucketThreshold + bucketThresholdUnit;
			JsonObject jsonObj = Json.createObjectBuilder()
				.add("message", errMsg)
				.build();
			return ResponseEntity.status(HttpStatusCode.valueOf(413)).body(jsonObj.toString());

		} else {
			String otherErrMsg = "Seems like there is internal server error...";
			JsonObject jsonObj = Json.createObjectBuilder()
				.add("message", otherErrMsg)
				.build();
			return ResponseEntity.status(HttpStatusCode.valueOf(500)).body(jsonObj.toString());
		}
	}
}
