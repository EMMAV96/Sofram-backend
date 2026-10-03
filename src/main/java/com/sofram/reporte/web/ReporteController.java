package com.sofram.reporte.web;

import com.sofram.reporte.application.ReporteActividadService;
import com.sofram.reporte.application.ReporteClinicoService;
import com.sofram.reporte.application.ReporteOcupacionService;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reportes")
public class ReporteController {

    private final ReporteActividadService reporteActividadService;
    private final ReporteClinicoService reporteClinicoService;
    private final ReporteOcupacionService reporteOcupacionService;

    public ReporteController(
            ReporteActividadService reporteActividadService,
            ReporteClinicoService reporteClinicoService,
            ReporteOcupacionService reporteOcupacionService
    ) {
        this.reporteActividadService = reporteActividadService;
        this.reporteClinicoService = reporteClinicoService;
        this.reporteOcupacionService = reporteOcupacionService;
    }

    @GetMapping("/actividades/{actividadId}/pdf")
    public ResponseEntity<byte[]> generarReporteActividad(
            @PathVariable Long actividadId
    ) {

        byte[] pdf =
                reporteActividadService
                        .generarReporteActividad(actividadId);

        String filename =
                "reporte-actividad-" + actividadId + ".pdf";

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition
                                .attachment()
                                .filename(filename)
                                .build()
                                .toString()
                )
                .body(pdf);
    }

    @GetMapping("/clinico/residentes/{residenteId}/pdf")
    public ResponseEntity<byte[]> generarReporteClinico(
            @PathVariable Long residenteId
    ) {

        byte[] pdf =
                reporteClinicoService.generarReporteClinico(residenteId);

        String filename =
                "reporte-clinico-residente-" + residenteId + ".pdf";

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition
                                .attachment()
                                .filename(filename)
                                .build()
                                .toString()
                )
                .body(pdf);
    }

    @GetMapping("/ocupacion/pdf")
    public ResponseEntity<byte[]> generarReporteOcupacion() {

        byte[] pdf =
                reporteOcupacionService.generarReporteOcupacion();

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition
                                .attachment()
                                .filename("reporte-ocupacion.pdf")
                                .build()
                                .toString()
                )
                .body(pdf);
    }
}
