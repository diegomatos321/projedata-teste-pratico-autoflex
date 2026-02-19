package br.com.projedata.dtos;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProductResponseDTO {
	
	public Long id;

    public String code;

    public String name;
    
    public BigDecimal price;

    public List<ProductMaterialResponseDTO> materials = new ArrayList();
}
