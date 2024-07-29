package com.farmover.server.farmover.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.farmover.server.farmover.entities.ArticleDetail;
import com.farmover.server.farmover.entities.CommentArticle;


public interface CommentArticleRepo extends JpaRepository<CommentArticle,Integer>{
    List<CommentArticle> findByUname(String uname);
    List<CommentArticle> findByArticle(ArticleDetail article);
}
