package br.com.projedata.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

import br.com.projedata.dtos.RawMaterialDTO;
import br.com.projedata.models.RawMaterial;

@Path("/api/raw-materials")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RawMaterialController {

    @GET
    public List<RawMaterial> list() {
        return RawMaterial.listAll();
    }

    @GET
    @Path("/{id}")
    public RawMaterial get(@PathParam("id") Long id) {
        return (RawMaterial) RawMaterial.findByIdOptional(id)
        		.orElseThrow(() -> new NotFoundException("Product not found"));
    }

    @POST
    @Transactional
    public Response create(@Valid RawMaterialDTO dto) {
    	RawMaterial rawMaterial = new RawMaterial();
		 rawMaterial.code = dto.code;
		 rawMaterial.name = dto.name;
		 rawMaterial.stockQuantity = dto.stockQuantity;
		 rawMaterial.persist();
		 
		 return Response.ok().build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response update(@PathParam("id") Long id, @Valid RawMaterialDTO dto) {
    	RawMaterial rawMaterial = (RawMaterial) RawMaterial.findByIdOptional(id)
    			.orElseThrow(() -> new NotFoundException("Product not found"));
		 rawMaterial.code = dto.code;
		 rawMaterial.name = dto.name;
		 rawMaterial.stockQuantity = dto.stockQuantity;
		 rawMaterial.persist();
		 
		 return Response.ok().build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response delete(@PathParam("id") Long id) {
        RawMaterial.deleteById(id);
        
        return Response.ok().build();
    }
}
