package com.farmover.server.farmover.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.farmover.server.farmover.entities.ArticleDetail;
import com.farmover.server.farmover.entities.DownVoteArticle;
import com.farmover.server.farmover.entities.UpVoteArticle;
import com.farmover.server.farmover.payloads.request.DownVoteArticleRequest;
import com.farmover.server.farmover.repositories.ArticleRepo;
import com.farmover.server.farmover.repositories.DownVoteArticleRepo;
import com.farmover.server.farmover.repositories.UpVoteArticleRepo;
import com.farmover.server.farmover.services.DownVoteArticleService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class DownVoteArticleServiceImpl implements DownVoteArticleService{
    @Autowired 
    UpVoteArticleRepo uRepo;
    @Autowired
    DownVoteArticleRepo dRepo;
    @Autowired
    ArticleRepo articleRepo;
    @Override
    public void downVote(DownVoteArticleRequest request) {
        ArticleDetail  articleDetail = articleRepo.findById(request.getArticleId()).orElseThrow(() -> new RuntimeException("Video not found"));
        List<UpVoteArticle> ls = uRepo.findByArticleAndUname(articleDetail, request.getUname());
        List<DownVoteArticle> lss = dRepo.findByArticleAndUname(articleDetail, request.getUname());
        if(lss.size()==0){
            DownVoteArticle downVoteArticle = new DownVoteArticle();
            downVoteArticle.setArticle(articleDetail);
            downVoteArticle.setUname(request.getUname());
            articleDetail.setDownCount(articleDetail.getDownCount()+1);
            dRepo.save(downVoteArticle);
        }
        if(ls.size()!=0){
            articleDetail.setUpCount(articleDetail.getUpCount()-1);
            uRepo.deleteById(ls.get(0).getId());
        }
        articleRepo.save(articleDetail);
    }

    @Override
    public void deleteDownVote(String uname, int articleId) {
        ArticleDetail  articleDetail = articleRepo.findById(articleId).orElseThrow(() -> new RuntimeException("Video not found"));
        List<DownVoteArticle> lss = dRepo.findByArticleAndUname(articleDetail, uname);
        if(lss.size()!=0){
            dRepo.deleteById(lss.get(0).getId());
            articleDetail.setDownCount(articleDetail.getDownCount()-1);
        }
        articleRepo.save(articleDetail);
    }
    
}
