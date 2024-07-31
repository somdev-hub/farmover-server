package com.farmover.server.farmover.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.farmover.server.farmover.payloads.VideoDto;
import com.farmover.server.farmover.payloads.request.VideoRequestDto;
import com.farmover.server.farmover.services.impl.VideoServiceImp;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@CrossOrigin
@RequestMapping("/videos")
public class VideoController {
    @Autowired
    VideoServiceImp serviceImp;

    @PostMapping(value = "/add-video", consumes = "multipart/form-data")
    public ResponseEntity<String> addVideo(@RequestParam String ownerEmail, @ModelAttribute VideoRequestDto dto) {
        serviceImp.addVideo(ownerEmail, dto);
        return new ResponseEntity<String>("uploaded", HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public VideoDto getVideo(@PathVariable Integer id) {
        return serviceImp.getVideoByid(id);
    }

    @GetMapping("/get-by-email")
    public List<VideoDto> getVideoByAuthor(@RequestParam String ownerEmail) {
        return serviceImp.getVideoByAuthor(ownerEmail);
    }

    @GetMapping("/get-video-by-title")
    public List<VideoDto> getVideoByTitle(@RequestParam String title) {
        return serviceImp.getVideoByTitle(title);
    }

    @DeleteMapping("/{id}")
    public void deleteVideo(@PathVariable Integer id) {
        serviceImp.deleteVideo(id);
    }

}
