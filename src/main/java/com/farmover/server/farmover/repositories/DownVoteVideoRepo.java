package com.farmover.server.farmover.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.farmover.server.farmover.entities.DownVoteVideo;
import com.farmover.server.farmover.entities.User;
import com.farmover.server.farmover.entities.VideoDetail;

public interface DownVoteVideoRepo extends JpaRepository<DownVoteVideo, Integer> {
    List<DownVoteVideo> findByVideoAndUser(VideoDetail video, User user);
}
