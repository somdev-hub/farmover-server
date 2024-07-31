package com.farmover.server.farmover.payloads;

import lombok.Data;

@Data
public class CommentVideoDto {
    private String email;
    private String comment;
    private String date;
}
