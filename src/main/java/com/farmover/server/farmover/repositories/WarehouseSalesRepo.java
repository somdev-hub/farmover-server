package com.farmover.server.farmover.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.farmover.server.farmover.entities.Warehouse;
import com.farmover.server.farmover.entities.WarehouseSales;

public interface WarehouseSalesRepo extends JpaRepository<WarehouseSales, Integer> {

    @Query("SELECT ws FROM WarehouseSales ws WHERE ws.warehouse = :warehouse  AND MONTH(ws.date) = MONTH(CURRENT_DATE) AND YEAR(ws.date) = YEAR(CURRENT_DATE)")
    List<WarehouseSales> findByWarehouseAndMonth(@Param("warehouse") Warehouse warehouse);
}
