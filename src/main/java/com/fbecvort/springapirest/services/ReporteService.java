package com.fbecvort.springapirest.services;

import com.fbecvort.springapirest.dtos.reporte.ReporteCuentaResponseDTO;

import java.util.Date;
import java.util.List;

public interface ReporteService {
    List<ReporteCuentaResponseDTO> makeReporte(Long clienteId, Date start, Date end);
}
