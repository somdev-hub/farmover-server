package com.farmover.server.farmover.services;

import com.farmover.server.farmover.payloads.request.UpVoteVideoRequest;

public interface UpVoteVideoService {

    void upVote(UpVoteVideoRequest request);

    void deleteUpVote(String uname, Integer videoId);

}
