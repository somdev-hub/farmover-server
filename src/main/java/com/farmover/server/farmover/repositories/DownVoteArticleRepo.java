package com.farmover.server.farmover.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.farmover.server.farmover.entities.ArticleDetail;
import com.farmover.server.farmover.entities.DownVoteArticle;
import com.farmover.server.farmover.entities.User;

public interface DownVoteArticleRepo extends JpaRepository<DownVoteArticle, Integer> {

    List<DownVoteArticle> findByArticleAndUser(ArticleDetail article, User user);
}
