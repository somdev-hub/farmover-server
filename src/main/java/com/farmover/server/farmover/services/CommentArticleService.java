package com.farmover.server.farmover.services;

import java.util.List;

import com.farmover.server.farmover.payloads.CommentArticleDto;
import com.farmover.server.farmover.payloads.request.CommentArticleRequest;

public interface CommentArticleService {
    void addComment(CommentArticleRequest request);
    List<CommentArticleDto> getAllComment(Integer articleId);
    List<CommentArticleDto> getAllCommentByUser(String uname);
    void deleteComment(Integer id);
    
}
