package br.com.projedata.dtos;

import jakarta.validation.constraints.*;

public class ProductMaterialRequestDTO {

    @NotNull
    public Long productId;

    @NotNull
    public Long rawMaterialId;

    @NotNull
    @Min(1)
    public Integer quantityRequired;
}
