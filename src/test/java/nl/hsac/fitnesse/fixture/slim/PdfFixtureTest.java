package nl.hsac.fitnesse.fixture.slim;

import nl.hsac.fitnesse.fixture.util.FileUtil;
import org.junit.Test;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PdfFixtureTest {
    private final PdfFixture pdfFixture = new PdfFixture();

    @Test
    public void canGetNoOfPages() {
        String doc = "latex-sample";
        String file = getInputPdf(doc);

        int pageCount = pdfFixture.numberOfPagesIn(file);

        assertEquals(10, pageCount);
    }

    @Test
    public void canGetDocumentInformation() {
        String doc = "latex-sample";
        String file = getInputPdf(doc);

        pdfFixture.getDocumentInformationHelper().setTimeZone("UTC");
        Map<String, Object> docInfo = pdfFixture.pdfDocumentInformation(file);

        assertEquals("{Author=, Title=, Subject=, Creator=TeX, Producer=pdfTeX, Keywords=, CreationDate=1999-02-20 21:20}",
                docInfo.toString());
        assertEquals(docInfo.getClass(), LinkedHashMap.class);
    }

    @Test
    public void canGetText() {
        String doc = "pdf-sample";
        String file = getInputPdf(doc);
        String expectedText = FileUtil.loadFile(doc + ".txt");

        String text = pdfFixture.pdfText(file);

        assertEquals(expectedText, text);
    }

    @Test
    public void canGetTextFewPages() {
        String doc = "latex-sample";
        String file = getInputPdf(doc);
        String expectedText = FileUtil.loadFile(doc + "-3-6.txt");

        String text = pdfFixture.pdfTextFormattedFromPagesTo(file, 3, 6);

        assertEquals(expectedText, text);
    }

    @Test
    public void canGetTextFormatted() {
        String doc = "freemarker";
        String file = getInputPdf(doc);
        String expectedText = FileUtil.loadFile(doc + ".txt");

        String text = pdfFixture.pdfTextFormatted(file);

        assertEquals(expectedText, text);
    }

    private String getInputPdf(String doc) {
        File f = new File("src/test/resources/" + doc + ".pdf");
        assertTrue(f.exists());
        return f.getAbsolutePath();
    }
}