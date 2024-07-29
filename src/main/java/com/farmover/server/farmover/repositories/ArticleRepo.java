package com.farmover.server.farmover.repositories;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.farmover.server.farmover.entities.ArticleDetail;
import com.farmover.server.farmover.entities.User;


public interface ArticleRepo extends JpaRepository<ArticleDetail,Integer>{
    ArrayList<ArticleDetail> findByAuthor(User user);
    Optional<ArrayList<ArticleDetail>> findByTitle(String title);
}
