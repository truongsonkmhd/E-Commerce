package vn.feature.service;

import vn.feature.dtos.CommentDTO;
import vn.feature.model.Comment;
import vn.feature.response.comment.CommentResponse;

import javax.xml.crypto.Data;
import java.util.List;

public interface CommentService {
    Comment insertComment(CommentDTO comment);

    void deleteComment(Long id);

    void updateComment(Long id , CommentDTO comment) throws Exception;

    List<CommentResponse> getCommentByUserAndProduct(Long userId , Long productId);

    List<CommentResponse> getCommentByProduct(Long productId);
}
