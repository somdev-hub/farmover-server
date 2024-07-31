package com.farmover.server.farmover.services;

import java.io.IOException;
import java.util.List;

import com.farmover.server.farmover.payloads.VideoDto;
import com.farmover.server.farmover.payloads.request.VideoRequestDto;

public interface VideoService {
        void addVideo(String userEmail, VideoRequestDto video) throws IOException;

        List<VideoDto> getVideoByAuthor(String ownerEmail);

        VideoDto getVideoByid(Integer id);

        void deleteVideo(Integer id);
}
