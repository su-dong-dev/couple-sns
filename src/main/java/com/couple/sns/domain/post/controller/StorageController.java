package com.couple.sns.domain.post.controller;

import com.couple.sns.common.responce.Response;
import com.couple.sns.domain.post.service.StorageService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/s3")
public class StorageController {

    final private StorageService awsS3Service;

    @PostMapping("/file")
    public Response<List<String>> uploadFile(@RequestPart List<MultipartFile> multipartFile) {
        return Response.success(awsS3Service.uploadImage(multipartFile));
    }

    @DeleteMapping("/file")
    public Response<Void> deleteFile(@RequestParam String fileName) {
        awsS3Service.deleteImage(fileName);
        return Response.success();
    }
}
