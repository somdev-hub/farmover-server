package com.farmover.server.farmover.services;

import java.util.ArrayList;
import java.util.List;

import com.farmover.server.farmover.entities.Storage;
import com.farmover.server.farmover.payloads.StorageCardDto;
import com.farmover.server.farmover.payloads.StorageDto;
import com.farmover.server.farmover.payloads.request.StorageRequestDto;

public interface StorageService {
    StorageDto geStorage(Integer id);

    StorageDto addStorage(String email, StorageRequestDto requestDto);

    List<StorageCardDto> getStorageCards(String email);

    void updateStorage(Integer id, Storage storage);

    ArrayList<StorageDto> getAllStorageByOwner(String email);

    void deleteStorage(Integer id);

}
