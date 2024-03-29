package com.couple.sns.domain.post.controller;

import com.couple.sns.common.responce.Response;
import com.couple.sns.domain.post.dto.request.PostRequest;
import com.couple.sns.domain.post.dto.response.PostLikeResponse;
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
    public Response<Page<PostResponse>> getPosts(Pageable pageable) {
        return Response.success(postService.getPosts(pageable).map(PostResponse::fromPost));
    }

    @GetMapping("/my")
    public Response<Page<PostResponse>> getMyPosts(Authentication authentication, Pageable pageable) {
        return Response.success(postService.getMyPosts(authentication.getName(), pageable).map(PostResponse::fromPost));
    }

    @PostMapping
    public Response<PostResponse> create(@RequestBody PostRequest request, Authentication authentication) {
        return Response.success(postService.create(authentication.getName(), request.toDto()));
    }

    @PutMapping("/{postId}")
    public Response<PostResponse> modify(
        @PathVariable Long postId,
        @RequestBody PostRequest request,
        Authentication authentication) {
        return Response.success(postService.modify(postId, authentication.getName(), request.toDto()));
    }

    @DeleteMapping("/{postId}")
    public Response<Void> delete(@PathVariable Long postId, Authentication authentication) {
        postService.delete(postId, authentication.getName());
        return Response.success();
    }

    @PostMapping("/{postId}/likes")
    public Response<Boolean> like(@PathVariable Long postId, Authentication authentication) {
        return Response.success(postService.like(postId, authentication.getName()));
    }

    @GetMapping("/{postId}/likes")
    public Response<PostLikeResponse> likeList(@PathVariable Long postId, Pageable pageable) {
        return Response.success(postService.likeList(postId, pageable));
    }
}
