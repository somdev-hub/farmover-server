package com.farmover.server.farmover.services.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.farmover.server.farmover.entities.DownVoteVideo;
import com.farmover.server.farmover.entities.UpVoteVideo;
import com.farmover.server.farmover.entities.VideoDetail;
import com.farmover.server.farmover.payloads.request.UpVoteVideoRequest;
import com.farmover.server.farmover.repositories.DownVoteVideoRepo;
import com.farmover.server.farmover.repositories.UpVoteVideoRepo;
import com.farmover.server.farmover.repositories.VideoRepo;
import com.farmover.server.farmover.services.UpVoteVideoService;

@Service
public class UpVoteVideoServiceImpl implements UpVoteVideoService{
    @Autowired 
    UpVoteVideoRepo uRepo;
    @Autowired
    DownVoteVideoRepo dRepo;
    @Autowired
    VideoRepo videoRepo;
    @Override
    public void upVote(UpVoteVideoRequest request) {
        VideoDetail  videoDetail = videoRepo.findById(request.getVideoId()).orElseThrow(() -> new RuntimeException("Video not found"));
        List<UpVoteVideo> ls = uRepo.findByVideoAndUname(videoDetail, request.getUname());
        List<DownVoteVideo> lss = dRepo.findByVideoAndUname(videoDetail, request.getUname());
        if(ls.size()==0){
            UpVoteVideo vUpVoteVideo = new UpVoteVideo();
            vUpVoteVideo.setVideo(videoDetail);
            vUpVoteVideo.setUname(request.getUname());
            videoDetail.setUpCount(videoDetail.getUpCount()+1);
            uRepo.save(vUpVoteVideo);
        }
        if(lss.size()!=0){
            videoDetail.setDownCount(videoDetail.getDownCount()-1);
            dRepo.deleteById(lss.get(0).getId());
        }
        videoRepo.save(videoDetail);
    }

    @Override
    public void deleteUpVote(String uname, int videoId) {
        VideoDetail  videoDetail = videoRepo.findById(videoId).orElseThrow(() -> new RuntimeException("Video not found"));
        List<UpVoteVideo> ls = uRepo.findByVideoAndUname(videoDetail, uname);
        if(ls.size()!=0){
            videoDetail.setUpCount(videoDetail.getUpCount()-1);
            
            uRepo.deleteById(ls.get(0).getId());
        }
        videoRepo.save(videoDetail);
    }
    
}
