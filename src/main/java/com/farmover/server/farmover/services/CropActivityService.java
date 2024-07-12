package com.farmover.server.farmover.services;

import java.util.List;

import com.farmover.server.farmover.payloads.CropActivityDto;

public interface CropActivityService {
    CropActivityDto addCropActivity(CropActivityDto cropActivityDto);

    CropActivityDto getCropActivity(Integer id);

    CropActivityDto updateCropActivity(CropActivityDto cropActivityDto, Integer id);

    void deleteCropActivity(Integer id);

    List<CropActivityDto> getAllCropActivities();

    List<CropActivityDto> getCropActivitiesByProduction(Integer token);
}
