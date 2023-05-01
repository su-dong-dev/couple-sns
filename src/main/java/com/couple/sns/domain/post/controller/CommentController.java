package com.couple.sns.domain.post.controller;

import com.couple.sns.common.responce.Response;
import com.couple.sns.domain.post.dto.response.CommentResponse;
import com.couple.sns.domain.post.dto.response.LikeResponse;
import com.couple.sns.domain.post.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/{postId}/comment")
    public Response<Page<CommentResponse>> list(@PathVariable Long postId, Pageable pageable) {
        return Response.success(commentService.list(postId, pageable).map(CommentResponse::fromComment));
    }

    @PostMapping("/{postId}/comment")
    public Response<CommentResponse> comment(Authentication authentication, @PathVariable Long postId, @RequestBody String content) {
        return Response.success(commentService.create(authentication.getName(), postId, content));
    }

    @DeleteMapping("/comment/{commentId}")
    public Response<Void> delete(Authentication authentication, @PathVariable Long commentId) {
        commentService.delete(authentication.getName(), commentId);
        return Response.success();
    }

    @PostMapping("/comment/{commendId}/likes")
    public Response<String> like(@PathVariable Long commendId, Authentication authentication) {
        String isLiked = commentService.like(commendId, authentication.getName());
        return Response.success(isLiked);
    }

    @GetMapping("/comment/{commendId}/likes")
    public Response<LikeResponse> likeList(@PathVariable Long commendId, Pageable pageable) {
        return Response.success(commentService.likeList(commendId, pageable));
    }
}
