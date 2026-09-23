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
import com.sofram.residente.application.HabitacionService;
import com.sofram.residente.web.dto.HabitacionResponse;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

@Service
public class ReporteOcupacionService {

    private static final DateTimeFormatter DATE_TIME_FORMAT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final DecimalFormat PERCENT_FORMAT =
            new DecimalFormat("0.00");

    private final HabitacionService habitacionService;

    public ReporteOcupacionService(
            HabitacionService habitacionService
    ) {
        this.habitacionService = habitacionService;
    }

    public byte[] generarReporteOcupacion() {

        List<HabitacionResponse> habitaciones =
                habitacionService
                        .listar()
                        .stream()
                        .sorted(Comparator.comparing(
                                HabitacionResponse::numero,
                                Comparator.nullsLast(String::compareTo)
                        ))
                        .toList();

        int capacidadTotal = habitaciones.stream()
                .map(HabitacionResponse::capacidad)
                .mapToInt(this::safeInteger)
                .sum();

        long ocupacionTotal = habitaciones.stream()
                .map(HabitacionResponse::ocupacionActual)
                .mapToLong(this::safeLong)
                .sum();

        int cuposDisponiblesTotal = habitaciones.stream()
                .map(HabitacionResponse::cuposDisponibles)
                .mapToInt(this::safeInteger)
                .sum();

        double porcentajeOcupacion =
                capacidadTotal == 0
                        ? 0
                        : ocupacionTotal * 100.0 / capacidadTotal;

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
            addSummarySection(
                    document,
                    habitaciones.size(),
                    capacidadTotal,
                    ocupacionTotal,
                    cuposDisponiblesTotal,
                    porcentajeOcupacion
            );
            addRoomsTable(document, habitaciones);

            document.close();

            return outputStream.toByteArray();
        } catch (Exception exception) {
            throw new IllegalStateException(
                    "No se pudo generar el reporte de ocupación",
                    exception
            );
        }
    }

    private void addTitle(Document document) throws DocumentException {
        Paragraph title = new Paragraph(
                "SOFRAM\nREPORTE DE OCUPACIÓN",
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

    private void addSummarySection(
            Document document,
            int totalHabitaciones,
            int capacidadTotal,
            long ocupacionTotal,
            int cuposDisponiblesTotal,
            double porcentajeOcupacion
    ) throws DocumentException {

        addSectionTitle(document, "RESUMEN GENERAL");

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{1.8F, 2.2F});

        addRow(table, "Total de habitaciones",
                String.valueOf(totalHabitaciones));
        addRow(table, "Capacidad total",
                String.valueOf(capacidadTotal));
        addRow(table, "Ocupación total actual",
                String.valueOf(ocupacionTotal));
        addRow(table, "Cupos disponibles totales",
                String.valueOf(cuposDisponiblesTotal));
        addRow(table, "Porcentaje de ocupación general",
                PERCENT_FORMAT.format(porcentajeOcupacion) + "%");

        table.setSpacingAfter(12);
        document.add(table);
    }

    private void addRoomsTable(
            Document document,
            List<HabitacionResponse> habitaciones
    ) throws DocumentException {

        addSectionTitle(document, "DETALLE DE HABITACIONES");

        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{
                1.2F,
                1.4F,
                1.4F,
                1.0F,
                1.0F,
                1.4F
        });

        addHeaderCell(table, "Habitación");
        addHeaderCell(table, "Tipo");
        addHeaderCell(table, "Estado");
        addHeaderCell(table, "Capacidad");
        addHeaderCell(table, "Ocupación");
        addHeaderCell(table, "Cupos disponibles");

        if (habitaciones.isEmpty()) {
            PdfPCell cell = new PdfPCell(
                    new Phrase("Sin registros.", secondaryFont())
            );
            cell.setColspan(6);
            cell.setPadding(6);
            table.addCell(cell);
        } else {
            for (HabitacionResponse habitacion : habitaciones) {
                addBodyCell(table, value(habitacion.numero()));
                addBodyCell(table, value(habitacion.tipo()));
                addBodyCell(table, value(habitacion.estado()));
                addBodyCell(table, value(habitacion.capacidad()));
                addBodyCell(table, value(habitacion.ocupacionActual()));
                addBodyCell(table, value(habitacion.cuposDisponibles()));
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

    private String value(Long value) {
        if (value == null) {
            return "-";
        }
        return value.toString();
    }

    private int safeInteger(Integer value) {
        if (value == null) {
            return 0;
        }
        return value;
    }

    private long safeLong(Long value) {
        if (value == null) {
            return 0;
        }
        return value;
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
