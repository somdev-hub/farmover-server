package com.farmover.server.farmover.services;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;

import com.farmover.server.farmover.payloads.ArticleDto;
import com.farmover.server.farmover.payloads.records.VideoUserResponseRecord;
import com.farmover.server.farmover.payloads.request.ArticleRequest;

public interface ArticleService {
    void addArticle(String userEmail, ArticleRequest article) throws IOException;

    void editArticle(Integer id, ArticleRequest article) throws IOException;

    List<ArticleDto> getArticleByAuthor(String ownerEmail);

    ArticleDto getArticleByid(Integer id, String email);

    void deleteArticle(Integer id);

    Page<VideoUserResponseRecord> getArticles(Integer page, Integer size);

    String upVoteArticle(Integer id, String email);

    String downVoteArticle(Integer id, String email);

    void addCommentToArticle(Integer id, String email, String comment);

    void addViewToArticle(Integer id, String email);
}
