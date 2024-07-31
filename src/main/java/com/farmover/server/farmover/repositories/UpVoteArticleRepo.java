package com.farmover.server.farmover.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.farmover.server.farmover.entities.ArticleDetail;
import com.farmover.server.farmover.entities.UpVoteArticle;

public interface UpVoteArticleRepo extends JpaRepository<UpVoteArticle, Integer> {

     List<UpVoteArticle> findByArticleAndEmail(ArticleDetail article, String email);
}
