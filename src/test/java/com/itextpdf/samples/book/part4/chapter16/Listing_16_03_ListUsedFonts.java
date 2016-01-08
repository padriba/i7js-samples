package com.itextpdf.samples.book.part4.chapter16;

import com.itextpdf.core.pdf.PdfDictionary;
import com.itextpdf.core.pdf.PdfDocument;
import com.itextpdf.core.pdf.PdfName;
import com.itextpdf.core.pdf.PdfReader;
import com.itextpdf.core.testutils.annotations.type.SampleTest;
import com.itextpdf.samples.GenericTest;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Ignore;
import org.junit.experimental.categories.Category;

@Ignore
@Category(SampleTest.class)
public class Listing_16_03_ListUsedFonts extends GenericTest {
    public static final String DEST = "./target/test/resources/book/part4/chapter16/Listing_16_03_ListUsedFonts.pdf";
    public static final String RESULT = "./target/test/resources/book/part4/chapter16/Listing_16_03_ListUsedFonts.txt";
    // Note that currently Listing_11_01_FontTypes is labeled with TODO
    public static final String FONT_TYPES = "./src/test/resources/book/part3/chapter11/cmp_Listing_11_01_FontTypes.pdf";

    public static void main(String args[]) throws Exception {
        new Listing_16_03_ListUsedFonts().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws Exception {
        Set<String> set = listFonts(FONT_TYPES);
        PrintWriter out = new PrintWriter(new FileOutputStream(RESULT));
        for (String fontname : set)
            out.println(fontname);
        out.flush();
        out.close();
    }

    /**
     * Creates a Set containing information about the fonts in the src PDF file.
     *
     * @param src the path to a PDF file
     * @throws IOException
     */
    public Set<String> listFonts(String src) throws IOException {
        Set<String> set = new TreeSet<String>();
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(src));
        PdfDictionary resources;
        for (int k = 1; k <= pdfDoc.getNumOfPages(); ++k) {
            resources = pdfDoc.getPage(k).getPdfObject().getAsDictionary(PdfName.Resources);
            processResource(set, resources);
        }
        pdfDoc.close();
        return set;
    }

    /**
     * Extracts the font names from page or XObject resources.
     *
     * @param set the set with the font names
     */
    public static void processResource(Set<String> set, PdfDictionary resource) {
        if (resource == null)
            return;
        PdfDictionary xobjects = resource.getAsDictionary(PdfName.XObject);
        if (xobjects != null) {
            for (PdfName key : xobjects.keySet()) {
                processResource(set, xobjects.getAsDictionary(key));
            }
        }
        PdfDictionary fonts = resource.getAsDictionary(PdfName.Font);
        if (fonts == null)
            return;
        PdfDictionary font;
        for (PdfName key : fonts.keySet()) {
            font = fonts.getAsDictionary(key);
            String name = font.getAsName(PdfName.BaseFont).toString();
            if (name.length() > 8 && name.charAt(7) == '+') {
                name = String.format("%s subset (%s)", name.substring(8), name.substring(1, 7));
            } else {
                name = name.substring(1);
                PdfDictionary desc = font.getAsDictionary(PdfName.FontDescriptor);
                if (desc == null)
                    name += " nofontdescriptor";
                else if (desc.get(PdfName.FontFile) != null)
                    name += " (Type 1) embedded";
                else if (desc.get(PdfName.FontFile2) != null)
                    name += " (TrueType) embedded";
                else if (desc.get(PdfName.FontFile3) != null)
                    name += " (" + font.getAsName(PdfName.Subtype).toString().substring(1) + ") embedded";
            }
            set.add(name);
        }
    }
}