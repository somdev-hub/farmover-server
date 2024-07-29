package com.farmover.server.farmover.services;


import com.farmover.server.farmover.payloads.request.DownVoteRequest;

public interface DownVoteVideoService {
     void downVote(DownVoteRequest request);
    void deleteDownVote(String uname,int videoId);
}
