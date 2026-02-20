package br.com.projedata.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.projedata.dtos.ProductionSuggestionDTO;
import br.com.projedata.models.Product;
import br.com.projedata.models.ProductMaterial;
import br.com.projedata.models.RawMaterial;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@Path("/api/production-suggestions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductionSuggestionController {

    @GET
    public ProductionSuggestionDTO suggest() {
    	// Load all materials with stock
        List<RawMaterial> rawMaterials = RawMaterial.listAll();

        Map<Long, Integer> stock = new HashMap<>();
        for (RawMaterial rm : rawMaterials) {
            stock.put(rm.id, rm.stockQuantity);
        }

        // Products ordered by highest price first
        List<Product> products = Product.list("order by price desc");

        ProductionSuggestionDTO result = new ProductionSuggestionDTO();
        result.items = new ArrayList<>();
        result.totalValue = BigDecimal.ZERO;

        for (Product product : products) {
            if (product.materials.isEmpty()) {
                continue; // product without BOM cannot be produced
            }

            // compute max possible for this product with remaining stock
            int max = Integer.MAX_VALUE;

            for (ProductMaterial pm : product.materials) {
                int available = stock.getOrDefault(pm.rawMaterial.id, 0);
                int possible = (int) Math.floor(available / pm.quantityRequired);
                max = Math.min(max, possible);
            }

            if (max <= 0) {
                continue;
            }

            // consume stock
            for (ProductMaterial pm : product.materials) {
                int available = stock.getOrDefault(pm.rawMaterial.id, 0);
                stock.put(pm.rawMaterial.id, available - (max * pm.quantityRequired));
            }

            ProductionSuggestionDTO.Item item = new ProductionSuggestionDTO.Item();
            item.productId = product.id;
            item.productCode = product.code;
            item.productName = product.name;
            item.productPrice = product.price;
            item.quantityToProduce = max;
            item.totalValue = product.price.multiply(BigDecimal.valueOf(max));

            result.items.add(item);
            result.totalValue = result.totalValue.add(item.totalValue);
        }

        return result;
    }
}