package com.farmover.server.farmover.services;

import java.util.List;

import org.springframework.data.domain.Page;

import com.farmover.server.farmover.payloads.RecentsCommentsDto;
import com.farmover.server.farmover.payloads.records.ContentCreatorDashboardCard;

public interface ContentCreatorService {
    List<ContentCreatorDashboardCard> getDashboardCards(String email);

    List<RecentsCommentsDto> getRecentComments(String email, int page, int size);
}
