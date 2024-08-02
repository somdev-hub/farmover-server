package com.farmover.server.farmover.services;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;

import com.farmover.server.farmover.payloads.VideoDto;
import com.farmover.server.farmover.payloads.records.VideoCommentRecord;
import com.farmover.server.farmover.payloads.records.VideoUserResponseRecord;
import com.farmover.server.farmover.payloads.request.VideoRequestDto;

public interface VideoService {
        void addVideo(String userEmail, VideoRequestDto video) throws IOException;

        List<VideoDto> getVideoByAuthor(String ownerEmail);

        VideoDto getVideoByid(Integer id);

        void deleteVideo(Integer id);

        Page<VideoCommentRecord> getVideoComments(Integer id, Integer page, Integer size);

        Page<VideoUserResponseRecord> getVideos(Integer page, Integer size);

        void addViewToVideo(Integer id, String email);

        void upVoteVideo(Integer id, String email);

        void downVoteVideo(Integer id, String email);

        void addCommentToVideo(Integer id, String email, String comment);
}
