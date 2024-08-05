package com.farmover.server.farmover.services;

import java.util.List;

import com.farmover.server.farmover.payloads.RecentsCommentsDto;
import com.farmover.server.farmover.payloads.request.CommentVideoRequest;

public interface CommentVideoService {
    void addComment(CommentVideoRequest request);
    List<RecentsCommentsDto> getAllComment(Integer videoId);
    List<RecentsCommentsDto> getAllCommentByUser(String uname);
    void deleteComment(Integer id);

}
