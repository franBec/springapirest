package com.fbecvort.springapirest.service;

import com.fbecvort.springapirest.dto.reporte.ReporteCuentaResponseDTO;

import java.util.Date;
import java.util.List;

public interface ReporteService {
    List<ReporteCuentaResponseDTO> makeReporte(Long clienteId, Date start, Date end);
}
