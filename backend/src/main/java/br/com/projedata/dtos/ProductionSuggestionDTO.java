package br.com.projedata.dtos;

import java.math.BigDecimal;
import java.util.List;

public class ProductionSuggestionDTO {

    public List<Item> items;
    public BigDecimal totalValue;

    public static class Item {
        public Long productId;
        public String productCode;
        public String productName;
        public BigDecimal productPrice;

        public Integer quantityToProduce;
        public BigDecimal totalValue;
    }
}