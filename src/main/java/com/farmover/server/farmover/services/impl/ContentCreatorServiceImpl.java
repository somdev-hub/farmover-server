package com.farmover.server.farmover.services.impl;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.farmover.server.farmover.entities.ArticleDetail;
import com.farmover.server.farmover.entities.CommentArticle;
import com.farmover.server.farmover.entities.CommentVideo;
import com.farmover.server.farmover.entities.User;
import com.farmover.server.farmover.entities.VideoDetail;
import com.farmover.server.farmover.exceptions.ResourceNotFoundException;
import com.farmover.server.farmover.payloads.RecentsCommentsDto;
import com.farmover.server.farmover.payloads.records.ContentCreatorDashboardCard;
import com.farmover.server.farmover.payloads.records.VideoCommentRecord;
import com.farmover.server.farmover.repositories.ArticleRepo;
import com.farmover.server.farmover.repositories.CommentArticleRepo;
import com.farmover.server.farmover.repositories.CommentVideoRepo;
import com.farmover.server.farmover.repositories.UserRepo;
import com.farmover.server.farmover.repositories.VideoRepo;
import com.farmover.server.farmover.services.ContentCreatorService;

@Service
public class ContentCreatorServiceImpl implements ContentCreatorService {

    @Autowired
    VideoRepo videoRepo;

    @Autowired
    ArticleRepo articleRepo;

    @Autowired
    UserRepo userRepo;

    @Autowired
    CommentVideoRepo commentVideoRepo;

    @Autowired
    CommentArticleRepo commentArticleRepo;

    @Override
    public List<ContentCreatorDashboardCard> getDashboardCards(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        ArrayList<VideoDetail> videos = videoRepo.findByAuthor(user);

        ArrayList<ArticleDetail> articles = articleRepo.findByAuthor(user);

        List<ContentCreatorDashboardCard> dashboardCards = new ArrayList<>();

        for (VideoDetail video : videos) {
            dashboardCards.add(new ContentCreatorDashboardCard("video", video.getTitle(), video.getViewCount(),
                    video.getUpCount(), video.getCommentCount()));
        }

        for (ArticleDetail article : articles) {
            dashboardCards.add(new ContentCreatorDashboardCard("article", article.getTitle(), article.getViewCount(),
                    article.getUpCount(), article.getCommentCount()));
        }

        return dashboardCards;
    }

    public List<RecentsCommentsDto> getRecentComments(String email, int page, int size) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        Pageable pageable = PageRequest.of(page, size);

        Page<VideoDetail> videos = videoRepo.findByAuthor(user, pageable);
        Page<ArticleDetail> articles = articleRepo.findByAuthor(user, pageable);

        List<RecentsCommentsDto> recentsComments = new ArrayList<>();

        for (VideoDetail video : videos) {
            RecentsCommentsDto recentsComment = new RecentsCommentsDto();
            recentsComment.setTitle(video.getTitle());
            recentsComment.setDate(video.getDate());
            recentsComment.setType("video");
            List<CommentVideo> videoComments = commentVideoRepo.findByVideoAndDate(video,
                    Date.valueOf(LocalDate.now()));
            recentsComment.setComments(videoComments.stream().map((comment) -> {
                User user1 = userRepo.findByEmail(comment.getEmail())
                        .orElseThrow(() -> new ResourceNotFoundException("User", "email", comment.getEmail()));
                return new VideoCommentRecord(
                        comment.getId(),
                        user1.getUname(),
                        user1.getProfileImage(),
                        comment.getComment(),
                        comment.getDate());
            }).collect(Collectors.toList()));
            recentsComments.add(recentsComment);
        }

        for (ArticleDetail article : articles) {
            RecentsCommentsDto recentsComment = new RecentsCommentsDto();
            recentsComment.setTitle(article.getTitle());
            recentsComment.setDate(article.getDate());
            recentsComment.setType("article");
            List<VideoCommentRecord> articleComments = commentArticleRepo.findByArticleAndDate(article,
                    Date.valueOf(LocalDate.now())).stream().map((comment) -> {
                        User user1 = userRepo.findByEmail(comment.getEmail())
                                .orElseThrow(() -> new ResourceNotFoundException("User", "email", comment.getEmail()));
                        return new VideoCommentRecord(
                                comment.getId(),
                                user1.getUname(),
                                user1.getProfileImage(),
                                comment.getComment(),
                                comment.getDate());
                    }).collect(Collectors.toList());
            recentsComment.setComments(articleComments);

            recentsComments.add(recentsComment);
        }

        return recentsComments;
    }
}
