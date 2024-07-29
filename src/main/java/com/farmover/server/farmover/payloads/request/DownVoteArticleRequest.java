package com.farmover.server.farmover.payloads.request;

import lombok.Data;

@Data
public class DownVoteArticleRequest {
    String uname;
    Integer articleId;
}
