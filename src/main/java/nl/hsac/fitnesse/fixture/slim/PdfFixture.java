package nl.hsac.fitnesse.fixture.slim;

import org.apache.commons.io.output.StringBuilderWriter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;

/**
 * Fixture to work with PDFs.
 */
public class PdfFixture extends SlimFixture {
    private final PDFTextStripper pdfStripper;

    public PdfFixture() {
        try {
            pdfStripper = new PDFTextStripper();
            pdfStripper.setLineSeparator("\n");
            pdfStripper.setPageEnd("\n");
        } catch (IOException e) {
            throw new SlimFixtureException("Unable to create PDF toolkit", e);
        }
    }

    /**
     * @param pdfFile pdf to get text from.
     * @return text in PDF (HTML preformatted).
     */
    public String pdfTextFormatted(String pdfFile) {
        String text = pdfText(pdfFile);
        return "<pre>" + text + "</pre>";
    }

    /**
     * @param pdfFile pdf to get text from.
     * @return text in PDF (HTML preformatted).
     */
    public String pdfText(String pdfFile) {
        String file = getFilePathFromWikiUrl(pdfFile);
        return getPdfText(file);
    }

    protected String getPdfText(String file) {
        try (PDDocument doc = PDDocument.load(new File(file))) {
            StringBuilderWriter writer = new StringBuilderWriter(2048);
            pdfStripper.writeText(doc, writer);
            String text = writer.toString();
            // spaces at end of line are not interesting
            text = text.replaceAll(" +\\n", "\n");
            return text;
        } catch (IOException e) {
            throw new SlimFixtureException("Unable to read PDF: " + file, e);
        }
    }
}