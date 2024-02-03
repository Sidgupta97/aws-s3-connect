package com.aws.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.rekognition.RekognitionClient;
import software.amazon.awssdk.services.rekognition.model.DetectLabelsRequest;
import software.amazon.awssdk.services.rekognition.model.DetectLabelsResponse;
import software.amazon.awssdk.services.rekognition.model.Image;
import software.amazon.awssdk.services.rekognition.model.Label;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListBucketsResponse;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class S3Service {
    private static final Logger logger = LoggerFactory.getLogger(S3Service.class);

    @Value("${aws.accessKeyId}")
    private String accessKey;

    @Value("${aws.secretKey}")
    private String secretKey;

    @Value("${aws.region}")
    private String region;

    @Value("${aws.bucketName}")
    private String bucketName;

    public S3Client createS3Client() {
        return S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
                .build();
    }
    public boolean isS3ClientConnected() {
        try {
            // Attempt to list buckets as a simple check
            createS3Client().listBuckets();
            return true;
        } catch (Exception e) {
            logger.error("Error connecting to S3 client: {}", e.getMessage());
            return false;
        }
    }
    public ListBucketsResponse listBuckets() {
        try {
            // Use the S3 client to list buckets
            return createS3Client().listBuckets();
        } catch (Exception e) {
            // Log the error when listing buckets fails
            logger.error("Error listing S3 buckets: {}", e.getMessage());
            return null;
        }
    }

    public List<S3ObjectInfo> listObjects(String bucketName) {
        try {
            // Use the S3 client to list objects in the specified bucket
            ListObjectsV2Response response = createS3Client().listObjectsV2(builder -> builder.bucket(bucketName));

            // Extract relevant information from S3Object and return a list
            return response.contents().stream()
                    .map(this::mapToS3ObjectInfo)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            // Log the error when listing objects fails
            logger.error("Error listing objects in S3 bucket '{}': {}", bucketName, e.getMessage());
            return null;
        }
    }

    private S3ObjectInfo mapToS3ObjectInfo(S3Object s3Object) {
        return new S3ObjectInfo(s3Object.key(), s3Object.size(), s3Object.lastModified());
    }

}

