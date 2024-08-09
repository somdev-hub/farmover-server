package com.farmover.server.farmover.services.impl;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.farmover.server.farmover.entities.CommentVideo;
import com.farmover.server.farmover.entities.DownVoteVideo;
import com.farmover.server.farmover.entities.UpVoteVideo;
import com.farmover.server.farmover.entities.User;
import com.farmover.server.farmover.entities.VideoDetail;
import com.farmover.server.farmover.entities.VideoViews;
import com.farmover.server.farmover.exceptions.ResourceNotFoundException;
import com.farmover.server.farmover.payloads.VideoDto;
import com.farmover.server.farmover.payloads.records.VideoCommentRecord;
import com.farmover.server.farmover.payloads.records.VideoUserResponseRecord;
import com.farmover.server.farmover.payloads.request.VideoRequestDto;
import com.farmover.server.farmover.repositories.CommentVideoRepo;
import com.farmover.server.farmover.repositories.UserRepo;
import com.farmover.server.farmover.repositories.VideoRepo;
import com.farmover.server.farmover.services.VideoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class VideoServiceImp implements VideoService {

    @Autowired
    VideoRepo videoRepo;

    @Autowired
    S3ServiceImpl s3ServiceImpl;

    @Autowired
    UserRepo userRepo;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    CommentVideoRepo commentVideoRepo;

    @Override
    public void addVideo(String userEmail, VideoRequestDto video) throws JsonMappingException, JsonProcessingException {
        User owner = userRepo.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String url = "";

        String thumbnail = "";

        try {
            url = s3ServiceImpl.uploadFile(video.getVideo());
            thumbnail = s3ServiceImpl.uploadFile(video.getThumbnail());
        } catch (IOException e) {
            throw new RuntimeException("Video upload error " + e);
        }

        VideoDetail videoDetail = new VideoDetail();

        videoDetail.setAuthor(owner);
        videoDetail.setUrl(url);
        videoDetail.setDescription(video.getDescription());
        videoDetail.setTitle(video.getTitle());
        videoDetail.setThumbnail(thumbnail);
        videoDetail.setLongDescription(video.getLongDescription());

        List<String> tags = objectMapper.readValue(video.getTags(), new TypeReference<List<String>>() {
        });
        videoDetail.setTags(tags);

        videoDetail.setUpVoteVideo(new ArrayList<>());
        videoDetail.setDownVoteVideo(new ArrayList<>());
        videoDetail.setVideoComment(new ArrayList<>());
        videoDetail.setViews(new ArrayList<>());

        videoDetail.setDate(new Date(System.currentTimeMillis()));

        videoRepo.save(videoDetail);
    }

    @Override
    public List<VideoDto> getVideoByAuthor(String ownerEmail) {
        User owner = userRepo.findByEmail(ownerEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<VideoDetail> list = videoRepo.findByAuthor(owner);
        List<VideoDto> dtos = new ArrayList<>();
        for (VideoDetail detail : list) {
            VideoDto dto = modelMapper.map(detail, VideoDto.class);
            dto.setAuthorName(owner.getUname());
            dtos.add(dto);
        }
        return dtos;
    }

    public List<VideoDto> getVideoByTitle(String title) {
        List<VideoDetail> list = videoRepo.findByTitle(title).orElseThrow(() -> new RuntimeException("Vid not found"));
        ;
        List<VideoDto> dtos = new ArrayList<>();
        for (VideoDetail detail : list) {
            VideoDto dto = modelMapper.map(detail, VideoDto.class);
            dto.setAuthorName(detail.getAuthor().getUname());
            dtos.add(dto);
        }
        return dtos;
    }

    @Override
    public VideoDto getVideoByid(Integer id) {
        VideoDetail vDetail = videoRepo.findById(id).orElseThrow(() -> new RuntimeException("Video not found"));
        String name = vDetail.getAuthor().getUname();
        String userProfileImage = vDetail.getAuthor().getProfileImage();
        List<String> tags = vDetail.getTags();
        VideoDto dto = modelMapper.map(vDetail, VideoDto.class);
        dto.setAuthorName(name);
        dto.setAuthorProfile(userProfileImage);
        dto.setTags(tags);
        return dto;

    }

    @Override
    public void deleteVideo(Integer id) {
        videoRepo.deleteById(id);
    }

    @Override
    public Page<VideoCommentRecord> getVideoComments(Integer id, Integer page, Integer size) {
        VideoDetail video = videoRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Video", "id", id.toString()));

        Pageable pageable = PageRequest.of(page, size);
        Page<CommentVideo> commentPage = commentVideoRepo.findByVideo(video, pageable);

        return commentPage.map(comment -> {
            User user = userRepo.findByEmail(comment.getEmail())
                    .orElseThrow(() -> new ResourceNotFoundException("User", "email", comment.getEmail()));
            return new VideoCommentRecord(
                    comment.getId(),
                    user.getUname(),
                    user.getProfileImage(),
                    comment.getComment(),
                    comment.getDate());
        });

    }

    @Override
    public Page<VideoUserResponseRecord> getVideos(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<VideoDetail> videoPage = videoRepo.findAll(pageable);

        return videoPage.map(video -> {
            User user = video.getAuthor();
            return new VideoUserResponseRecord(
                    video.getId(),
                    video.getThumbnail(),
                    video.getTitle(),
                    user.getUname(),
                    user.getProfileImage(),
                    video.getDescription());
        });
    }

    @Override
    public void addViewToVideo(Integer id, String email) {
        VideoDetail video = videoRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Video", "id", id.toString()));

        if (video.getViews().stream().noneMatch(view -> view.getViewerEmail().equals(email))) {
            video.setViewCount(video.getViewCount() + 1);
            VideoViews view = new VideoViews();
            view.setVideo(video);
            view.setViewerEmail(email);
            view.setDate(new Date(System.currentTimeMillis()));
            video.getViews().add(view);
        }

        videoRepo.save(video);
    }

    @Override
    public void upVoteVideo(Integer id, String email) {
        VideoDetail video = videoRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Video", "id", id.toString()));

        if (video.getUpVoteVideo().stream().noneMatch(upVote -> upVote.getEmail().equals(email))) {
            video.setUpCount(video.getUpCount() + 1);
            UpVoteVideo upVote = new UpVoteVideo();
            upVote.setVideo(video);
            upVote.setEmail(email);
            video.getUpVoteVideo().add(upVote);
        } else {
            video.setUpCount(video.getUpCount() - 1);
            video.getUpVoteVideo().removeIf(upVote -> upVote.getEmail().equals(email));
        }

        videoRepo.save(video);
    }

    @Override
    public void downVoteVideo(Integer id, String email) {
        VideoDetail video = videoRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Video", "id", id.toString()));

        if (video.getDownVoteVideo().stream().noneMatch(downVote -> downVote.getEmail().equals(email))) {
            video.setDownCount(video.getDownCount() + 1);
            DownVoteVideo downVote = new DownVoteVideo();
            downVote.setVideo(video);
            downVote.setEmail(email);
            video.getDownVoteVideo().add(downVote);
        } else {
            video.setDownCount(video.getDownCount() - 1);
            video.getDownVoteVideo().removeIf(downVote -> downVote.getEmail().equals(email));
        }

        videoRepo.save(video);
    }

    @Override
    public void addCommentToVideo(Integer id, String email, String comment) {
        VideoDetail video = videoRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Video", "id", id.toString()));

        CommentVideo commentVideo = new CommentVideo();
        commentVideo.setVideo(video);
        commentVideo.setEmail(email);
        commentVideo.setComment(comment);
        commentVideo.setDate(new Date(System.currentTimeMillis()));

        video.getVideoComment().add(commentVideo);
        video.setCommentCount(
                video.getCommentCount() + 1);

        videoRepo.save(video);
    }

    @Override
    public void editVideo(Integer id, VideoRequestDto video) throws IOException {
        VideoDetail videoDetail = videoRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Video", "id", id.toString()));

        String url = "";
        String thumbnail = "";

        try {
            if (video.getVideo() != null) {
                if (videoDetail.getUrl() != null) {
                    Boolean deleted = s3ServiceImpl.deleteFile(videoDetail.getUrl());
                }
                url = s3ServiceImpl.uploadFile(video.getVideo());
                videoDetail.setUrl(url);
            }
            if (video.getThumbnail() != null) {
                if (videoDetail.getThumbnail() != null) {
                    Boolean deleted = s3ServiceImpl.deleteFile(videoDetail.getThumbnail());
                }
                thumbnail = s3ServiceImpl.uploadFile(video.getThumbnail());
                videoDetail.setThumbnail(thumbnail);
            }

        } catch (IOException e) {
            throw new RuntimeException("Video upload error " + e);
        }

        videoDetail.setDescription(video.getDescription());
        videoDetail.setTitle(video.getTitle());
        videoDetail.setLongDescription(video.getLongDescription());

        List<String> tags = objectMapper.readValue(video.getTags(), new TypeReference<List<String>>() {
        });
        videoDetail.setTags(tags);

        videoRepo.save(videoDetail);
    }

}
