package com.farmover.server.farmover.services;

import java.util.ArrayList;

import com.farmover.server.farmover.entities.Storage;
import com.farmover.server.farmover.entities.Warehouse;
import com.farmover.server.farmover.payloads.StorageDto;

public interface StorageService {
    StorageDto geStorage(Integer id);
    void addStorage(Integer wareHouseId,Storage storage);
    void updateStorage(Integer id, Storage storage);
    ArrayList<StorageDto> getAllStorageByWarehouse(Warehouse warehouse);
    void deleteStorage(Integer id);

}
