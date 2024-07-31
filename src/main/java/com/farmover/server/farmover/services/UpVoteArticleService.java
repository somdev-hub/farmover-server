package com.farmover.server.farmover.services;

import com.farmover.server.farmover.payloads.request.UpVoteArticleRequest;

public interface UpVoteArticleService {

    void upVote(UpVoteArticleRequest request);

    void deleteUpVote(String uname, Integer articleId);
}
