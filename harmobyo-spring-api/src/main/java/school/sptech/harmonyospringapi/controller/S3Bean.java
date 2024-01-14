package school.sptech.harmonyospringapi.controller;

import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.regions.Regions;

@Configuration
public class S3Bean {
    public S3Bean() {
    }

    @Bean
    public AmazonS3 amazonS3() {
        Regions region = Regions.US_EAST_1;
        ClientConfiguration clientConfiguration = new ClientConfiguration();
        return (AmazonS3)((AmazonS3ClientBuilder)((AmazonS3ClientBuilder)AmazonS3Client.builder().withRegion(region)).withClientConfiguration(clientConfiguration)).build();
    }
}