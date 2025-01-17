package com.emmanuel.api.springsecurity.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;
import java.math.BigDecimal;

public class SaveProduct implements Serializable {

    @NotBlank(message = "The name must have at least 2 digits")
    private String name;

    @DecimalMin(value = "0.01", message = "The price mustn't be null or 0")
    private BigDecimal price;

    @Min(value = 1)
    private Long categoryId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
}
