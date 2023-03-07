package com.caito.tutorialwebflux.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductoNuevoDTO {
    @NotBlank(message = "el nombre es requerido")
    private String name;
    @Min(value = 1, message = "el precio debe ser mayor que 0")
    private Float price;
}
