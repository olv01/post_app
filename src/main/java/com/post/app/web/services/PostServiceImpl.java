package com.post.app.web.services;

import com.post.app.domain.Post;
import com.post.app.domain.User;
import com.post.app.exception.ResourceNotFoundException;
import com.post.app.repositories.PostRepository;
import com.post.app.web.model.BaseResponse;
import com.post.app.web.model.Post.PostDto;
import com.post.app.web.model.Post.PostListItem;
import com.post.app.web.model.Post.PostListPaged;
import com.post.app.web.model.mapper.PostMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {

    private static final Integer DEFAULT_PAGE_NUMBER = 0;
    private static final Integer DEFAULT_PAGE_SIZE = 20;
    private final PostRepository postRepository;
    private final PostMapper postMapper;

    public PostServiceImpl(PostRepository postRepository, PostMapper postMapper) {
        this.postRepository = postRepository;
        this.postMapper = postMapper;
    }

    @Transactional
    @Override
    public PostListPaged listPosts(Integer pageNumber, Integer pageSize) {
        if (pageNumber == null || pageNumber < 0) {
            pageNumber = DEFAULT_PAGE_NUMBER;
        }
        if (pageSize == null || pageSize < 1 || pageSize > 100) {
            pageSize = DEFAULT_PAGE_SIZE;
        }

        PageRequest page = PageRequest.of(pageNumber, pageSize, Sort.by("createdDate").descending());
        Page<PostListItem> postListPage = postRepository.findPostListItem(page);

        return new PostListPaged(postListPage.getContent(),
                PageRequest.of(postListPage.getPageable().getPageNumber(), postListPage.getPageable().getPageSize()),
                postListPage.getTotalPages());
    }

    @Override
    public PostDto findPost(Long postId) {
        Optional<Post> post = postRepository.findById(postId);
        if (post.isEmpty()) {
            throw new ResourceNotFoundException("Post not found");
        }
        return postMapper.postToPostDto(post.get());
    }

    @Override
    public PostDto create(Post post, User user) {
        post.setUser(user);
        Post createdPost = postRepository.save(post);
        return postMapper.postToPostDto(createdPost);
    }

    @Override
    public BaseResponse deleteById(Long postId, User user) {
        Post foundPost = checkPostOwner(postId, user);
        postRepository.delete(foundPost);
        return new BaseResponse("Deleted Successfully");
    }

    @Override
    public PostDto putPost(Long postId, Post newPost, User user) {
        checkPostOwner(postId, user);
        newPost.setId(postId);
        newPost.setUser(user);
        Post updatedPost = postRepository.save(newPost);
        return postMapper.postToPostDto(updatedPost);
    }

    @Override
    public PostDto patchPost(Long postId, Post patchPost, User user) {
        Post foundPost = checkPostOwner(postId, user);

        if (patchPost.getTitle() != null) {
            foundPost.setTitle(patchPost.getTitle());
        }
        if (patchPost.getContent() != null) {
            foundPost.setContent(patchPost.getContent());
        }

        Post updatedPost = postRepository.save(foundPost);
        return postMapper.postToPostDto(updatedPost);
    }

    @Transactional
    // checks if user owns the post. and then it returns found post.
    private Post checkPostOwner(Long postId, User user) {
        // If post doesn't exist, raise exception
        Optional<Post> post = postRepository.findById(postId);
        if (post.isEmpty()) {
            throw new ResourceNotFoundException("Post not found");
        }

        // If user isn't the owner of a post, raise exception
        if (!user.getUsername().equals(post.get().getUser().getUsername())) {
            throw new AccessDeniedException("Not allowed");
        }

        // Otherwise, return a post
        return post.get();
    }
}
