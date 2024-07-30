package com.farmover.server.farmover.services.impl;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.farmover.server.farmover.entities.CommentVideo;
import com.farmover.server.farmover.entities.User;
import com.farmover.server.farmover.entities.VideoDetail;
import com.farmover.server.farmover.exceptions.ResourceNotFoundException;
import com.farmover.server.farmover.payloads.CommentVideoDto;
import com.farmover.server.farmover.payloads.request.CommentVideoRequest;
import com.farmover.server.farmover.repositories.CommentVideoRepo;
import com.farmover.server.farmover.repositories.UserRepo;
import com.farmover.server.farmover.repositories.VideoRepo;
import com.farmover.server.farmover.services.CommentVideoService;

@Service
public class CommentVideoServiceImp implements CommentVideoService {

    @Autowired
    VideoRepo videoRepo;

    @Autowired
    CommentVideoRepo repo;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    UserRepo userRepo;

    @Override
    public List<CommentVideoDto> getAllComment(Integer videoId) {
        VideoDetail vDetail = videoRepo.findById(videoId).orElseThrow(() -> new RuntimeException("Video not found"));
        List<CommentVideo> comments = repo.findByVideo(vDetail);
        List<CommentVideoDto> dtos = new ArrayList<>();
        for (CommentVideo comment : comments) {
            CommentVideoDto dto = modelMapper.map(comment, CommentVideoDto.class);
            dtos.add(dto);
        }
        return dtos;
    }

    @Override
    public List<CommentVideoDto> getAllCommentByUser(String email) {

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        List<CommentVideo> comments = repo.findByEmail(email);

        List<CommentVideoDto> dtos = comments.stream().map(comment -> {
            CommentVideoDto dto = modelMapper.map(comment, CommentVideoDto.class);
            return dto;
        }).toList();

        return dtos;
    }

    @Override
    public void deleteComment(Integer id) {
        CommentVideo comment = repo.findById(id).orElseThrow(() -> new RuntimeException("Comment not found"));
        VideoDetail videoDetail = comment.getVideo();
        videoDetail.setCommentCount(videoDetail.getCommentCount() - 1);
        videoRepo.save(videoDetail);
        repo.deleteById(id);
    }

    @Override
    public void addComment(CommentVideoRequest request) {
       

        VideoDetail vDetail = videoRepo.findById(request.getVideoId())
                .orElseThrow(() -> new RuntimeException("Video not found"));
        CommentVideo comment = new CommentVideo();
        comment.setComment(request.getComment());
        comment.setEmail(request.getEmail());
        comment.setVideo(vDetail);

        comment.setDate(new Date(System.currentTimeMillis()));
        vDetail.setCommentCount(vDetail.getCommentCount() + 1);
        repo.save(comment);
        videoRepo.save(vDetail);

    }

}
