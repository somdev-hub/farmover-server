package com.farmover.server.farmover.services;

import com.farmover.server.farmover.payloads.request.DownVoteArticleRequest;

public interface DownVoteArticleService {
    void downVote(DownVoteArticleRequest request);
    void deleteDownVote(String uname,int videoId);
}
