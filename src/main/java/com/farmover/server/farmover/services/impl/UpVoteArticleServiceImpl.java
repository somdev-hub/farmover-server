package com.farmover.server.farmover.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.farmover.server.farmover.entities.ArticleDetail;
import com.farmover.server.farmover.entities.DownVoteArticle;
import com.farmover.server.farmover.entities.UpVoteArticle;
import com.farmover.server.farmover.entities.User;
import com.farmover.server.farmover.exceptions.ResourceNotFoundException;
import com.farmover.server.farmover.payloads.request.UpVoteArticleRequest;
import com.farmover.server.farmover.repositories.ArticleRepo;
import com.farmover.server.farmover.repositories.DownVoteArticleRepo;
import com.farmover.server.farmover.repositories.UpVoteArticleRepo;
import com.farmover.server.farmover.repositories.UserRepo;
import com.farmover.server.farmover.services.UpVoteArticleService;

@Service
public class UpVoteArticleServiceImpl implements UpVoteArticleService {

    @Autowired
    UpVoteArticleRepo uRepo;

    @Autowired
    DownVoteArticleRepo dRepo;

    @Autowired
    ArticleRepo articleRepo;

    @Autowired
    UserRepo userRepo;

    @Override
    public void upVote(UpVoteArticleRequest request) {
        ArticleDetail articleDetail = articleRepo.findById(request.getArticleId())
                .orElseThrow(() -> new RuntimeException("Video not found"));



        List<UpVoteArticle> ls = uRepo.findByArticleAndEmail(articleDetail, request.getEmail());

        List<DownVoteArticle> lss = dRepo.findByArticleAndEmail(articleDetail, request.getEmail());

        if (ls.size() == 0) {
            UpVoteArticle aUpVoteArticle = new UpVoteArticle();
            aUpVoteArticle.setArticle(articleDetail);
            aUpVoteArticle.setEmail(request.getEmail());
            articleDetail.setUpCount(articleDetail.getUpCount() + 1);
            uRepo.save(aUpVoteArticle);
        }
        if (lss.size() != 0) {
            articleDetail.setDownCount(articleDetail.getDownCount() - 1);
            dRepo.deleteById(lss.get(0).getId());
        }
        articleRepo.save(articleDetail);
    }

    @Override
    public void deleteUpVote(String email, Integer articleId) {
        ArticleDetail articleDetail = articleRepo.findById(articleId)
                .orElseThrow(() -> new RuntimeException("Video not found"));

       

        List<UpVoteArticle> ls = uRepo.findByArticleAndEmail(articleDetail, email);
        if (ls.size() != 0) {
            articleDetail.setUpCount(articleDetail.getUpCount() - 1);

            uRepo.deleteById(ls.get(0).getId());
        }
        articleRepo.save(articleDetail);
    }

}
