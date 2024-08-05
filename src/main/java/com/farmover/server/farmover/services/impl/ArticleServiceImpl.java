package com.farmover.server.farmover.services.impl;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.farmover.server.farmover.entities.ArticleDetail;
import com.farmover.server.farmover.entities.ArticleViews;
import com.farmover.server.farmover.entities.CommentArticle;
import com.farmover.server.farmover.entities.DownVoteArticle;
import com.farmover.server.farmover.entities.UpVoteArticle;
import com.farmover.server.farmover.entities.User;
import com.farmover.server.farmover.exceptions.ResourceNotFoundException;
import com.farmover.server.farmover.payloads.ArticleDto;
import com.farmover.server.farmover.payloads.CommentArticleDto;
import com.farmover.server.farmover.payloads.records.VideoUserResponseRecord;
import com.farmover.server.farmover.payloads.request.ArticleRequest;
import com.farmover.server.farmover.repositories.ArticleRepo;
import com.farmover.server.farmover.repositories.DownVoteArticleRepo;
import com.farmover.server.farmover.repositories.UpVoteArticleRepo;
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

    @Autowired
    UpVoteArticleRepo upVoteArticleRepo;

    @Autowired
    DownVoteArticleRepo downVoteArticleRepo;

    @Autowired
    UserRepo UserRepo;

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

        String contentUrl = "";

        try {
            contentUrl = s3ServiceImpl.uploadRichText(article.getContent());
        } catch (Exception e) {
            throw new RuntimeException("article not found");
        }

        articleDetail.setContent(contentUrl);

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
    public ArticleDto getArticleByid(Integer id, String email) {
        ArticleDetail vDetail = repo.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("article", "id", id.toString()));
        String name = vDetail.getAuthor().getUname();
        ArticleDto dto = modelMapper.map(vDetail, ArticleDto.class);
        dto.setAuthorName(name);
        List<CommentArticleDto> comments = vDetail.getArticleComment().stream().map(comment -> {
            User user = UserRepo.findByEmail(comment.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            CommentArticleDto commentDto = new CommentArticleDto();
            commentDto.setComment(comment.getComment());
            commentDto.setName(user.getUname());
            commentDto.setProfilePicture(user.getProfileImage());
            commentDto.setDate(comment.getDate());
            return commentDto;
        }).toList();

        dto.setArticleComment(comments);

        if (vDetail.getUpVoteArticle().stream().anyMatch(upVote -> upVote.getEmail().equals(email))) {
            dto.setIsUpVoted(true);
        } else {
            dto.setIsUpVoted(false);
        }

        if (vDetail.getDownVoteArticle().stream().anyMatch(downVote -> downVote.getEmail().equals(email))) {
            dto.setIsDownVoted(true);
        } else {
            dto.setIsDownVoted(false);
        }
        return dto;
    }

    @Override
    public void deleteArticle(Integer id) {
        ArticleDetail articleDetail = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("article", "id", id.toString()));

        Boolean isThumbnailDeleted = s3ServiceImpl.deleteFile(articleDetail.getThumbnail());
        Boolean isContentDeleted = s3ServiceImpl.deleteFile(articleDetail.getContent());

        if (!isThumbnailDeleted || !isContentDeleted) {
            throw new RuntimeException("Error deleting article");
        } else {
            repo.deleteById(id);
        }
    }

    @Override
    public Page<VideoUserResponseRecord> getArticles(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ArticleDetail> articles = repo.findAll(pageable);

        return articles.map(article -> {
            User user = userRepo.findById(article.getAuthor().getId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            return new VideoUserResponseRecord(article.getId(), article.getThumbnail(),
                    article.getTitle(), user.getUname(), user.getProfileImage(), article.getSubHeading());
        });

    }

    @Override
    public String upVoteArticle(Integer id, String email) {
        ArticleDetail article = repo.findById(id).orElseThrow(() -> {
            throw new ResourceNotFoundException("article", "id", id.toString());
        });

        boolean flag = false;

        if (article.getUpVoteArticle().stream().noneMatch(upVote -> upVote.getEmail().equals(email))) {
            article.setUpCount(article.getUpCount() + 1);
            UpVoteArticle upvoteArticle = new UpVoteArticle();
            upvoteArticle.setEmail(email);
            upvoteArticle.setArticle(article);
            article.getUpVoteArticle().add(upvoteArticle);

            flag = true;
        } else {
            if (article.getUpCount() > 0) {
                article.setUpCount(article.getUpCount() - 1);
                UpVoteArticle upvoteToRemove = article.getUpVoteArticle().stream()
                        .filter(upVote -> upVote.getEmail().equals(email))
                        .findFirst()
                        .orElse(null);
                if (upvoteToRemove != null) {
                    article.getUpVoteArticle().remove(upvoteToRemove);
                    upVoteArticleRepo.delete(upvoteToRemove); // Ensure you have a repository for UpVoteArticle
                }
            }
            flag = false;
        }

        repo.save(article);

        return flag ? "upvoted" : "upvote removed";
    }

    @Override
    public String downVoteArticle(Integer id, String email) {
        ArticleDetail article = repo.findById(id).orElseThrow(() -> {
            throw new ResourceNotFoundException("article", "id", id.toString());
        });

        boolean flag = false;

        if (article.getDownVoteArticle().stream().noneMatch(downVote -> downVote.getEmail().equals(email))) {
            article.setDownCount(article.getDownCount() + 1);
            DownVoteArticle downvoteArticle = new DownVoteArticle();
            downvoteArticle.setEmail(email);
            downvoteArticle.setArticle(article);
            article.getDownVoteArticle().add(downvoteArticle);

            flag = true;
        } else {
            if (article.getDownCount() > 0) {
                article.setDownCount(article.getDownCount() - 1);
                DownVoteArticle downvoteToRemove = article.getDownVoteArticle().stream()
                        .filter(upVote -> upVote.getEmail().equals(email))
                        .findFirst()
                        .orElse(null);
                if (downvoteToRemove != null) {
                    article.getDownVoteArticle().remove(downvoteToRemove);
                    downVoteArticleRepo.delete(downvoteToRemove);
                }
            }
            flag = false;
        }

        repo.save(article);

        return flag ? "downvoted" : "downvote removed";
    }

    @Override
    public void addCommentToArticle(Integer id, String email, String comment) {
        ArticleDetail article = repo.findById(id).orElseThrow(() -> {
            throw new ResourceNotFoundException("article", "id", id.toString());
        });

        List<CommentArticle> comments = article.getArticleComment();
        CommentArticle commentArticle = new CommentArticle();
        commentArticle.setArticle(article);
        commentArticle.setComment(comment);
        commentArticle.setEmail(email);
        commentArticle.setDate(new Date(System.currentTimeMillis()));
        comments.add(commentArticle);
        article.setCommentCount(article.getCommentCount() + 1);

        repo.save(article);
    }

    @Override
    public void addViewToArticle(Integer id, String email) {
        ArticleDetail article = repo.findById(id).orElseThrow(() -> {
            throw new ResourceNotFoundException("article", "id", id.toString());
        });

        if (article.getArticleViews().stream().noneMatch(view -> view.getEmail().equals(email))) {
            article.setViewCount(article.getViewCount() + 1);
            ArticleViews view = new ArticleViews();
            view.setEmail(email);
            view.setArticle(article);
            view.setDate(new Date(System.currentTimeMillis()));
            article.getArticleViews().add(view);
        }

        repo.save(article);
    }

}
