package com.farmover.server.farmover.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
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
@RequestMapping("/articleAnalytics")
public class ArticleAnalytics {
    @Autowired
    UpVoteArticleServiceImpl vUpVoteVideoServiceImpl;
    @Autowired
    DownVoteArticleServiceImpl downVoteServiceImp;
    @Autowired
    CommentArticleServiceImp commentVideoServiceImp;

    @PostMapping("/upVote")
    public String upVote(@RequestBody UpVoteArticleRequest entity) {
        vUpVoteVideoServiceImpl.upVote(entity);
        
        return "done";
    }
    @PostMapping("/downVote")
    public String downVote(@RequestBody DownVoteArticleRequest entity) {
        downVoteServiceImp.downVote(entity);
        return "done";
    }
    @PostMapping("/deleteUpVote")
    public String deleteUpVote(@RequestParam int articleId,@RequestParam String uname) {
        vUpVoteVideoServiceImpl.deleteUpVote(uname,articleId);
        
        return "done";
    }
    @PostMapping("/deleteDownVote")
    public String deleteDownVote(@RequestParam int articleId,@RequestParam String uname) {
        downVoteServiceImp.deleteDownVote(uname,articleId);
        return "done";
    }
    @PostMapping("/addComment")
    public String addComment(@RequestBody CommentArticleRequest entity) {
        commentVideoServiceImp.addComment(entity);
        return "done";
    }
    @PostMapping("/deleteComment")
    public String deleteComment(@RequestParam int id) {
        commentVideoServiceImp.deleteComment(id);
        return "done";
    }
    @GetMapping("/getAllComment")
    public List<CommentArticleDto> getAllComment(@RequestParam int articleId) {
        return commentVideoServiceImp.getAllComment(articleId);
    }
    @GetMapping("/getAllCommentByUser")
    public List<CommentArticleDto> getAllCommentByUser(@RequestParam String uname) {
        return commentVideoServiceImp.getAllCommentByUser(uname);
    }
}
