package com.farmover.server.farmover.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.farmover.server.farmover.payloads.CommentVideoDto;
import com.farmover.server.farmover.payloads.request.CommentVideoRequest;
import com.farmover.server.farmover.payloads.request.DownVoteRequest;
import com.farmover.server.farmover.payloads.request.UpVoteVideoRequest;
import com.farmover.server.farmover.services.impl.CommentVideoServiceImp;
import com.farmover.server.farmover.services.impl.DownVoteServiceImp;
import com.farmover.server.farmover.services.impl.UpVoteVideoServiceImpl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@CrossOrigin
@RequestMapping("/video-analytics")
public class VideoAnalyticsAuth {
    @Autowired
    UpVoteVideoServiceImpl vUpVoteVideoServiceImpl;
    @Autowired
    DownVoteServiceImp downVoteServiceImp;
    @Autowired
    CommentVideoServiceImp commentVideoServiceImp;

    @PostMapping("/up-vote")
    public String upVote(@RequestBody UpVoteVideoRequest entity) {
        vUpVoteVideoServiceImpl.upVote(entity);

        return "done";
    }

    @PostMapping("/down-vote")
    public String downVote(@RequestBody DownVoteRequest entity) {
        downVoteServiceImp.downVote(entity);
        return "done";
    }

    @DeleteMapping("/delete-up-vote")
    public String deleteUpVote(@RequestParam int videoId, @RequestParam String email) {
        vUpVoteVideoServiceImpl.deleteUpVote(email, videoId);

        return "done";
    }

    @DeleteMapping("/delete-down-vote")
    public String deleteDownVote(@RequestParam int videoId, @RequestParam String email) {
        downVoteServiceImp.deleteDownVote(email, videoId);
        return "done";
    }

    @PostMapping("/add-comment")
    public String addComment(@RequestBody CommentVideoRequest entity) {
        commentVideoServiceImp.addComment(entity);
        return "done";
    }

    @DeleteMapping("/delete-comment")
    public String deleteComment(@RequestParam int id) {
        commentVideoServiceImp.deleteComment(id);
        return "done";
    }

    @GetMapping("/get-all-comment")
    public List<CommentVideoDto> getAllComment(@RequestParam int videoId) {
        return commentVideoServiceImp.getAllComment(videoId);
    }

    @GetMapping("/get-all-comment-by-user")
    public List<CommentVideoDto> getAllCommentByUser(@RequestParam String email) {
        return commentVideoServiceImp.getAllCommentByUser(email);
    }

}
