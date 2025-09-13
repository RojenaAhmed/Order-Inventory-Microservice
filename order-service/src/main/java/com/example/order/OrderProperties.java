package com.example.order;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "app")
public class OrderProperties {
    private String inventoryBaseUrl = "http://localhost:8081";

    public String inventoryBaseUrl() {
        return inventoryBaseUrl;
    }
    public void setInventoryBaseUrl(String v) {
        this.inventoryBaseUrl = v;
    }
}
