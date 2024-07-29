package com.farmover.server.farmover.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.farmover.server.farmover.entities.ArticleDetail;
import com.farmover.server.farmover.entities.DownVoteArticle;

public interface DownVoteArticleRepo extends JpaRepository<DownVoteArticle,Integer>{
    List<DownVoteArticle> findByArticleAndUname(ArticleDetail article, String uname);
}
