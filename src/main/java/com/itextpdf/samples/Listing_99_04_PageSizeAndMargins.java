package com.itextpdf.samples;

import com.itextpdf.basics.PdfException;
import com.itextpdf.core.geom.PageSize;
import com.itextpdf.core.pdf.PdfDocument;
import com.itextpdf.core.pdf.PdfWriter;
import com.itextpdf.model.Document;
import com.itextpdf.model.element.AreaBreak;
import com.itextpdf.model.element.Paragraph;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Listing_99_04_PageSizeAndMargins {

    static public final String DEST = "./target/test/resources/Listing_99_03_PageSizeAndMargins.pdf";

    public static void main(String args[]) throws IOException, PdfException {
        new Listing_99_04_PageSizeAndMargins().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws FileNotFoundException, PdfException {
        //Initialize writer
        FileOutputStream fos = new FileOutputStream(dest);
        PdfWriter writer = new PdfWriter(fos);

        //Initialize document with a certain page size.
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document doc = new Document(pdfDoc, new PageSize(100, 100, 10, 10, 10, 10));

        //Add paragraph to the document
        doc.add(new Paragraph("Hello")).add(new Paragraph("World"));

        //Add new page with the default page size which is 100*100 and [10, 10, 10, 10] margins for this document.
        doc.add(new AreaBreak());

        //Add paragraph to the document
        doc.add(new Paragraph("Hello")).add(new Paragraph("World"));

        //Add new A4 page
        doc.add(new AreaBreak(PageSize.A4));

        //Add paragraph to the document
        doc.add(new Paragraph("Hello")).add(new Paragraph("World"));

        //Close document
        doc.close();
    }


}
