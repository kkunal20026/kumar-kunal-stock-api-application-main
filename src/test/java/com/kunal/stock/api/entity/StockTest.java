/**
 * 
 */
package com.kunal.stock.api.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Kumar.Kunal
 * @project Stock API
 */
public class StockTest {
	
	private Stock stockUnderTest;

    @BeforeEach
    void setUp() {
        stockUnderTest = new Stock(1, "Kumar", new BigDecimal("12.00"),
                Timestamp.valueOf(LocalDateTime.of(2023, 1, 1, 0, 0, 0, 0)));
    }

    @Test
    void testPrePersist() {
        stockUnderTest.prePersist();
    }

    @Test
    void testToString() {
        assertThat(stockUnderTest.toString()).isEqualTo("Stock(id=1, name=Kumar, currentPrice=12.00, lastUpdate=2023-01-01 00:00:00.0)");
        assertThat(stockUnderTest.toString()).hasToString(stockUnderTest.toString());
    }

    @Test
    void testEquals() {
        assertThat(stockUnderTest.equals("o")).isFalse();
    }

    @Test
    void testCanEqual() {
        assertThat(stockUnderTest.canEqual("other")).isFalse();
    }

    @Test
    void testHashCode() {
        assertThat(stockUnderTest.hashCode()).isEqualTo(2006498835);
    }

}
