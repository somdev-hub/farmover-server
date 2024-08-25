package com.farmover.server.farmover.controllers;

import java.util.Map;

import org.springframework.ai.vertexai.palm2.VertexAiPaLm2ChatModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai")
@CrossOrigin
public class FarmAiController {

    private VertexAiPaLm2ChatModel vertexAiPaLm2ChatModel;

    public FarmAiController(VertexAiPaLm2ChatModel chatModel) {
        this.vertexAiPaLm2ChatModel = chatModel;
    }

    @PostMapping("/chat")
    public ResponseEntity<Map<String, String>> getResponse(@RequestBody String prompt) {
        Map<String, String> generation = Map.of("generation", vertexAiPaLm2ChatModel.call(prompt));

        return new ResponseEntity<Map<String, String>>(generation, HttpStatus.OK);
    }

}
