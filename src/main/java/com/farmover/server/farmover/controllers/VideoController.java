package com.farmover.server.farmover.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.farmover.server.farmover.payloads.VideoDto;
import com.farmover.server.farmover.payloads.records.VideoCommentRecord;
import com.farmover.server.farmover.payloads.records.VideoUserResponseRecord;
import com.farmover.server.farmover.payloads.request.CommentVideoRequest;
import com.farmover.server.farmover.payloads.request.VideoRequestDto;
import com.farmover.server.farmover.services.impl.VideoServiceImp;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@CrossOrigin
@RequestMapping("/videos")
public class VideoController {

    @Autowired
    VideoServiceImp serviceImp;

    @PostMapping(value = "/", consumes = "multipart/form-data")
    public ResponseEntity<String> addVideo(@RequestParam String ownerEmail, @ModelAttribute VideoRequestDto dto)
            throws JsonMappingException, JsonProcessingException {
        serviceImp.addVideo(ownerEmail, dto);
        return new ResponseEntity<String>("uploaded", HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public VideoDto getVideo(@PathVariable Integer id) {
        return serviceImp.getVideoByid(id);
    }

    @GetMapping("/get-by-email")
    public List<VideoDto> getVideoByAuthor(@RequestParam String ownerEmail) {
        return serviceImp.getVideoByAuthor(ownerEmail);
    }

    @GetMapping("/get-by-title")
    public List<VideoDto> getVideoByTitle(@RequestParam String title) {
        return serviceImp.getVideoByTitle(title);
    }

    @DeleteMapping("/{id}")
    public void deleteVideo(@PathVariable Integer id) {
        serviceImp.deleteVideo(id);
    }

    @GetMapping("/comments/{id}")
    public ResponseEntity<Page<VideoCommentRecord>> getVideoComments(@PathVariable Integer id,
            @RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer size) {
        return new ResponseEntity<Page<VideoCommentRecord>>(serviceImp.getVideoComments(id, page, size), HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<Page<VideoUserResponseRecord>> getVideos(@RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return new ResponseEntity<Page<VideoUserResponseRecord>>(serviceImp.getVideos(page, size), HttpStatus.OK);
    }

    @PutMapping("/view/{id}")
    public ResponseEntity<String> addView(@PathVariable Integer id, @RequestParam String email) {
        serviceImp.addViewToVideo(id, email);
        return new ResponseEntity<String>("view added", HttpStatus.OK);
    }

    @PutMapping("/upvote/{id}")
    public ResponseEntity<String> upVote(@PathVariable Integer id, @RequestParam String email) {
        serviceImp.upVoteVideo(id, email);
        return new ResponseEntity<String>("upvote updated", HttpStatus.OK);
    }

    @PutMapping("/downvote/{id}")
    public ResponseEntity<String> downVote(@PathVariable Integer id, @RequestParam String email) {
        serviceImp.downVoteVideo(id, email);
        return new ResponseEntity<String>("downvote updated", HttpStatus.OK);
    }

    @PostMapping("/comment/{id}")
    public ResponseEntity<String> addComment(@PathVariable Integer id,
            @RequestBody CommentVideoRequest comment) {
        serviceImp.addCommentToVideo(id, comment.getEmail(), comment.getComment());
        return new ResponseEntity<String>("comment added", HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> editVideo(@PathVariable Integer id, @ModelAttribute VideoRequestDto dto)
            throws IOException {

        serviceImp.editVideo(id, dto);
        return new ResponseEntity<String>("video updated", HttpStatus.ACCEPTED);
    }

}
