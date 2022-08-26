package com.post.app.web.services;

import com.post.app.domain.Post;
import com.post.app.domain.PostComment;
import com.post.app.domain.User;
import com.post.app.exception.ResourceNotFoundException;
import com.post.app.repositories.PostCommentRepository;
import com.post.app.repositories.PostRepository;
import com.post.app.web.model.BaseResponse;
import com.post.app.web.model.Post.PostCommentDto;
import com.post.app.web.model.mapper.PostCommentMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PostCommentServiceImpl implements PostCommentService {

    private final PostRepository postRepository;
    private final PostCommentRepository postCommentRepository;
    private final PostCommentMapper postCommentMapper;

    public PostCommentServiceImpl(PostRepository postRepository,
                                  PostCommentRepository postCommentRepository,
                                  PostCommentMapper postCommentMapper) {
        this.postRepository = postRepository;
        this.postCommentRepository = postCommentRepository;
        this.postCommentMapper = postCommentMapper;
    }

    @Override
    public PostCommentDto create(Long postId, PostComment newPostComment, User user) {
        Optional<Post> post = postRepository.findById(postId);
        if (post.isEmpty()) {
            throw new ResourceNotFoundException("Post not found");
        }
        newPostComment.setPost(post.get());
        newPostComment.setUser(user);
        PostComment createdPostComment = postCommentRepository.save(newPostComment);
        return postCommentMapper.postCommentToPostCommentDto(createdPostComment);
    }

    @Override
    public BaseResponse deleteById(Long postCommentId, User user) {
        checkPostCommentOwner(postCommentId, user);
        postCommentRepository.deleteById(postCommentId);
        return new BaseResponse("Deleted Successfully");
    }

    @Override
    public PostCommentDto update(Long postCommentId, PostComment patchPostComment, User user) {
        PostComment foundPostComment = checkPostCommentOwner(postCommentId, user);

        if (patchPostComment.getContent() != null) {
            foundPostComment.setContent(patchPostComment.getContent());
        }
        PostComment updatedPostComment = postCommentRepository.save(foundPostComment);
        return postCommentMapper.postCommentToPostCommentDto(updatedPostComment);
    }

    private PostComment checkPostCommentOwner(Long postCommentId, User user) {
        Optional<PostComment> postComment = postCommentRepository.findById(postCommentId);
        if (postComment.isEmpty()) {
            throw new ResourceNotFoundException("PostComment not found");
        }
        if (!user.getUsername().equals(postComment.get().getUser().getUsername())) {
            throw new AccessDeniedException("Not allowed");
        }
        return postComment.get();
    }
}
