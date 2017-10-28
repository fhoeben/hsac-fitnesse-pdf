package nl.hsac.fitnesse.fixture.slim;

import nl.hsac.fitnesse.fixture.util.FileUtil;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PdfFixtureTest {

    @Test
    public void canGetText() {
        PdfFixture pdfFixture = new PdfFixture();
        File f = new File("src/test/resources/pdf-sample.pdf");
        assertTrue(f.exists());

        String text = pdfFixture.pdfText(f.getAbsolutePath());

        String expectedText = FileUtil.loadFile("pdf-sample.txt");
        assertEquals(expectedText, text);
    }
}