package com.farmover.server.farmover.services;

import java.util.List;


public interface SuitableForService {
    public void addToStorage(int storageId,String name);
    public List<String> getAllByStorage(int storageId);
}
