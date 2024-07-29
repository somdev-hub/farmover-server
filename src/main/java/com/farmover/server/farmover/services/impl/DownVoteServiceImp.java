package com.farmover.server.farmover.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.farmover.server.farmover.entities.DownVoteVideo;
import com.farmover.server.farmover.entities.UpVoteVideo;
import com.farmover.server.farmover.entities.VideoDetail;
import com.farmover.server.farmover.payloads.request.DownVoteRequest;
import com.farmover.server.farmover.repositories.DownVoteVideoRepo;
import com.farmover.server.farmover.repositories.UpVoteVideoRepo;
import com.farmover.server.farmover.repositories.VideoRepo;
import com.farmover.server.farmover.services.DownVoteVideoService;
@Service
public class DownVoteServiceImp implements DownVoteVideoService{
    @Autowired 
    UpVoteVideoRepo uRepo;
    @Autowired
    DownVoteVideoRepo dRepo;
    @Autowired
    VideoRepo videoRepo;
    @Override
    public void downVote(DownVoteRequest request) {
         VideoDetail  videoDetail = videoRepo.findById(request.getVideoId()).orElseThrow(() -> new RuntimeException("Video not found"));
        List<UpVoteVideo> ls = uRepo.findByVideoAndUname(videoDetail, request.getUname());
        List<DownVoteVideo> lss = dRepo.findByVideoAndUname(videoDetail, request.getUname());
        if(lss.size()==0){
            DownVoteVideo downVoteVideo = new DownVoteVideo();
            downVoteVideo.setVideo(videoDetail);
            downVoteVideo.setUname(request.getUname());
            videoDetail.setDownCount(videoDetail.getDownCount()+1);
            dRepo.save(downVoteVideo);
        }
        if(ls.size()!=0){
            videoDetail.setUpCount(videoDetail.getUpCount()-1);
            uRepo.deleteById(ls.get(0).getId());
        }
        videoRepo.save(videoDetail);
    }

    @Override
    public void deleteDownVote(String uname, int videoId) {
        VideoDetail  videoDetail = videoRepo.findById(videoId).orElseThrow(() -> new RuntimeException("Video not found"));
        List<DownVoteVideo> lss = dRepo.findByVideoAndUname(videoDetail, uname);
        if(lss.size()!=0){
            dRepo.deleteById(lss.get(0).getId());
            videoDetail.setDownCount(videoDetail.getDownCount()-1);
        }
        videoRepo.save(videoDetail);
    }
    
}
