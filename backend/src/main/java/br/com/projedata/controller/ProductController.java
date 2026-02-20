package br.com.projedata.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

import br.com.projedata.dtos.ProductResponseDTO;
import br.com.projedata.dtos.RawMaterialResponseDTO;
import br.com.projedata.dtos.ProductMaterialResponseDTO;
import br.com.projedata.dtos.ProductRequestDTO;
import br.com.projedata.models.Product;
import br.com.projedata.models.ProductMaterial;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductController {
	@GET
	public List<ProductResponseDTO> list() {
		return Product.<Product>listAll()
				.stream()
				.map(p -> {
					ProductResponseDTO dto = new ProductResponseDTO();
					dto.id = p.id;
					dto.code = p.code;
					dto.name = p.name;
					dto.price = p.price;
					
					return dto;
				})
				.toList();
	}
	
	@GET
    @Path("/{id}")
    public ProductResponseDTO get(@PathParam("id") Long id) {
        Product product = (Product) Product.findByIdOptional(id)
        		.orElseThrow(() -> new NotFoundException("Product not found"));
        
        List<ProductMaterialResponseDTO> materialsDTOs = new ArrayList<ProductMaterialResponseDTO>();
        for (ProductMaterial productMaterial : product.materials) {
        	RawMaterialResponseDTO rawMaterialDTO = new RawMaterialResponseDTO();
        	rawMaterialDTO.id = productMaterial.rawMaterial.id;
        	rawMaterialDTO.name = productMaterial.rawMaterial.name;
        	rawMaterialDTO.code = productMaterial.rawMaterial.code;
        	rawMaterialDTO.stockQuantity = productMaterial.rawMaterial.stockQuantity;
        	
        	ProductMaterialResponseDTO productMaterialDTO = new ProductMaterialResponseDTO();
            productMaterialDTO.id = productMaterial.id;
            productMaterialDTO.rawMaterial = rawMaterialDTO;
            productMaterialDTO.quantityRequired = productMaterial.quantityRequired;
            
            materialsDTOs.add(productMaterialDTO);
		}
        
        ProductResponseDTO dto = new ProductResponseDTO();
        dto.id = product.id;
        dto.code = product.code;
        dto.name = product.name;
        dto.price = product.price;
        dto.materials = materialsDTOs;
        
        return dto;
    }

    @POST
    @Transactional
    public Response create(@Valid ProductRequestDTO dto) {
    	Product product = new Product();
    	product.code = dto.code;
    	product.name = dto.name;
    	product.price = dto.price;
    	product.persist();
    	
        return Response.ok().build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response update(@PathParam("id") Long id, ProductRequestDTO dto) {
    	Product product = Product.findById(id);
    	product.code = dto.code;
    	product.name = dto.name;
    	product.price = dto.price;
    	product.persist();
    	
        return Response.ok().build();
    }

    @DELETE
    @Path("/{id}")	
    @Transactional
    public Response delete(@PathParam("id") Long id) {
    	Product.deleteById(id);
    	
    	return Response.ok().build();
    }
}
