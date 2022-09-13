package com.post.app.web.controllers;

import com.post.app.domain.PostComment;
import com.post.app.domain.User;
import com.post.app.web.model.Post.PostCommentDto;
import com.post.app.web.services.PostCommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts/{postId}/comments")
public class PostCommentController {

    private final PostCommentService postCommentService;

    public PostCommentController(PostCommentService postCommentService) {
        this.postCommentService = postCommentService;
    }

    @PostMapping("/create")
    public ResponseEntity<PostCommentDto> createPostComment(@PathVariable("postId") Long postId,
                                                            @RequestBody PostComment postComment,
                                                            @AuthenticationPrincipal User user) {
        return new ResponseEntity<>(postCommentService.create(postId, postComment, user), HttpStatus.CREATED);
    }

    @DeleteMapping("/{postCommentId}")
    public ResponseEntity<Void> deletePostComment(@PathVariable("postCommentId") Long postCommentId,
                                                  @AuthenticationPrincipal User user) {
        postCommentService.deleteById(postCommentId, user);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{postCommentId}")
    public ResponseEntity<PostCommentDto> patchPostComment(@PathVariable("postCommentId") Long postCommentId,
                                                           @RequestBody PostComment postComment,
                                                           @AuthenticationPrincipal User user) {
        return new ResponseEntity<>(postCommentService.update(postCommentId, postComment, user), HttpStatus.OK);
    }
}
