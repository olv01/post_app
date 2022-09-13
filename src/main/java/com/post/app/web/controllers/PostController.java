package com.post.app.web.controllers;

import com.post.app.domain.Post;
import com.post.app.domain.User;
import com.post.app.model.ECategory;
import com.post.app.web.model.Post.PostDto;
import com.post.app.web.model.Post.PostListPaged;
import com.post.app.web.services.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/list")
    public ResponseEntity<PostListPaged> listPosts(
            @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        return new ResponseEntity<>(postService.listPosts(pageNumber, pageSize), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<PostDto> create(@RequestBody Post post,
                                          @AuthenticationPrincipal User user) {
        return new ResponseEntity<>(postService.create(post, user), HttpStatus.CREATED);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDto> getPost(@PathVariable Long postId) {
        return new ResponseEntity<>(postService.findPost(postId), HttpStatus.OK);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<PostDto> putPost(@PathVariable Long postId,
                                           @RequestBody Post post,
                                           @AuthenticationPrincipal User user) {
        return new ResponseEntity<>(postService.putPost(postId, post, user), HttpStatus.OK);
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<PostDto> patchPost(@PathVariable Long postId,
                                             @RequestBody Post post,
                                             @AuthenticationPrincipal User user) {
        return new ResponseEntity<>(postService.patchPost(postId, post, user), HttpStatus.OK);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId,
                                           @AuthenticationPrincipal User user) {
        postService.deleteById(postId, user);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/search")
    public ResponseEntity<PostListPaged> searchPosts(
            @RequestParam(value = "query", required = true) String searchQuery,
            @RequestParam(value = "category", required = true) ECategory category,
            @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        return new ResponseEntity<>(postService.findPostsByCategory(searchQuery, category, pageNumber, pageSize), HttpStatus.OK);
    }
}
