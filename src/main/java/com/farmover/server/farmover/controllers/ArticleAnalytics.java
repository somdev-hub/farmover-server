package com.farmover.server.farmover.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.farmover.server.farmover.payloads.CommentArticleDto;
import com.farmover.server.farmover.payloads.request.CommentArticleRequest;
import com.farmover.server.farmover.payloads.request.DownVoteArticleRequest;
import com.farmover.server.farmover.payloads.request.UpVoteArticleRequest;
import com.farmover.server.farmover.services.impl.CommentArticleServiceImp;
import com.farmover.server.farmover.services.impl.DownVoteArticleServiceImpl;
import com.farmover.server.farmover.services.impl.UpVoteArticleServiceImpl;

@RestController
@CrossOrigin
@RequestMapping("/article-analytics")
public class ArticleAnalytics {
    @Autowired
    UpVoteArticleServiceImpl vUpVoteVideoServiceImpl;
    @Autowired
    DownVoteArticleServiceImpl downVoteServiceImp;
    @Autowired
    CommentArticleServiceImp commentVideoServiceImp;

    @PostMapping("/up-vote")
    public String upVote(@RequestBody UpVoteArticleRequest entity) {
        vUpVoteVideoServiceImpl.upVote(entity);
        
        return "done";
    }
    @PostMapping("/down-vote")
    public String downVote(@RequestBody DownVoteArticleRequest entity) {
        downVoteServiceImp.downVote(entity);
        return "done";
    }
    @DeleteMapping("/delete-up-vote")
    public String deleteUpVote(@RequestParam int articleId,@RequestParam String email) {
        vUpVoteVideoServiceImpl.deleteUpVote(email,articleId);
        
        return "done";
    }
    @DeleteMapping("/delete-down-vote")
    public String deleteDownVote(@RequestParam int articleId,@RequestParam String email) {
        downVoteServiceImp.deleteDownVote(email,articleId);
        return "done";
    }
    @PostMapping("/add-comment")
    public String addComment(@RequestBody CommentArticleRequest entity) {
        commentVideoServiceImp.addComment(entity);
        return "done";
    }
    @DeleteMapping("/delete-comment")
    public String deleteComment(@RequestParam int id) {
        commentVideoServiceImp.deleteComment(id);
        return "done";
    }
    @GetMapping("/get-all-comment")
    public List<CommentArticleDto> getAllComment(@RequestParam int articleId) {
        return commentVideoServiceImp.getAllComment(articleId);
    }
    @GetMapping("/get-all-comment-by-user")
    public List<CommentArticleDto> getAllCommentByUser(@RequestParam String email) {
        return commentVideoServiceImp.getAllCommentByUser(email);
    }
}
