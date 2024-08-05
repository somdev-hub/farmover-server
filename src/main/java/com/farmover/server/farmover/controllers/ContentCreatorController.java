package com.farmover.server.farmover.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.farmover.server.farmover.payloads.RecentsCommentsDto;
import com.farmover.server.farmover.payloads.records.ContentCreatorDashboardCard;
import com.farmover.server.farmover.services.impl.ContentCreatorServiceImpl;

@RestController
@CrossOrigin
@RequestMapping("/content-creator")
public class ContentCreatorController {

    @Autowired
    private ContentCreatorServiceImpl contentCreatorService;

    @GetMapping("/cards")
    public ResponseEntity<List<ContentCreatorDashboardCard>> getCards(@RequestParam String email) {
        return new ResponseEntity<List<ContentCreatorDashboardCard>>(contentCreatorService.getDashboardCards(email),
                HttpStatus.OK);
    }

    @GetMapping("/recent")
    public ResponseEntity<List<RecentsCommentsDto>> getRecents(@RequestParam String email, @RequestParam int page,
            @RequestParam int size) {
        return new ResponseEntity<List<RecentsCommentsDto>>(
                contentCreatorService.getRecentComments(email, page, size), HttpStatus.OK);
    }

    // @GetMapping("/test")
    // public String getMethodName() {
    // // VideoDetail video = videoRepo.findById(1).orElseThrow(() -> new
    // RuntimeException("Video not found"));
    // List<CommentVideo> byVideoAndDate =
    // commentVideoRepo.findByDate(Date.valueOf(LocalDate.now()));
    // System.out.println(byVideoAndDate);
    // return new String("done");
    // }

}
