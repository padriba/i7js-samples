package com.itextpdf.samples.book.part3.chapter10;

import com.itextpdf.basics.image.Image;
import com.itextpdf.basics.image.ImageFactory;
import com.itextpdf.core.pdf.PdfDocument;
import com.itextpdf.core.pdf.PdfReader;
import com.itextpdf.core.pdf.PdfWriter;
import com.itextpdf.core.testutils.annotations.type.SampleTest;
import com.itextpdf.model.Document;
import com.itextpdf.samples.GenericTest;

import java.io.IOException;

import org.junit.experimental.categories.Category;

@Category(SampleTest.class)
public class Listing_10_18_ImageMask extends GenericTest {
    public static final String DEST = "./target/test/resources/book/part3/chapter10/Listing_10_18_ImageMask.pdf";
    /**
     * The resulting PDF file.
     */
    public static final String RESULT1
            = "./target/test/resources/book/part3/chapter10/hardmask.pdf";
    /**
     * The resulting PDF file.
     */
    public static final String RESULT2
            = "./target/test/resources/book/part3/chapter10/softmask.pdf";
    /**
     * One of the resources.
     */
    public static final String RESOURCE
            = "./src/test/resources/book/part3/chapter10/bruno.jpg";

    public static void main(String args[]) throws IOException {
        new Listing_10_18_ImageMask().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException {
        byte circledata[] = {(byte) 0x3c, (byte) 0x7e, (byte) 0xff,
                (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0x7e,
                (byte) 0x3c};
        Image mask = ImageFactory.getImage(8, 8, 1, 1, circledata, null);
        mask.makeMask();
        mask.setInverted(true);
        // TODO Acrobat opens the result file with problems (Foxit sees no errors)
        createPdf(RESULT1, mask);
        byte gradient[] = new byte[256];
        for (int i = 0; i < 256; i++)
            gradient[i] = (byte) i;
        mask = ImageFactory.getImage(256, 1, 1, 8, gradient, null);
        mask.makeMask();
        createPdf(RESULT2, mask);
        // Create a file to compare via CompareTool
        concatenateResults(DEST, new String[]{RESULT1, RESULT2});
    }

    public void createPdf(String filename, Image mask) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(filename));
        Document doc = new Document(pdfDoc);
        Image img = ImageFactory.getImage(RESOURCE);
        img.setImageMask(mask);
        doc.add(new com.itextpdf.model.element.Image(img));
        doc.close();
    }

    // Only for testing reasons
    protected void concatenateResults(String dest, String[] names) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        PdfDocument tempDoc;
        for (String name : names) {
            tempDoc = new PdfDocument(new PdfReader(name));
            tempDoc.copyPages(1, tempDoc.getNumOfPages(), pdfDoc);
            tempDoc.close();
        }
        pdfDoc.close();
    }
}