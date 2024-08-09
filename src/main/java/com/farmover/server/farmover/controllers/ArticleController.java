package com.farmover.server.farmover.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.farmover.server.farmover.payloads.ArticleDto;
import com.farmover.server.farmover.payloads.records.VideoUserResponseRecord;
import com.farmover.server.farmover.payloads.request.ArticleRequest;
import com.farmover.server.farmover.payloads.request.CommentArticleRequest;
import com.farmover.server.farmover.services.impl.ArticleServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@CrossOrigin
@RequestMapping("/articles")
public class ArticleController {

    @Autowired
    ArticleServiceImpl serviceImp;

    @PostMapping("/")
    public ResponseEntity<String> addArticle(@RequestParam String ownerEmail, @ModelAttribute ArticleRequest dto)
            throws JsonMappingException, JsonProcessingException {
        serviceImp.addArticle(ownerEmail, dto);
        return new ResponseEntity<String>("", HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ArticleDto getArticle(@PathVariable Integer id, @RequestParam String email) {
        return serviceImp.getArticleByid(id, email);
    }

    @GetMapping("/get-by-email")
    public List<ArticleDto> getArticleByAuthor(@RequestParam String ownerEmail) {
        return serviceImp.getArticleByAuthor(ownerEmail);
    }

    @GetMapping("/get-by-title")
    public List<ArticleDto> getArticleByTitle(@RequestParam String title) {
        return serviceImp.getArticleByTitle(title);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteArticle(@PathVariable Integer id) {

        serviceImp.deleteArticle(id);
        return new ResponseEntity<String>("article deleted", HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<Page<VideoUserResponseRecord>> getArticles(@RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return new ResponseEntity<Page<VideoUserResponseRecord>>(serviceImp.getArticles(page, size), HttpStatus.OK);
    }

    @PutMapping("/view/{id}")
    public ResponseEntity<String> addView(@PathVariable Integer id, @RequestParam String email) {
        serviceImp.addViewToArticle(id, email);
        return new ResponseEntity<String>("", HttpStatus.OK);
    }

    @PutMapping("/upvote/{id}")
    public ResponseEntity<String> upVote(@PathVariable Integer id, @RequestParam String email) {
        String upVoteArticle = serviceImp.upVoteArticle(id, email);
        return new ResponseEntity<String>(upVoteArticle, HttpStatus.OK);
    }

    @PutMapping("/downvote/{id}")
    public ResponseEntity<String> downVote(@PathVariable Integer id, @RequestParam String email) {
        String downVoteArticle = serviceImp.downVoteArticle(id, email);
        return new ResponseEntity<String>(downVoteArticle, HttpStatus.OK);
    }

    @PostMapping("/comment/{id}")
    public ResponseEntity<String> addComment(@PathVariable Integer id,
            @RequestBody CommentArticleRequest comment) {
        serviceImp.addCommentToArticle(id, comment.getEmail(), comment.getComment());
        return new ResponseEntity<String>("", HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> editArticle(@PathVariable Integer id, @ModelAttribute ArticleRequest dto) throws IOException {
        serviceImp.editArticle(id, dto);
        return new ResponseEntity<String>("Article updated", HttpStatus.ACCEPTED);
    }

}
