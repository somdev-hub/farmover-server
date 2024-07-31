package com.farmover.server.farmover.services.impl;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.farmover.server.farmover.entities.ArticleDetail;
import com.farmover.server.farmover.entities.User;
import com.farmover.server.farmover.payloads.ArticleDto;
import com.farmover.server.farmover.payloads.request.ArticleRequest;
import com.farmover.server.farmover.repositories.ArticleRepo;
import com.farmover.server.farmover.repositories.UserRepo;
import com.farmover.server.farmover.services.ArticleService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    ArticleRepo repo;
    @Autowired
    S3ServiceImpl s3ServiceImpl;
    @Autowired
    UserRepo userRepo;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    ObjectMapper objectMapper;

    @Override
    public void addArticle(String userEmail, ArticleRequest article)
            throws JsonMappingException, JsonProcessingException {
        User owner = userRepo.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String thumbnail = "";
        try {
            thumbnail = s3ServiceImpl.uploadFile(article.getThumbnail());
        } catch (IOException e) {
            throw new RuntimeException("Video not found");
        }
        ArticleDetail articleDetail = new ArticleDetail();
        List<String> tags = objectMapper.readValue(article.getTags(), new TypeReference<List<String>>() {
        });
        articleDetail.setTags(tags);
        articleDetail.setSubHeading(article.getSubHeading());
        articleDetail.setAuthor(owner);
        articleDetail.setTitle(article.getTitle());
        articleDetail.setThumbnail(thumbnail);
        articleDetail.setContent(article.getContent());

        articleDetail.setDate(new Date(System.currentTimeMillis()));
        repo.save(articleDetail);
    }

    @Override
    public List<ArticleDto> getArticleByAuthor(String ownerEmail) {
        User owner = userRepo.findByEmail(ownerEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<ArticleDetail> list = repo.findByAuthor(owner);

        List<ArticleDto> dtos = list.stream().map(article -> {
            ArticleDto dto = modelMapper.map(article, ArticleDto.class);
            dto.setAuthorName(owner.getUname());
            return dto;
        }).toList();

        return dtos;
    }

    public List<ArticleDto> getArticleByTitle(String title) {
        List<ArticleDetail> list = repo.findByTitle(title).orElseThrow(() -> new RuntimeException("Vid not found"));
        ;
        List<ArticleDto> dtos = new ArrayList<>();
        for (ArticleDetail detail : list) {
            ArticleDto dto = modelMapper.map(detail, ArticleDto.class);
            dto.setAuthorName(detail.getAuthor().getUname());
            dtos.add(dto);
        }
        return dtos;
    }

    @Override
    public ArticleDto getArticleByid(Integer id) {
        ArticleDetail vDetail = repo.findById(id).orElseThrow(() -> new RuntimeException("Video not found"));
        String name = vDetail.getAuthor().getUname();
        ArticleDto dto = modelMapper.map(vDetail, ArticleDto.class);
        dto.setAuthorName(name);
        return dto;
    }

    @Override
    public void deleteArticle(Integer id) {
        repo.deleteById(id);
    }

}
