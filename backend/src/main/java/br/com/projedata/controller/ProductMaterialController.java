package br.com.projedata.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

import br.com.projedata.dtos.ProductMaterialRequestDTO;
import br.com.projedata.dtos.ProductMaterialResponseDTO;
import br.com.projedata.dtos.ProductResponseDTO;
import br.com.projedata.dtos.RawMaterialResponseDTO;
import br.com.projedata.models.Product;
import br.com.projedata.models.ProductMaterial;
import br.com.projedata.models.RawMaterial;

@Path("/api/product-materials")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductMaterialController {

    @GET
    public List<ProductMaterialResponseDTO> listAll() {
        return ProductMaterial.<ProductMaterial>listAll()
        		.stream()
        		.map(pm -> {
        			ProductResponseDTO productDTO = new ProductResponseDTO();
        			productDTO.id = pm.product.id;
        			productDTO.code = pm.product.code;
        			productDTO.name = pm.product.name;
        			productDTO.price = pm.product.price;
        			
        			RawMaterialResponseDTO materialDTO = new RawMaterialResponseDTO();
        			materialDTO.id = pm.rawMaterial.id;
        			materialDTO.code = pm.rawMaterial.code;
        			materialDTO.name = pm.rawMaterial.name;
        			materialDTO.stockQuantity = pm.rawMaterial.stockQuantity;
        			
        			ProductMaterialResponseDTO dto = new ProductMaterialResponseDTO();
        			dto.id = pm.id;
        			dto.product = productDTO;
        			dto.rawMaterial = materialDTO;
        			dto.quantityRequired = pm.quantityRequired;
        			
        			return dto;
        		})
        		.toList();
    }

    @GET
    @Path("/product/{productId}")
    public List<ProductMaterialResponseDTO> listByProduct(@PathParam("productId") Long productId) {
    	return ProductMaterial.<ProductMaterial>list("product.id", productId)
    			.stream()
    			.map(p -> {
    				RawMaterialResponseDTO rm = new RawMaterialResponseDTO();
    				rm.id = p.rawMaterial.id;
    				rm.code = p.rawMaterial.code;
    				rm.name = p.rawMaterial.name;
    				rm.stockQuantity = p.rawMaterial.stockQuantity;
    				
    				ProductMaterialResponseDTO dto = new ProductMaterialResponseDTO();
    				dto.id = p.id;
    				dto.rawMaterial = rm;
    				dto.quantityRequired = p.quantityRequired;
    				
    				return dto;
    			})
    			.toList();
    }

    @POST
    @Transactional
    public Response create(@Valid ProductMaterialRequestDTO dto) {
    	Product product = (Product) Product.findByIdOptional(dto.productId)
                .orElseThrow(() -> new NotFoundException("Product not found"));

        RawMaterial rawMaterial = (RawMaterial) RawMaterial.findByIdOptional(dto.rawMaterialId)
                .orElseThrow(() -> new NotFoundException("Raw material not found"));

        boolean alreadyExists = ProductMaterial.find(
                "product.id = ?1 and rawMaterial.id = ?2",
                dto.productId, dto.rawMaterialId
        ).firstResultOptional().isPresent();

        if (alreadyExists) {
            throw new BadRequestException("This raw material is already associated with the product");
        }

        ProductMaterial productMaterial = new ProductMaterial();
        productMaterial.product = product;
        productMaterial.rawMaterial = rawMaterial;
        productMaterial.quantityRequired = dto.quantityRequired;
        productMaterial.persist();

        return Response.ok().build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response update(@PathParam("id") Long id, @Valid ProductMaterialRequestDTO dto) {
    	ProductMaterial productMaterial = (ProductMaterial) ProductMaterial.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Association not found"));

    	productMaterial.quantityRequired = dto.quantityRequired;
    	productMaterial.persist();
    	
        return Response.ok().build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response delete(@PathParam("id") Long id) {
        ProductMaterial.deleteById(id);
        
        return Response.ok().build();
    }
}

