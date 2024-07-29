package com.farmover.server.farmover.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.farmover.server.farmover.payloads.ArticleDto;
import com.farmover.server.farmover.payloads.request.ArticleRequest;
import com.farmover.server.farmover.services.impl.ArticleServiceImpl;

@RestController
@CrossOrigin
@RequestMapping("/articles")
public class ArticleController {
    @Autowired
    ArticleServiceImpl serviceImp;

    @PostMapping("/addArticle")
    public ResponseEntity<String> addArticle(@RequestParam String ownerEmail,@ModelAttribute ArticleRequest dto) {
        serviceImp.addArticle(ownerEmail, dto);
        return new ResponseEntity<String>("",HttpStatus.OK);
    }

    @GetMapping("/getArticle")
    public ArticleDto getArticle(@RequestParam Integer id) {
         return serviceImp.getArticleByid(id);
    }

    @GetMapping("/getArticleByAuthor")
    public List<ArticleDto> getArticleByAuthor(@RequestParam String ownerEmail) {
        return serviceImp.getArticleByAuthor(ownerEmail);
    }

    @GetMapping("/getArticleByTitle")
    public List<ArticleDto> getArticleByTitle(@RequestParam String title) {
        return serviceImp.getArticleByTitle(title);
    }

    @PostMapping("/deleteArticle")
    public void deleteArticle(@RequestParam Integer id) {
        serviceImp.deleteArticle(id);
    }
}
