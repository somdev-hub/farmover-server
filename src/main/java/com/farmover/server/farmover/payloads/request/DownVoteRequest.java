package com.farmover.server.farmover.payloads.request;

import lombok.Data;

@Data
public class DownVoteRequest {
    String uname;
    Integer videoId;
}
