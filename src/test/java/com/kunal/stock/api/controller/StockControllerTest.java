package com.kunal.stock.api.controller;

import com.kunal.stock.api.entity.Stock;
import com.kunal.stock.api.repository.StockRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
/**
 * @author Kumar.Kunal
 * @project Stock API
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(StockController.class)
class StockControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StockRepository mockStockRepository;

    @Test
    void testGetStocks() throws Exception {
        final List<Stock> stocks = List.of(new Stock(1, "Kumar", new BigDecimal("12.00"),
                Timestamp.valueOf(LocalDateTime.of(2023, 1, 1, 0, 0, 0, 0))));
        when(mockStockRepository.findAll()).thenReturn(stocks);

        final MockHttpServletResponse response = mockMvc.perform(get("/api/stocks")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.EXPECTATION_FAILED.value());
        assertThat(response.getContentAsString()).isEqualTo("");
    }

    @Test
    void testGetStocks_StockRepositoryReturnsNoItems() throws Exception {
        when(mockStockRepository.findAll()).thenReturn(Collections.emptyList());
        final MockHttpServletResponse response = mockMvc.perform(get("/api/stocks")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.EXPECTATION_FAILED.value());
        assertThat(response.getContentAsString()).isEqualTo("");
    }

    @Test
    void testGetStock() throws Exception {
        final Optional<Stock> stock = Optional.of(new Stock(1, "Kumar", new BigDecimal("12.00"),
                Timestamp.valueOf(LocalDateTime.of(2023, 1, 1, 0, 0, 0, 0))));
        when(mockStockRepository.findById(1)).thenReturn(stock);

        final MockHttpServletResponse response = mockMvc.perform(get("/api/stocks/{id}", "id")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.EXPECTATION_FAILED.value());
        assertThat(response.getContentAsString()).isEqualTo("");
    }

    @Test
    void testLoadStocksPage() throws Exception {
        final Page<Stock> stocks = new PageImpl<>(List.of(new Stock(2, "Kunal", new BigDecimal("13.00"),
                Timestamp.valueOf(LocalDateTime.of(2023, 1, 1, 0, 0, 0, 0)))));
        when(mockStockRepository.findAllPage(any(Pageable.class))).thenReturn(stocks);

        final MockHttpServletResponse response = mockMvc.perform(get("/api/stocks/page")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void testLoadStocksPage_StockRepositoryReturnsNoItems() throws Exception {
        when(mockStockRepository.findAllPage(any(Pageable.class))).thenReturn(new PageImpl<>(Collections.emptyList()));

        final MockHttpServletResponse response = mockMvc.perform(get("/api/stocks/page")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void testAddStock() throws Exception {
        final Stock stock = new Stock(1, "Kumar", new BigDecimal("12.00"),
                Timestamp.valueOf(LocalDateTime.of(2023, 1, 1, 0, 0, 0, 0)));
        when(mockStockRepository.save(new Stock(1, "Kumar", new BigDecimal("12.00"),
                Timestamp.valueOf(LocalDateTime.of(2023, 1, 1, 0, 0, 0, 0)))))
                .thenReturn(stock);
        final MockHttpServletResponse response = mockMvc.perform(post("/api/stocks")
                        .content("content").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isNotEqualTo(HttpStatus.OK.value());
    }
}
