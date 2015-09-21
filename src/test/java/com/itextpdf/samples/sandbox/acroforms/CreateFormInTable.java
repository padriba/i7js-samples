/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/21028286/itext-editable-texfield-is-not-clickable
 */
package com.itextpdf.samples.sandbox.acroforms;

import com.itextpdf.canvas.PdfCanvas;
import com.itextpdf.core.pdf.PdfDocument;
import com.itextpdf.core.pdf.PdfWriter;
import com.itextpdf.core.testutils.annotations.type.SampleTest;
import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.forms.fields.PdfTextFormField;
import com.itextpdf.model.Document;
import com.itextpdf.model.element.Cell;
import com.itextpdf.model.element.Paragraph;
import com.itextpdf.model.element.Table;
import com.itextpdf.model.renderer.CellRenderer;
import com.itextpdf.samples.GenericTest;
import org.junit.experimental.categories.Category;

import java.io.File;
import java.io.FileOutputStream;

@Category(SampleTest.class)
public class CreateFormInTable extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/acroforms/create_form_in_table.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new CreateFormInTable().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        FileOutputStream fos = new FileOutputStream(dest);
        PdfWriter writer = new PdfWriter(fos);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document doc = new Document(pdfDoc);

        Table table = new Table(2);
        Cell cell;
        cell = new Cell().add(new Paragraph("Name:"));
        table.addCell(cell);
        cell = new Cell();
        cell.setNextRenderer(new MyCellRenderer(cell, "name"));
        table.addCell(cell);
        cell = new Cell().add(new Paragraph("Address"));
        table.addCell(cell);
        cell = new Cell();
        cell.setNextRenderer(new MyCellRenderer(cell, "address"));
        table.addCell(cell);
        doc.add(table);

        pdfDoc.close();
    }


    private class MyCellRenderer extends CellRenderer {
        protected String fieldName;

        public MyCellRenderer(Cell modelElement, String fieldName) {
            super(modelElement);
            this.fieldName = fieldName;
        }

        @Override
        public void draw(PdfDocument document, PdfCanvas canvas) {
            super.draw(document, canvas);
            PdfTextFormField field = PdfFormField.createText(document, getOccupiedAreaBBox(), "", fieldName);
            PdfAcroForm form = PdfAcroForm.getAcroForm(document, true);
            form.addField(field);
        }
    }
}