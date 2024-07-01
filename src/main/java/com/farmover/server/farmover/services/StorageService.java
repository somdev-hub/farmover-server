package com.farmover.server.farmover.services;

import java.util.ArrayList;

import com.farmover.server.farmover.entities.Storage;
import com.farmover.server.farmover.entities.Warehouse;

public interface StorageService {
    Storage geStorage(Integer id);
    void addStorage(Integer wareHouseId,Storage storage);
    void updateStorage(Integer id, Storage storage);
    ArrayList<Storage> getAllStorageByWarehouse(Warehouse warehouse);
    void deleteStorage(Integer id);

}
