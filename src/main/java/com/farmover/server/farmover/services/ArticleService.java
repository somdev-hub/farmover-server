package com.farmover.server.farmover.services;

import java.io.IOException;
import java.util.List;

import com.farmover.server.farmover.payloads.ArticleDto;
import com.farmover.server.farmover.payloads.request.ArticleRequest;

public interface ArticleService {
    void addArticle(String userEmail, ArticleRequest article) throws IOException;

    List<ArticleDto> getArticleByAuthor(String ownerEmail);

    ArticleDto getArticleByid(Integer id);

    void deleteArticle(Integer id);
}
