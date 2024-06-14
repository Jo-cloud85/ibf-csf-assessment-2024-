package ibf2023.csf.backend;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

// IMPORTANT: Do not modify this file. 
@Configuration
public class AppConfig {

	@Value("${s3.key.secret}")
	private String secretKey;

	@Value("${s3.key.access}")
	private String accessKey;

	@Value("${s3.endpoint}")
	private String endpoint;

	@Value("${s3.region}")
	private String region;

	@Bean
	public AmazonS3 createS3Client() {
		BasicAWSCredentials cred = new BasicAWSCredentials(accessKey, secretKey);
		EndpointConfiguration endpointConfig = new EndpointConfiguration(endpoint, region);

		return AmazonS3ClientBuilder.standard()
			.withEndpointConfiguration(endpointConfig)
			.withCredentials(new AWSStaticCredentialsProvider(cred))
			.build();
	}
}
