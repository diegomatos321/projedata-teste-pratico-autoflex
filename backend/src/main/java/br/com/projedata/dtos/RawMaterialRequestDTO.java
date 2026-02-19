package br.com.projedata.dtos;

import jakarta.validation.constraints.*;

public class RawMaterialRequestDTO {
	
    @NotBlank
    @Size(max = 40)
    public String code;

    @NotBlank
    @Size(max = 120)
    public String name;

    @NotNull
    @Min(0)
    public Integer stockQuantity;
}
