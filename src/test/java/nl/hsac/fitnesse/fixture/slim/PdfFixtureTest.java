package nl.hsac.fitnesse.fixture.slim;

import nl.hsac.fitnesse.fixture.util.FileUtil;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PdfFixtureTest {
    private final PdfFixture pdfFixture = new PdfFixture();

    @Test
    public void canGetText() {
        String doc = "pdf-sample";
        String file = getInputPdf(doc);
        String expectedText = FileUtil.loadFile(doc + ".txt");

        String text = pdfFixture.pdfText(file);

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