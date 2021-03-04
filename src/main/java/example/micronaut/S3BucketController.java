/*
 * Copyright 2017-2020 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package example.micronaut;

import io.micronaut.context.annotation.Parameter;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.regions.providers.AwsRegionProviderChain;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.Bucket;
import software.amazon.awssdk.services.s3.model.CreateBucketConfiguration;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.CreateBucketResponse;
import software.amazon.awssdk.services.s3.model.DeleteBucketRequest;
import software.amazon.awssdk.services.s3.model.DeleteBucketResponse;
import software.amazon.awssdk.services.s3.model.ListBucketsResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.util.stream.Collectors;

@Controller("/s3/buckets")
public class S3BucketController {

    private final AwsRegionProviderChain awsRegionProviderChain;
    private final S3Client s3Client;

    public S3BucketController(AwsRegionProviderChain awsRegionProviderChain,
                              S3Client s3Client) {
        this.awsRegionProviderChain = awsRegionProviderChain;
        this.s3Client = s3Client;
    }

    @Post("/{bucketName}")
    public Result createBucket(String bucketName) {
        try {
            CreateBucketRequest createBucketRequest = CreateBucketRequest.builder()
                    .bucket(bucketName)
                    .createBucketConfiguration(CreateBucketConfiguration.builder()
                            .locationConstraint(awsRegionProviderChain.getRegion().toString())
                            .build())
                    .build();
            CreateBucketResponse response = s3Client.createBucket(createBucketRequest);
            return new Result(response.responseMetadata().requestId(),
                    String.valueOf(response.sdkHttpResponse().statusCode()),
                    response.location()
            );
        } catch (S3Exception s3Exception) {
            return new Result(s3Exception.requestId(), String.valueOf(s3Exception.statusCode()), s3Exception.getMessage());
        } catch (SdkException sdkException) {
            return new Result("N/A", sdkException.getMessage(), sdkException.getLocalizedMessage());
        }
    }

    @Get("/")
    public ListBucketsResult index() {
        try {
            ListBucketsResponse response = s3Client.listBuckets();
            return new ListBucketsResult(
                    response.responseMetadata().requestId(),
                    String.valueOf(response.sdkHttpResponse().statusCode()),
                    response.buckets().stream().map(Bucket::name).collect(Collectors.toList())
            );
        } catch (S3Exception s3Exception) {
            return new ListBucketsResult(s3Exception.requestId(), String.valueOf(s3Exception.statusCode()), null);
        } catch (SdkException sdkException) {
            return new ListBucketsResult("N/A", sdkException.getMessage(), null);
        }
    }

    @Delete("/{bucketName}")
    public Result deleteBucket(String bucketName) {
        try {
            DeleteBucketRequest deleteBucketRequest = DeleteBucketRequest.builder()
                    .bucket(bucketName)
                    .build();

            DeleteBucketResponse response = s3Client.deleteBucket(deleteBucketRequest);

            return new Result(response.responseMetadata().requestId(),
                    String.valueOf(response.sdkHttpResponse().statusCode()),
                    null
            );
        } catch (S3Exception s3Exception) {
            return new Result(s3Exception.requestId(), String.valueOf(s3Exception.statusCode()), s3Exception.getMessage());
        } catch (SdkException sdkException) {
            return new Result("N/A", sdkException.getMessage(), sdkException.getLocalizedMessage());
        }
    }
}
