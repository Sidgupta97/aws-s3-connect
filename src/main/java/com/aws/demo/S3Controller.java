package com.aws.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.services.s3.model.Bucket;
import software.amazon.awssdk.services.s3.model.ListBucketsResponse;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.util.List;

@RestController
@RequestMapping("/s3")
public class S3Controller {

    @Autowired
    private S3Service s3Service;
    @GetMapping("/checkConnection")
    public ResponseEntity<String> checkS3Connection() {
        boolean isConnected = s3Service.isS3ClientConnected();

        if (isConnected) {
            return ResponseEntity.ok("S3 client is connected successfully!");
        } else {
            return ResponseEntity.status(500).body("Failed to connect to S3 client.");
        }
    }
    @GetMapping("/listBuckets")
    public ResponseEntity<List<String>> listS3Buckets() {
        ListBucketsResponse bucketsResponse = s3Service.listBuckets();

        if (bucketsResponse != null) {
            List<Bucket> buckets = bucketsResponse.buckets();
            List<String> bucketNames = extractBucketNames(buckets);
            return ResponseEntity.ok(bucketNames);
        } else {
            // Handle the case where listing buckets failed
            return ResponseEntity.status(500).body(null);
        }
    }
    private List<String> extractBucketNames(List<Bucket> buckets) {
        return buckets.stream()
                .map(Bucket::name)
                .toList();
    }

    @GetMapping("/listObjects")
    public ResponseEntity<List<S3ObjectInfo>> listS3Objects(@RequestParam String bucketName) {
        List<S3ObjectInfo> objects = s3Service.listObjects(bucketName);

        if (objects != null) {
            return ResponseEntity.ok(objects);
        } else {
            // Handle the case where listing objects failed
            return ResponseEntity.status(500).body(null);
        }
    }
}

