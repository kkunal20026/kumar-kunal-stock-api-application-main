package com.kunal.stock.api.repository;

import com.kunal.stock.api.entity.Stock;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Kumar.Kunal
 * @project Stock API
 */

@Repository
public interface StockRepository extends JpaRepository<Stock, Integer> {

    List <Stock> findAll();
	Optional<Stock> findById(Integer id);
    
    Optional<List<Stock>> findByName(String name);
    
    @Query("select distinct s.name from Stock s")
    Optional<List<String>> getDistinctNames();

    // Pagination
    Optional<List<Stock>> findByName(String name, Pageable pageable);
    
    // Pagination
    @Query("select s from Stock s")
    Page<Stock> findAllPage(Pageable pageable);

}
