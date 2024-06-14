package ibf2023.csf.backend.services;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import ibf2023.csf.backend.models.Post;
import ibf2023.csf.backend.repositories.ImageRepository;
import ibf2023.csf.backend.repositories.PictureRepository;

@Service
public class PictureService {

	// TODO Task 5.1
	// You may change the method signature by adding parameters and/or the return type
	// You may throw any exception

	// Should not save if size of total number of images uploaded in curr mth exceeds preconfigured threshold
	
	@Autowired
	private PictureRepository pictureRepo;

	@Autowired
	private ImageRepository imageRepo;

	public String save(MultipartFile file, LocalDateTime uploadDate) throws IOException {
		return imageRepo.save(file, uploadDate);
	}

	public String saveToMongo(Post post) {
		String id = pictureRepo.save(post);
		return id;
	}
}
