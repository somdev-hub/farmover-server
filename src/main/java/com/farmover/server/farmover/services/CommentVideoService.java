package com.farmover.server.farmover.services;

import java.util.List;

import com.farmover.server.farmover.payloads.CommentVideoDto;
import com.farmover.server.farmover.payloads.request.CommentVideoRequest;

public interface CommentVideoService {
    void addComment(CommentVideoRequest request);
    List<CommentVideoDto> getAllComment(Integer videoId);
    List<CommentVideoDto> getAllCommentByUser(String uname);
    void deleteComment(Integer id);

}
