package com.taxisaeropuerto.taxisAeropuerto.dto;

import com.taxisaeropuerto.taxisAeropuerto.entity.Unidad;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UnidadRequest {

    @NotBlank(message = "La placa es obligatoria")
    private String placa;

    @NotBlank(message = "La serie es obligatoria")
    private String serie;

    private String fotografia;

    @NotNull(message = "El estado es obligatorio")
    private Unidad.EstadoUnidad estado;
}
