package com.farmover.server.farmover.services.impl;

import java.time.LocalDate;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.farmover.server.farmover.entities.CropActivity;
import com.farmover.server.farmover.payloads.CropActivityDto;
import com.farmover.server.farmover.repositories.CropActivityRepo;
import com.farmover.server.farmover.services.CropActivityService;

@Service
public class CropActivityServiceImpl implements CropActivityService {

    @Autowired
    private CropActivityRepo cropActivityRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CropActivityDto addCropActivity(CropActivityDto cropActivityDto) {
        CropActivity cropActivity = modelMapper.map(cropActivityDto, CropActivity.class);

        cropActivity.setStartDate(LocalDate.now());

        cropActivityRepo.save(cropActivity);

        return modelMapper.map(cropActivity, CropActivityDto.class);
    }

    @Override
    public CropActivityDto getCropActivity(Integer id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCropActivity'");
    }

    @Override
    public CropActivityDto updateCropActivity(CropActivityDto cropActivityDto, Integer id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateCropActivity'");
    }

    @Override
    public void deleteCropActivity(Integer id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteCropActivity'");
    }

    @Override
    public List<CropActivityDto> getAllCropActivities() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllCropActivities'");
    }

    @Override
    public List<CropActivityDto> getCropActivitiesByProduction(Integer token) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCropActivitiesByProduction'");
    }

}
