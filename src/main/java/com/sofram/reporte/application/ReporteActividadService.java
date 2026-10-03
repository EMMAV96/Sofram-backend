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
import com.sofram.actividad.application.ActividadService;
import com.sofram.actividad.domain.Actividad;
import com.sofram.actividad.domain.ParticipacionActividad;
import com.sofram.actividad.infrastructure.persistence.ParticipacionActividadRepository;
import com.sofram.personal.domain.Empleado;
import com.sofram.residente.domain.Residente;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

@Service
public class ReporteActividadService {

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter TIME_FORMAT =
            DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter DATE_TIME_FORMAT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final ActividadService actividadService;
    private final ParticipacionActividadRepository participacionRepository;

    public ReporteActividadService(
            ActividadService actividadService,
            ParticipacionActividadRepository participacionRepository
    ) {
        this.actividadService = actividadService;
        this.participacionRepository = participacionRepository;
    }

    @Transactional(readOnly = true)
    public byte[] generarReporteActividad(Long actividadId) {

        Actividad actividad =
                actividadService.buscarEntidadPorId(actividadId);

        List<ParticipacionActividad> participaciones =
                participacionRepository
                        .findByActividadIdOrderByIdAsc(actividadId)
                        .stream()
                        .sorted(Comparator.comparing(
                                this::nombreResidente,
                                Comparator.nullsLast(String::compareTo)
                        ))
                        .toList();

        long presentes = participaciones.stream()
                .filter(participacion ->
                        Boolean.TRUE.equals(
                                participacion.getAsistencia()
                        )
                )
                .count();
        long ausentes = participaciones.size() - presentes;

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
            addActivitySection(document, actividad);
            addParticipationSummary(
                    document,
                    participaciones.size(),
                    presentes,
                    ausentes
            );
            addParticipantsTable(document, participaciones);

            document.close();

            return outputStream.toByteArray();
        } catch (Exception exception) {
            throw new IllegalStateException(
                    "No se pudo generar el reporte de actividad",
                    exception
            );
        }
    }

    private void addTitle(Document document) throws DocumentException {
        Paragraph title = new Paragraph(
                "SOFRAM\nReporte de Actividad",
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

    private void addActivitySection(
            Document document,
            Actividad actividad
    ) throws DocumentException {

        addSectionTitle(document, "DATOS DE LA ACTIVIDAD");

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{1.4F, 2.6F});

        addRow(table, "Taller", value(actividad.getTaller()));
        addRow(table, "Nombre de la actividad",
                value(actividad.getNombre()));
        addRow(table, "Tipo", value(actividad.getTipo()));
        addRow(table, "Descripción",
                value(actividad.getDescripcion()));
        addRow(table, "Estado", value(actividad.getEstado()));
        addRow(table, "Fecha",
                value(actividad.getDetalleCalendario().getFecha()));
        addRow(table, "Hora de inicio",
                value(actividad.getDetalleCalendario().getHoraInicio()));
        addRow(table, "Hora de fin",
                value(actividad.getDetalleCalendario().getHoraFin()));
        addRow(table, "Duración", value(actividad.getDuracion()));
        addRow(table, "Responsable",
                responsibleName(actividad.getEmpleado()));
        addRow(table, "Cupo máximo",
                value(actividad.getCupoMaximo()));

        table.setSpacingAfter(12);
        document.add(table);
    }

    private void addParticipationSummary(
            Document document,
            int totalParticipantes,
            long presentes,
            long ausentes
    ) throws DocumentException {

        addSectionTitle(document, "PARTICIPACIÓN");

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{1.4F, 2.6F});

        addRow(table, "Total de participantes",
                String.valueOf(totalParticipantes));
        addRow(table, "Presentes", String.valueOf(presentes));
        addRow(table, "Ausentes", String.valueOf(ausentes));

        table.setSpacingAfter(12);
        document.add(table);
    }

    private void addParticipantsTable(
            Document document,
            List<ParticipacionActividad> participaciones
    ) throws DocumentException {

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{2.4F, 1.0F});

        addHeaderCell(table, "Residente");
        addHeaderCell(table, "Asistencia");

        if (participaciones.isEmpty()) {
            PdfPCell cell = new PdfPCell(
                    new Phrase("Sin registros.", secondaryFont())
            );
            cell.setColspan(2);
            cell.setPadding(6);
            table.addCell(cell);
        } else {
            for (ParticipacionActividad participacion : participaciones) {
                addBodyCell(table, nombreResidente(participacion));
                addBodyCell(table, asistencia(participacion));
            }
        }

        document.add(table);
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

    private void addHeaderCell(
            PdfPTable table,
            String text
    ) {

        PdfPCell cell = new PdfPCell(
                new Phrase(text, boldFont())
        );
        cell.setPadding(6);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
    }

    private void addBodyCell(
            PdfPTable table,
            String text
    ) {

        PdfPCell cell = new PdfPCell(
                new Phrase(text, normalFont())
        );
        cell.setPadding(5);
        table.addCell(cell);
    }

    private String responsibleName(Empleado empleado) {
        return value(empleado.getNombre())
                + " "
                + value(empleado.getApellido())
                + " - "
                + value(empleado.getCargo().getNombre());
    }

    private String nombreResidente(
            ParticipacionActividad participacion
    ) {
        Residente residente = participacion.getResidente();

        return value(residente.getApellido())
                + ", "
                + value(residente.getNombre());
    }

    private String asistencia(
            ParticipacionActividad participacion
    ) {
        return Boolean.TRUE.equals(participacion.getAsistencia())
                ? "Presente"
                : "Ausente";
    }

    private String value(String value) {
        if (value == null || value.isBlank()) {
            return "-";
        }
        return value;
    }

    private String value(Integer value) {
        if (value == null) {
            return "-";
        }
        return value.toString();
    }

    private String value(LocalDate value) {
        if (value == null) {
            return "-";
        }
        return value.format(DATE_FORMAT);
    }

    private String value(LocalTime value) {
        if (value == null) {
            return "-";
        }
        return value.format(TIME_FORMAT);
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
