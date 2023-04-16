package com.couple.sns.domain.post.controller;

import com.couple.sns.common.responce.Response;
import com.couple.sns.domain.post.dto.Post;
import com.couple.sns.domain.post.dto.request.PostCreateRequest;
import com.couple.sns.domain.post.dto.request.PostModifyRequest;
import com.couple.sns.domain.post.dto.response.LikeResponse;
import com.couple.sns.domain.post.dto.response.PostIdResponse;
import com.couple.sns.domain.post.dto.response.PostResponse;
import com.couple.sns.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping
    public Response<Page<PostResponse>> list(Pageable pageable) {
        return Response.success(postService.list(pageable).map(PostResponse::fromPost));
    }

    @GetMapping("/my")
    public Response<Page<PostResponse>> my(Authentication authentication, Pageable pageable) {
        return Response.success(postService.my(authentication.getName(), pageable).map(PostResponse::fromPost));
    }

    @PostMapping
    public Response<PostResponse> create(@RequestBody PostCreateRequest request,
        Authentication authentication) {
        Post post = postService.create(request.getTitle(), request.getBody(),
            authentication.getName());
        return Response.success(PostResponse.fromPost(post));
    }

    @PutMapping("/{postId}")
    public Response<PostResponse> modify(@PathVariable Long postId,
        @RequestBody PostModifyRequest request,
        Authentication authentication) {
        Post post = postService.modify(postId, request.getBody(), request.getBody(),
            authentication.getName());
        return Response.success(PostResponse.fromPost(post));
    }

    @DeleteMapping("/{postId}")
    public Response<PostIdResponse> delete(@PathVariable Long postId, Authentication authentication) {
        return Response.success(postService.delete(postId, authentication.getName()));
    }

    @PostMapping("/{postId}/likes")
    public Response<Void> like(@PathVariable Long postId, Authentication authentication) {
        postService.like(postId, authentication.getName());
        return Response.success();
    }

    @GetMapping("/{postId}/likes")
    public Response<LikeResponse> likeList(@PathVariable Long postId, Pageable pageable) {
        return Response.success(postService.likeList(postId, pageable));
    }
}
