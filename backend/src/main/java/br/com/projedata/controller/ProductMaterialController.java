package br.com.projedata.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

import br.com.projedata.dtos.ProductMaterialDTO;
import br.com.projedata.models.Product;
import br.com.projedata.models.ProductMaterial;
import br.com.projedata.models.RawMaterial;

@Path("/api/product-materials")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductMaterialController {

    @GET
    public List<ProductMaterial> listAll() {
        return ProductMaterial.listAll();
    }

    @GET
    @Path("/product/{productId}")
    public List<ProductMaterial> listByProduct(@PathParam("productId") Long productId) {
    	return  ProductMaterial.list("product.id", productId);
    }

    @POST
    @Transactional
    public Response create(@Valid ProductMaterialDTO dto) {
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
    public Response update(@PathParam("id") Long id, @Valid ProductMaterialDTO dto) {
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

