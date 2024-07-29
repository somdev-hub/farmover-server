package com.farmover.server.farmover.services.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
import com.farmover.server.farmover.services.CommentArticleService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CommentArticleServiceImp implements CommentArticleService{
    @Autowired
    ArticleRepo articleRepo;
    @Autowired
    CommentArticleRepo repo;
    @Autowired
    ModelMapper modelMapper;
    @Override
    public void addComment(CommentArticleRequest request) {
        ArticleDetail aDetail = articleRepo.findById(request.getArticleId()).orElseThrow(() -> new RuntimeException("Video not found"));
        CommentArticle comment = new CommentArticle();
        comment.setComment(request.getComment());
        comment.setUname(request.getUname());
        comment.setArticle(aDetail);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");  
        LocalDateTime now = LocalDateTime.now();
        comment.setDate( dtf.format(now) +"");
        aDetail.setCommnetCount(aDetail.getCommnetCount()+1);
        repo.save(comment);
        articleRepo.save(aDetail);
    }
    @Override
    public List<CommentArticleDto> getAllComment(Integer articleId) {
         ArticleDetail aDetail = articleRepo.findById(articleId).orElseThrow(() -> new RuntimeException("Video not found"));
         List<CommentArticle> comments = repo.findByArticle(aDetail);
         List<CommentArticleDto> dtos = new ArrayList<>();
         for(CommentArticle comment : comments){
            CommentArticleDto dto = modelMapper.map(comment, CommentArticleDto.class);
            dtos.add(dto);
         }
         return dtos;     
    }

    @Override
    public List<CommentArticleDto> getAllCommentByUser(String uname) {
       
        List<CommentArticle> comments = repo.findByUname(uname);
        List<CommentArticleDto> dtos = new ArrayList<>();
        for(CommentArticle comment : comments){
            CommentArticleDto dto = modelMapper.map(comment, CommentArticleDto.class);
           dtos.add(dto);
        }
        return dtos;   
    }

    @Override
    public void deleteComment(Integer id) {
        CommentArticle comment = repo.findById(id).orElseThrow(() -> new RuntimeException("Comment not found"));
        ArticleDetail articleDetail = comment.getArticle();
        articleDetail.setCommnetCount(articleDetail.getCommnetCount()-1);
        articleRepo.save(articleDetail);
        repo.deleteById(id);
    }
    
}
