package com.hanahakdangserver.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

@Log4j2
@RequiredArgsConstructor
@Component
public class S3FileProcessor {

  private final S3AsyncClient s3AsyncClient;

  /**
   * 이미지를 S3 버킷에 업로드 하고 url 주소를 받아오는 메서드
   *
   * @param imageFile 업로드할 이미지 파일
   * @return S3에 업로드된 url 반환
   * @throws IOException
   */
  public String uploadImageFileToS3(String bucket, MultipartFile imageFile)
      throws IOException {
    ExecutorService executorService = Executors.newFixedThreadPool(10);

    try (InputStream inputStream = imageFile.getInputStream()) {
      String originalFilename = imageFile.getOriginalFilename();
      String uuid = UUID.randomUUID().toString();
      String imageFileName = uuid + originalFilename;

      PutObjectRequest putObjectRequest = PutObjectRequest.builder()
          .bucket(bucket)
          .key(imageFileName)
          .contentType(imageFile.getContentType())
          .contentLength(imageFile.getSize())
          .build();

      // 파일 업로드 (비동기 처리)
      CompletableFuture<PutObjectResponse> future = s3AsyncClient.putObject(
          putObjectRequest,
          AsyncRequestBody.fromInputStream(
              inputStream,
              imageFile.getSize(),
              executorService
          )
      );

      // 비동기 결과를 기다리고 응답 확인
      PutObjectResponse response = future.join();

      // 업로드 성공 여부 확인
      if (response.sdkHttpResponse().isSuccessful()) {
        // 업로드된 파일의 URL 가져오기
        String imageUrl = String.format("https://%s.s3.%s.amazonaws.com/%s",
            bucket,
            Region.AP_NORTHEAST_2.toString().toLowerCase(),
            imageFileName);
        return imageUrl;

      } else {
        throw new RuntimeException(
            "S3 업로드 실패: " + response.sdkHttpResponse().statusText().orElse("UNKNOWN ERROR"));
      }

    } catch (IOException e) {
      log.warn("파일 업로드 실패: {}", e.getMessage());
      throw new RuntimeException("S3 파일 업로드 중 오류 발생", e);
    }
  }

}
