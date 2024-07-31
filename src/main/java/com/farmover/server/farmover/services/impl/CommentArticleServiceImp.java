package com.farmover.server.farmover.services.impl;

import java.sql.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.farmover.server.farmover.entities.ArticleDetail;
import com.farmover.server.farmover.entities.CommentArticle;
import com.farmover.server.farmover.payloads.CommentArticleDto;
import com.farmover.server.farmover.payloads.request.CommentArticleRequest;
import com.farmover.server.farmover.repositories.ArticleRepo;
import com.farmover.server.farmover.repositories.CommentArticleRepo;
import com.farmover.server.farmover.repositories.UserRepo;
import com.farmover.server.farmover.services.CommentArticleService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CommentArticleServiceImp implements CommentArticleService {

    @Autowired
    ArticleRepo articleRepo;

    @Autowired
    CommentArticleRepo repo;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    UserRepo userRepo;

    @Override
    public void addComment(CommentArticleRequest request) {
        ArticleDetail aDetail = articleRepo.findById(request.getArticleId())
                .orElseThrow(() -> new RuntimeException("Video not found"));

        CommentArticle comment = new CommentArticle();
        comment.setComment(request.getComment());
        comment.setEmail(request.getEmail());
        comment.setArticle(aDetail);

        comment.setDate(new Date(System.currentTimeMillis()));
        aDetail.setCommnetCount(aDetail.getCommnetCount() + 1);
        repo.save(comment);
        articleRepo.save(aDetail);
    }

    @Override
    public List<CommentArticleDto> getAllComment(Integer articleId) {
        ArticleDetail aDetail = articleRepo.findById(articleId)
                .orElseThrow(() -> new RuntimeException("Video not found"));
        List<CommentArticle> comments = repo.findByArticle(aDetail);
        List<CommentArticleDto> dtos = comments.stream().map(comment -> {
            CommentArticleDto dto = modelMapper.map(comment, CommentArticleDto.class);
            return dto;
        }).toList();
        return dtos;
    }

    @Override
    public List<CommentArticleDto> getAllCommentByUser(String email) {

        List<CommentArticle> comments = repo.findByEmail(email);
        List<CommentArticleDto> dtos = comments.stream().map(comment -> {
            CommentArticleDto dto = modelMapper.map(comment, CommentArticleDto.class);
            return dto;
        }).toList();
        return dtos;
    }

    @Override
    public void deleteComment(Integer id) {
        CommentArticle comment = repo.findById(id).orElseThrow(() -> new RuntimeException("Comment not found"));
        ArticleDetail articleDetail = comment.getArticle();
        articleDetail.setCommnetCount(articleDetail.getCommnetCount() - 1);
        articleRepo.save(articleDetail);
        repo.deleteById(id);
    }

}
