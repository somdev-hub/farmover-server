package com.farmover.server.farmover.payloads;

import lombok.Data;

@Data
public class CommentVideoDto {
    private String uname;
    private String comment;
    private String date;
}
