/**
 * 
 */
package com.kunal.stock.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.SpecVersion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Kumar.Kunal
 * @project Stock API
 */
public class OpenApiConfigTest {
	
	private OpenApiConfig openApiConfigUnderTest;

    @BeforeEach
    void setUp() {
        openApiConfigUnderTest = new OpenApiConfig();
    }

    @Test
    void testCustomOpenAPI() {
        final OpenAPI expectedResult = new OpenAPI(SpecVersion.V30);
        final OpenAPI result = openApiConfigUnderTest.customOpenAPI();
        assertThat(result).isNotEqualTo(expectedResult);
    }

}
