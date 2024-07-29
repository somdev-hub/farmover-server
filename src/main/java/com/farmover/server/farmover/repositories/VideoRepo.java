package com.farmover.server.farmover.repositories;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.farmover.server.farmover.entities.User;
import com.farmover.server.farmover.entities.VideoDetail;

public interface VideoRepo extends JpaRepository<VideoDetail,Integer>{
    ArrayList<VideoDetail> findByAuthor(User user);
    Optional<ArrayList<VideoDetail>> findByTitle(String title);
}
