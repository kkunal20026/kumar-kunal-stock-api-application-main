package com.kunal.stock.api.controller;

import com.kunal.stock.api.entity.Stock;
import com.kunal.stock.api.repository.StockRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Kumar.Kunal
 * @project Stock API
 */

@RestController
@RequestMapping("/api")
@Slf4j
public class StockController {

    private final StockRepository stockRepository;

    @Autowired
    public StockController(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    private static final String CREATE_STOCK = "Create stock: {}";

    @PostMapping("/stocks")
    public ResponseEntity<Stock> addStock(@RequestBody Stock stock) {
        log.info(CREATE_STOCK, stock);
        if (stock == null) {
            log.error(CREATE_STOCK, "Stock Is Null");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            Stock createdStock = stockRepository.save(stock);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("id", String.valueOf(createdStock.getId()));
            httpHeaders.add("lastUpdate", String.valueOf(createdStock.getLastUpdate()));
            log.info("Created stock: {}", createdStock);
            return new ResponseEntity<>(createdStock, httpHeaders, HttpStatus.CREATED);
        } catch (Exception exception) {
            log.error(CREATE_STOCK, exception.getMessage());
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
    }

    @GetMapping("/stocks")
    public ResponseEntity<List<Stock>> getStocks() {
        log.info("Getting All Stocks");
        try {
        	Optional<List<Stock>> stocks = Optional.of(stockRepository.findAll().stream().collect(Collectors.toList())).stream().findAny();
            log.info("Returned stocks: {}", stocks);
            return new ResponseEntity<>(stocks.get(), HttpStatus.OK);
        } catch (Exception exception) {
            log.error("Error in getting stocks: {}", exception.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/stocks/{id}")
    public ResponseEntity<Stock> getStock(@PathVariable String id) {
        log.info("Get stock by id: {}", id);
        try {
            Optional<Stock> stock = stockRepository.findById(Integer.parseInt(id));
            if (stock.isEmpty()) {
                log.warn("Get stock by id {}: {}", id, " Stock Not Found ");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            log.info("Returned stock: {}", stock.get());
            return new ResponseEntity<>(stock.get(), HttpStatus.OK);
        } catch (Exception exception) {
            log.error("Get stock by id {}: {}", id, exception.getMessage());
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
    }

    @GetMapping(path = "/stocks/page")
    Page<Stock> loadStocksPage(
            @PageableDefault(page = 0, size = 5)
            @SortDefault.SortDefaults({
                    @SortDefault(sort = "name", direction = Sort.Direction.DESC),
                    @SortDefault(sort = "id", direction = Sort.Direction.ASC)
            })
            Pageable stocksPageable) {
        return stockRepository.findAllPage(stocksPageable);
    }

    @GetMapping("/stocks/page/{page}")
    public ResponseEntity<List<Stock>> getStocksWithPagination(@PathVariable String page) {
        log.info("Get stocks with paging: {}", page);
        try {
            Page<Stock> stocks = stockRepository.findAll(PageRequest.of(Integer.parseInt(page), 5));
            log.info("Returned number of stocks pages: {}, {}", page, stocks.get());
            return new ResponseEntity<>(stocks.get().collect(Collectors.toList()), HttpStatus.OK);
        } catch (Exception exception) {
            log.error("Get stocks with paging: {} , {} ", page, exception.getMessage());
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
    }

    @GetMapping("/stocks/name")
    public ResponseEntity<List<String>> getNames() {
        log.info("Get Stocks Names");
        try {
            Optional<List<String>> names = stockRepository.getDistinctNames();
            if (names.isPresent()) {
                log.info("Returned names for Stocks: {}", names.get());
                return new ResponseEntity<>(names.get(), HttpStatus.OK);
            }
        } catch (Exception exception) {
            log.error("Get names: {}", exception.getMessage());
            throw exception;
        }
        return (ResponseEntity<List<String>>) Collections.EMPTY_LIST;
    }

    @PatchMapping(path = "/stocks/{id}/{currentPrice}")
    public ResponseEntity<Stock> partialUpdateStock(@PathVariable Integer id, @PathVariable BigDecimal currentPrice) {
        try {
            Stock updatedStock = stockRepository.findById(id).get();
            updatedStock.setCurrentPrice(currentPrice);
            log.info("Partially Updated stock: {}", updatedStock);
            return new ResponseEntity<>(stockRepository.save(updatedStock), HttpStatus.ACCEPTED);
        } catch (Exception exception) {
            log.error("Update stock id: {} : {}", id, exception.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/stocks/{id}")
    public ResponseEntity<Void> deleteStock(@PathVariable String id) {
        log.info("Delete stock id: {}", id);
        if (id == null) {
            log.error("Delete stock: {}", "id is null");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {
            stockRepository.deleteById(Integer.valueOf(id));
            log.info("Deleted stock id: {}", id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception exception) {
            log.error("Delete stock id: {} , {}", id, exception.getMessage());
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
    }

}
