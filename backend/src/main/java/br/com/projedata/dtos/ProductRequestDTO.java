package br.com.projedata.dtos;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProductRequestDTO {
	
    @NotBlank
    @Size(max = 40)
    public String code;

    @NotBlank
    @Size(max = 120)
    public String name;

    @NotNull
    @DecimalMin("0.01")
    public BigDecimal price;
}
