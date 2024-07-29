package com.farmover.server.farmover.services.impl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.farmover.server.farmover.entities.User;
import com.farmover.server.farmover.entities.VideoDetail;
import com.farmover.server.farmover.payloads.VideoDto;
import com.farmover.server.farmover.payloads.request.VideoRequestDto;
import com.farmover.server.farmover.repositories.UserRepo;
import com.farmover.server.farmover.repositories.VideoRepo;
import com.farmover.server.farmover.services.VideoService;
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
    @Override
    public void addVideo(String userEmail, VideoRequestDto video) {
         User owner = userRepo.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        String url ="";
        String thumbnail ="";
        try {
            url =s3ServiceImpl.uploadFile(video.getVideo());
            thumbnail = s3ServiceImpl.uploadFile(video.getThumbnail());
        } catch (IOException e) {
            throw new RuntimeException("Video not found");
        }
        VideoDetail videoDetail = new VideoDetail();
        videoDetail.setAuthor(owner);
        videoDetail.setUrl(url);
        videoDetail.setDescription(video.getDescription());
        videoDetail.setTitle(video.getTitle());
        videoDetail.setThumbnail(thumbnail);
        videoDetail.setLongDescription(video.getLongDescription());
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");  
        LocalDateTime now = LocalDateTime.now();
        videoDetail.setDate( dtf.format(now) +"" );
        videoRepo.save(videoDetail);
    }

    @Override
    public List<VideoDto> getVideoByAuthor(String ownerEmail) {
        User owner = userRepo.findByEmail(ownerEmail)
        .orElseThrow(() -> new RuntimeException("User not found"));
        List<VideoDetail> list = videoRepo.findByAuthor(owner);
        List<VideoDto> dtos = new ArrayList<>(); 
        for(VideoDetail detail : list){
            VideoDto dto = modelMapper.map(detail, VideoDto.class);
            dto.setAuthorName(owner.getUname());
            dtos.add(dto);
        }
        return dtos;
    }
    public List<VideoDto> getVideoByTitle(String title){
        List<VideoDetail> list = videoRepo.findByTitle(title) .orElseThrow(() -> new RuntimeException("Vid not found"));;
        List<VideoDto> dtos = new ArrayList<>(); 
        for(VideoDetail detail : list){
            VideoDto dto = modelMapper.map(detail, VideoDto.class);
            dto.setAuthorName(detail.getAuthor().getUname());
            dtos.add(dto);
        }
        return dtos;
    }

    @Override
    public VideoDto getVideoByid(Integer id) {
        VideoDetail vDetail = videoRepo.findById(id).orElseThrow(() -> new RuntimeException("Video not found"));
        String name=vDetail.getAuthor().getUname();
        VideoDto dto = modelMapper.map(vDetail, VideoDto.class);
        dto.setAuthorName(name);
        return dto;

    }

    @Override
    public void deleteVideo(Integer id) {
        videoRepo.deleteById(id);
    }
    
}
