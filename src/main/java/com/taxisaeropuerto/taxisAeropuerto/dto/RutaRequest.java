package com.taxisaeropuerto.taxisAeropuerto.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class RutaRequest {

    @NotBlank(message = "El campo inicio es obligatorio")
    private String inicio;

    @NotBlank(message = "El campo fin es obligatorio")
    private String fin;

    @NotNull(message = "El campo precio es obligatorio")
    @Positive(message = "El precio debe ser mayor que cero")
    private Double precio;
}
