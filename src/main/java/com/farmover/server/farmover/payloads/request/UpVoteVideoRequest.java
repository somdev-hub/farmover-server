package com.farmover.server.farmover.payloads.request;

import lombok.Data;

@Data
public class UpVoteVideoRequest {

    String email;

    Integer videoId;
}
