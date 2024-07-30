package com.farmover.server.farmover.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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

    @PostMapping("/add-article")
    public ResponseEntity<String> addArticle(@RequestParam String ownerEmail, @ModelAttribute ArticleRequest dto) {
        serviceImp.addArticle(ownerEmail, dto);
        return new ResponseEntity<String>("", HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ArticleDto getArticle(@PathVariable Integer id) {
        return serviceImp.getArticleByid(id);
    }

    @GetMapping("/get-by-email")
    public List<ArticleDto> getArticleByAuthor(@RequestParam String ownerEmail) {
        return serviceImp.getArticleByAuthor(ownerEmail);
    }

    @GetMapping("/get-article-by-title")
    public List<ArticleDto> getArticleByTitle(@RequestParam String title) {
        return serviceImp.getArticleByTitle(title);
    }

    @DeleteMapping("/{id}")
    public void deleteArticle(@PathVariable Integer id) {
        serviceImp.deleteArticle(id);
    }
}
