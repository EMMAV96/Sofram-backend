package com.sofram.reporte.application;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.sofram.gestionmedica.application.AtencionMedicaService;
import com.sofram.gestionmedica.application.DiagnosticoService;
import com.sofram.gestionmedica.application.EvaluacionService;
import com.sofram.gestionmedica.application.MedicacionService;
import com.sofram.gestionmedica.application.TratamientoService;
import com.sofram.gestionmedica.web.dto.AtencionMedicaResponse;
import com.sofram.gestionmedica.web.dto.DiagnosticoResponse;
import com.sofram.gestionmedica.web.dto.EvaluacionResponse;
import com.sofram.gestionmedica.web.dto.MedicacionResponse;
import com.sofram.gestionmedica.web.dto.TratamientoResponse;
import com.sofram.historiaclinica.application.HistoriaClinicaService;
import com.sofram.historiaclinica.web.dto.DetalleHistoriaClinicaResponse;
import com.sofram.historiaclinica.web.dto.HistoriaClinicaResponse;
import com.sofram.personal.application.PersonalService;
import com.sofram.personal.web.dto.EmpleadoResponse;
import com.sofram.residente.application.ResidenteService;
import com.sofram.residente.web.dto.ResidenteResponse;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReporteClinicoService {

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter DATE_TIME_FORMAT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final ResidenteService residenteService;
    private final HistoriaClinicaService historiaClinicaService;
    private final AtencionMedicaService atencionMedicaService;
    private final EvaluacionService evaluacionService;
    private final DiagnosticoService diagnosticoService;
    private final TratamientoService tratamientoService;
    private final MedicacionService medicacionService;
    private final PersonalService personalService;

    public ReporteClinicoService(
            ResidenteService residenteService,
            HistoriaClinicaService historiaClinicaService,
            AtencionMedicaService atencionMedicaService,
            EvaluacionService evaluacionService,
            DiagnosticoService diagnosticoService,
            TratamientoService tratamientoService,
            MedicacionService medicacionService,
            PersonalService personalService
    ) {
        this.residenteService = residenteService;
        this.historiaClinicaService = historiaClinicaService;
        this.atencionMedicaService = atencionMedicaService;
        this.evaluacionService = evaluacionService;
        this.diagnosticoService = diagnosticoService;
        this.tratamientoService = tratamientoService;
        this.medicacionService = medicacionService;
        this.personalService = personalService;
    }

    public byte[] generarReporteClinico(Long residenteId) {

        ResidenteResponse residente =
                residenteService.buscarPorId(residenteId);
        HistoriaClinicaResponse historiaClinica =
                historiaClinicaService.buscarPorResidente(residenteId);

        List<DetalleHistoriaClinicaResponse> detalles =
                historiaClinicaService
                        .listarDetalles(historiaClinica.id())
                        .stream()
                        .sorted(Comparator.comparing(
                                DetalleHistoriaClinicaResponse::fecha,
                                Comparator.nullsLast(LocalDate::compareTo)
                        ))
                        .toList();

        List<AtencionMedicaResponse> atenciones =
                atencionMedicaService
                        .listarPorResidente(residenteId)
                        .stream()
                        .sorted(Comparator.comparing(
                                AtencionMedicaResponse::fecha,
                                Comparator.nullsLast(LocalDate::compareTo)
                        ))
                        .toList();

        try (ByteArrayOutputStream outputStream =
                     new ByteArrayOutputStream()) {

            Document document = new Document(
                    PageSize.A4,
                    40,
                    40,
                    45,
                    45
            );
            PdfWriter.getInstance(document, outputStream);
            document.open();

            addTitle(document);
            addGeneratedAt(document);
            addResidentSection(document, residente);
            addHistoriaClinicaSection(document, historiaClinica);
            addDetallesSection(document, detalles);
            addAtencionesSection(document, atenciones);

            document.close();

            return outputStream.toByteArray();
        } catch (Exception exception) {
            throw new IllegalStateException(
                    "No se pudo generar el reporte clínico",
                    exception
            );
        }
    }

    private void addTitle(Document document) throws DocumentException {
        Paragraph title = new Paragraph(
                "SOFRAM\nREPORTE CLÍNICO INDIVIDUAL",
                titleFont()
        );
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(12);
        document.add(title);
    }

    private void addGeneratedAt(Document document)
            throws DocumentException {
        Paragraph generatedAt = new Paragraph(
                "Generado: "
                        + LocalDateTime.now().format(DATE_TIME_FORMAT),
                secondaryFont()
        );
        generatedAt.setAlignment(Element.ALIGN_RIGHT);
        generatedAt.setSpacingAfter(12);
        document.add(generatedAt);
    }

    private void addResidentSection(
            Document document,
            ResidenteResponse residente
    ) throws DocumentException {

        addSectionTitle(document, "A. DATOS DEL RESIDENTE");

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{1.2F, 2.8F});

        addRow(table, "Nombre y apellido",
                value(residente.nombre()) + " " + value(residente.apellido()));
        addRow(table, "DNI", value(residente.dni()));
        addRow(table, "Fecha de nacimiento",
                value(residente.fechaNacimiento()));
        addRow(table, "Obra social", value(residente.obraSocial()));
        addRow(table, "Habitación", value(residente.habitacionNumero()));
        addRow(table, "Estado actual", value(residente.estadoActual()));
        addRow(table, "Fecha de ingreso", value(residente.fechaIngreso()));
        addRow(table, "Fecha de egreso", value(residente.fechaEgreso()));

        table.setSpacingAfter(12);
        document.add(table);
    }

    private void addHistoriaClinicaSection(
            Document document,
            HistoriaClinicaResponse historiaClinica
    ) throws DocumentException {

        addSectionTitle(document, "B. HISTORIA CLÍNICA");

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{1.2F, 2.8F});

        addRow(table, "Fecha de creación",
                value(historiaClinica.fechaCreacion()));
        addRow(table, "Observaciones",
                value(historiaClinica.observaciones()));
        addRow(table, "Antecedentes personales",
                value(historiaClinica.antecedentesPersonales()));
        addRow(table, "Antecedentes familiares",
                value(historiaClinica.antecedentesFamiliares()));
        addRow(table, "Alergias", value(historiaClinica.alergias()));

        table.setSpacingAfter(12);
        document.add(table);
    }

    private void addDetallesSection(
            Document document,
            List<DetalleHistoriaClinicaResponse> detalles
    ) throws DocumentException {

        addSectionTitle(
                document,
                "C. EVOLUCIÓN / DETALLES DE HISTORIA"
        );

        if (detalles.isEmpty()) {
            addNoRecords(document);
            return;
        }

        for (DetalleHistoriaClinicaResponse detalle : detalles) {
            addSubTitle(
                    document,
                    "Detalle - " + value(detalle.fecha())
            );
            addParagraph(
                    document,
                    "Observaciones: " + value(detalle.observaciones())
            );

            addEvaluaciones(document, detalle.id());
            addDiagnosticos(document, detalle.id());
            addTratamientos(document, detalle.id());
            addMedicaciones(document, detalle.id());
        }
    }

    private void addEvaluaciones(
            Document document,
            Long detalleId
    ) throws DocumentException {

        addSubTitle(document, "EVALUACIONES");

        List<EvaluacionResponse> evaluaciones =
                evaluacionService.listarPorDetalleHistoria(detalleId);

        if (evaluaciones.isEmpty()) {
            addNoRecords(document);
            return;
        }

        for (EvaluacionResponse evaluacion : evaluaciones) {
            addParagraph(
                    document,
                    "Tipo: " + value(evaluacion.tipoEvaluacion())
                            + "\nDescripción: "
                            + value(evaluacion.descripcion())
                            + "\nPlan de intervención: "
                            + value(evaluacion.planIntervencion())
            );
        }
    }

    private void addDiagnosticos(
            Document document,
            Long detalleId
    ) throws DocumentException {

        addSubTitle(document, "DIAGNÓSTICOS");

        List<DiagnosticoResponse> diagnosticos =
                diagnosticoService.listarPorDetalleHistoria(detalleId);

        if (diagnosticos.isEmpty()) {
            addNoRecords(document);
            return;
        }

        for (DiagnosticoResponse diagnostico : diagnosticos) {
            addParagraph(
                    document,
                    value(diagnostico.descripcion())
            );
        }
    }

    private void addTratamientos(
            Document document,
            Long detalleId
    ) throws DocumentException {

        addSubTitle(document, "TRATAMIENTOS");

        List<TratamientoResponse> tratamientos =
                tratamientoService.listarPorDetalleHistoria(detalleId);

        if (tratamientos.isEmpty()) {
            addNoRecords(document);
            return;
        }

        for (TratamientoResponse tratamiento : tratamientos) {
            addParagraph(
                    document,
                    "Nombre: " + value(tratamiento.nombre())
                            + "\nDescripción: "
                            + value(tratamiento.descripcion())
            );
        }
    }

    private void addMedicaciones(
            Document document,
            Long detalleId
    ) throws DocumentException {

        addSubTitle(document, "MEDICACIONES");

        List<MedicacionResponse> medicaciones =
                medicacionService.listarPorDetalleHistoria(detalleId);

        if (medicaciones.isEmpty()) {
            addNoRecords(document);
            return;
        }

        for (MedicacionResponse medicacion : medicaciones) {
            addParagraph(
                    document,
                    "Nombre: " + value(medicacion.nombre())
                            + "\nDosis: "
                            + value(medicacion.dosis())
                            + "\nFrecuencia: "
                            + value(medicacion.frecuencia())
            );
        }
    }

    private void addAtencionesSection(
            Document document,
            List<AtencionMedicaResponse> atenciones
    ) throws DocumentException {

        addSectionTitle(document, "D. ATENCIONES MÉDICAS");

        if (atenciones.isEmpty()) {
            addNoRecords(document);
            return;
        }

        Map<Long, EmpleadoResponse> empleadosPorId = new HashMap<>();

        for (AtencionMedicaResponse atencion : atenciones) {
            EmpleadoResponse empleado =
                    empleadosPorId.computeIfAbsent(
                            atencion.empleadoId(),
                            personalService::obtenerEmpleado
                    );

            addSubTitle(
                    document,
                    "Atención - " + value(atencion.fecha())
            );
            addParagraph(
                    document,
                    "Motivo: " + value(atencion.motivo())
                            + "\nTipo de intervención: "
                            + value(atencion.tipoIntervencion())
                            + "\nObservaciones: "
                            + value(atencion.observaciones())
                            + "\nProfesional: "
                            + professionalName(empleado)
            );
        }
    }

    private String professionalName(EmpleadoResponse empleado) {
        return value(empleado.nombre())
                + " "
                + value(empleado.apellido())
                + " - "
                + value(empleado.cargoNombre());
    }

    private void addSectionTitle(
            Document document,
            String text
    ) throws DocumentException {

        Paragraph paragraph = new Paragraph(text, sectionFont());
        paragraph.setSpacingBefore(10);
        paragraph.setSpacingAfter(8);
        document.add(paragraph);
    }

    private void addSubTitle(
            Document document,
            String text
    ) throws DocumentException {

        Paragraph paragraph = new Paragraph(text, subTitleFont());
        paragraph.setSpacingBefore(6);
        paragraph.setSpacingAfter(4);
        document.add(paragraph);
    }

    private void addParagraph(
            Document document,
            String text
    ) throws DocumentException {

        Paragraph paragraph = new Paragraph(text, normalFont());
        paragraph.setSpacingAfter(6);
        document.add(paragraph);
    }

    private void addNoRecords(Document document)
            throws DocumentException {
        Paragraph paragraph = new Paragraph(
                "Sin registros.",
                secondaryFont()
        );
        paragraph.setSpacingAfter(6);
        document.add(paragraph);
    }

    private void addRow(
            PdfPTable table,
            String label,
            String value
    ) {

        PdfPCell labelCell = new PdfPCell(
                new Phrase(label, boldFont())
        );
        labelCell.setPadding(6);

        PdfPCell valueCell = new PdfPCell(
                new Phrase(value, normalFont())
        );
        valueCell.setPadding(6);

        table.addCell(labelCell);
        table.addCell(valueCell);
    }

    private String value(String value) {
        if (value == null || value.isBlank()) {
            return "-";
        }
        return value;
    }

    private String value(LocalDate value) {
        if (value == null) {
            return "-";
        }
        return value.format(DATE_FORMAT);
    }

    private Font titleFont() {
        return FontFactory.getFont(
                FontFactory.HELVETICA_BOLD,
                16
        );
    }

    private Font sectionFont() {
        return FontFactory.getFont(
                FontFactory.HELVETICA_BOLD,
                12
        );
    }

    private Font subTitleFont() {
        return FontFactory.getFont(
                FontFactory.HELVETICA_BOLD,
                10
        );
    }

    private Font boldFont() {
        return FontFactory.getFont(
                FontFactory.HELVETICA_BOLD,
                9
        );
    }

    private Font normalFont() {
        return FontFactory.getFont(
                FontFactory.HELVETICA,
                9
        );
    }

    private Font secondaryFont() {
        return FontFactory.getFont(
                FontFactory.HELVETICA_OBLIQUE,
                8
        );
    }
}
