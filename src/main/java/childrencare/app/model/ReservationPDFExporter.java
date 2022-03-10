package childrencare.app.model;


import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

public class ReservationPDFExporter {

    private List<ReservationModel> listReservations;

    public ReservationPDFExporter(List<ReservationModel> listReservations){
        this.listReservations = listReservations;
    }

    private void writeTableHeader(PdfPTable table){
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.LIGHT_GRAY);
        cell.setPadding(15);
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setColor(Color.WHITE);

        cell.setPhrase(new Phrase("#ID",font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("#Date",font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("#Customer name",font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("#Total cost",font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("#Status",font));
        table.addCell(cell);

    }

    private void writeTableData(PdfPTable table){
        for (ReservationModel reser:listReservations){
            table.addCell(String.valueOf(reser.getReservationId()));
            table.addCell(String.valueOf(reser.getDate()));
            table.addCell(String.valueOf(reser.getCustomer().getCustomer_user().getFullname()));
            table.addCell(String.valueOf(reser.getTotalReservationPrice()));
            table.addCell(String.valueOf(reser.getStatusReservation().getStatusName()));
        }

    }

    public void export(HttpServletResponse response) throws DocumentException, IOException {

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document,response.getOutputStream());
        document.open();
        document.add(new Paragraph("List of all reservation information- Total: "+listReservations.size() + " reservation"));
        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        table.setSpacingBefore(15);

        writeTableHeader(table);
        writeTableData(table);
        document.add(table);

        document.close();

    }
}
