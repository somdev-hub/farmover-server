package com.farmover.server.farmover.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
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
@RequestMapping("/videoAnalytics")
public class VideoAnalyticsAuth {
    @Autowired
    UpVoteVideoServiceImpl vUpVoteVideoServiceImpl;
    @Autowired
    DownVoteServiceImp downVoteServiceImp;
    @Autowired
    CommentVideoServiceImp commentVideoServiceImp;

    @PostMapping("/upVote")
    public String upVote(@RequestBody UpVoteVideoRequest entity) {
        vUpVoteVideoServiceImpl.upVote(entity);

        return "done";
    }

    @PostMapping("/downVote")
    public String downVote(@RequestBody DownVoteRequest entity) {
        downVoteServiceImp.downVote(entity);
        return "done";
    }

    @PostMapping("/deleteUpVote")
    public String deleteUpVote(@RequestParam int videoId, @RequestParam String uname) {
        vUpVoteVideoServiceImpl.deleteUpVote(uname, videoId);

        return "done";
    }

    @PostMapping("/deleteDownVote")
    public String deleteDownVote(@RequestParam int videoId, @RequestParam String uname) {
        downVoteServiceImp.deleteDownVote(uname, videoId);
        return "done";
    }

    @PostMapping("/addComment")
    public String addComment(@RequestBody CommentVideoRequest entity) {
        commentVideoServiceImp.addComment(entity);
        return "done";
    }

    @PostMapping("/deleteComment")
    public String deleteComment(@RequestParam int id) {
        commentVideoServiceImp.deleteComment(id);
        return "done";
    }

    @GetMapping("/getAllComment")
    public List<CommentVideoDto> getAllComment(@RequestParam int videoId) {
        return commentVideoServiceImp.getAllComment(videoId);
    }

    @GetMapping("/getAllCommentByUser")
    public List<CommentVideoDto> getAllCommentByUser(@RequestParam String uname) {
        return commentVideoServiceImp.getAllCommentByUser(uname);
    }

}
