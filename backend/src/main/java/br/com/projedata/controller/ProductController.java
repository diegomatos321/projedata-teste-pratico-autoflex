package br.com.projedata.controller;

import java.util.List;

import br.com.projedata.dtos.ProductDTO;
import br.com.projedata.models.Product;
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
	public List<Product> list() {
		return Product.listAll();
	}
	
	@GET
    @Path("/{id}")
    public Product get(@PathParam("id") Long id) {
        return (Product) Product.findByIdOptional(id)
        		.orElseThrow(() -> new NotFoundException("Product not found"));
    }

    @POST
    @Transactional
    public Response create(@Valid ProductDTO dto) {
    	Product product = new Product();
    	product.code = dto.code;
    	product.name = dto.name;
    	product.price = dto.price;
    	product.persist();
    	
        return Response.created(null).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response update(@PathParam("id") Long id, ProductDTO dto) {
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
