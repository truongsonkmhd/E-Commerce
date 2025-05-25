package vn.feature.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import vn.feature.dtos.CommentDTO;
import vn.feature.exception.payload.DataNotFoundException;
import vn.feature.model.Comment;
import vn.feature.model.Product;
import vn.feature.model.User;
import vn.feature.repositorys.CommentRepository;
import vn.feature.repositorys.ProductRepository;
import vn.feature.repositorys.UserRepository;
import vn.feature.response.comment.CommentResponse;
import vn.feature.service.CommentService;
import vn.feature.util.MessageKeys;

import java.util.List;

@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final UserRepository userRepository;

    private final ProductRepository productRepository;

    @Transactional
    @Override
    public Comment insertComment(CommentDTO commentDTO) {
        User user = userRepository.findById(commentDTO.getUserId()).orElse(null);
        Product product = productRepository.findById(commentDTO.getProductId()).orElse(null);

        if(user == null){
            throw new DataNotFoundException((MessageKeys.USER_NOT_FOUND));
        }

        if(product == null) {
            throw new DataNotFoundException((MessageKeys.PRODUCT_NOT_FOUND));
        }

        Comment newComment = Comment.builder()
                .user(userRepository.findById(commentDTO.getUserId()).get())
                .product(productRepository.findById(commentDTO.getProductId()).get())
                .content(commentDTO.getContent())
                .build();
        return commentRepository.save(newComment);
    }

    @Override
    public void deleteComment(Long id) {

    }

    @Override
    public void updateComment(Long id, CommentDTO comment) throws DataNotFoundException {

    }

    @Override
    public List<CommentResponse> getCommentByUserAndProduct(Long userId, Long productId) {
        return null;
    }

    @Override
    public List<CommentResponse> getCommentByProduct(Long productId) {
        return null;
    }
}
