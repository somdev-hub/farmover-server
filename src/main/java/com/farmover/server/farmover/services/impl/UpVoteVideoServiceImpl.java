package com.farmover.server.farmover.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.farmover.server.farmover.entities.DownVoteVideo;
import com.farmover.server.farmover.entities.UpVoteVideo;
import com.farmover.server.farmover.entities.User;
import com.farmover.server.farmover.entities.VideoDetail;
import com.farmover.server.farmover.exceptions.ResourceNotFoundException;
import com.farmover.server.farmover.payloads.request.UpVoteVideoRequest;
import com.farmover.server.farmover.repositories.DownVoteVideoRepo;
import com.farmover.server.farmover.repositories.UpVoteVideoRepo;
import com.farmover.server.farmover.repositories.UserRepo;
import com.farmover.server.farmover.repositories.VideoRepo;
import com.farmover.server.farmover.services.UpVoteVideoService;

@Service
public class UpVoteVideoServiceImpl implements UpVoteVideoService {

    @Autowired
    UpVoteVideoRepo uRepo;

    @Autowired
    DownVoteVideoRepo dRepo;

    @Autowired
    VideoRepo videoRepo;

    @Autowired
    UserRepo userRepo;

    @Override
    public void upVote(UpVoteVideoRequest request) {
        VideoDetail videoDetail = videoRepo.findById(request.getVideoId())
                .orElseThrow(() -> new RuntimeException("Video not found"));

        User user = userRepo.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", request.getEmail()));

        List<UpVoteVideo> ls = uRepo.findByVideoAndUser(videoDetail, user);
        List<DownVoteVideo> lss = dRepo.findByVideoAndUser(videoDetail, user);
        if (ls.size() == 0) {
            UpVoteVideo vUpVoteVideo = new UpVoteVideo();
            vUpVoteVideo.setVideo(videoDetail);
            vUpVoteVideo.setUser(user);
            videoDetail.setUpCount(videoDetail.getUpCount() + 1);
            uRepo.save(vUpVoteVideo);
        }
        if (lss.size() != 0) {
            videoDetail.setDownCount(videoDetail.getDownCount() - 1);
            dRepo.deleteById(lss.get(0).getId());
        }
        videoRepo.save(videoDetail);
    }

    @Override
    public void deleteUpVote(String email, Integer videoId) {
        VideoDetail videoDetail = videoRepo.findById(videoId)
                .orElseThrow(() -> new RuntimeException("Video not found"));

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        List<UpVoteVideo> ls = uRepo.findByVideoAndUser(videoDetail, user);
        if (ls.size() != 0) {
            videoDetail.setUpCount(videoDetail.getUpCount() - 1);

            uRepo.deleteById(ls.get(0).getId());
        }
        videoRepo.save(videoDetail);
    }

}
