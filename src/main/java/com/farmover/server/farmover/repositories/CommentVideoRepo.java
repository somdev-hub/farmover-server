package com.farmover.server.farmover.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.farmover.server.farmover.entities.CommentVideo;
import com.farmover.server.farmover.entities.VideoDetail;

public interface CommentVideoRepo extends JpaRepository<CommentVideo,Integer>{
    List<CommentVideo> findByUname(String uname);
    List<CommentVideo> findByVideo(VideoDetail video);

}
