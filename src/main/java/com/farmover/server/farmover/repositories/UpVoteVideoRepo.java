package com.farmover.server.farmover.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.farmover.server.farmover.entities.UpVoteVideo;
import com.farmover.server.farmover.entities.User;
import com.farmover.server.farmover.entities.VideoDetail;

public interface UpVoteVideoRepo extends JpaRepository<UpVoteVideo, Integer> {

    List<UpVoteVideo> findByVideoAndUser(VideoDetail video, User user);
}
