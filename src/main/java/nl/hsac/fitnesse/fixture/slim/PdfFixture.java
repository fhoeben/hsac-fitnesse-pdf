package nl.hsac.fitnesse.fixture.slim;

import nl.hsac.fitnesse.fixture.util.ThrowingFunction;
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
        this(createPdfTextStripper());
    }

    public PdfFixture(PDFTextStripper pdfTextStripper) {
        pdfStripper = pdfTextStripper;
    }

    /**
     * @param pdfFile pdf to number of pages from.
     * @return number of pages in file.
     */
    public int numberOfPagesIn(String pdfFile) {
        String file = getFilePathFromWikiUrl(pdfFile);
        return handleDoc(file, doc -> doc.getNumberOfPages());
    }

    /**
     * @param pdfFile pdf to get text from.
     * @return text in PDF (HTML preformatted).
     */
    public String pdfTextFormatted(String pdfFile) {
        String text = pdfText(pdfFile);
        return getEnvironment().getHtml(text);
    }

    /**
     * @param pdfFile pdf to get text from.
     * @return text in PDF.
     */
    public String pdfText(String pdfFile) {
        return pdfTextFromPagesTo(pdfFile, 1, Integer.MAX_VALUE);
    }

    /**
     * @param pdfFile pdf to get text from.
     * @param startPage page number to start at (1 based).
     * @param endPage last page to extract text from (inclusive).
     * @return text in PDF (HTML preformatted).
     */
    public String pdfTextFormattedFromPagesTo(String pdfFile, int startPage, int endPage) {
        String text = pdfTextFromPagesTo(pdfFile, startPage, endPage);
        return getEnvironment().getHtml(text);
    }

    /**
     * @param pdfFile pdf to get text from.
     * @param startPage page number to start at (1 based).
     * @param endPage last page to extract text from (inclusive).
     * @return text in PDF.
     */
    public String pdfTextFromPagesTo(String pdfFile, int startPage, int endPage) {
        String file = getFilePathFromWikiUrl(pdfFile);
        PDFTextStripper stripper = getPdfStripper();
        stripper.setStartPage(startPage);
        stripper.setEndPage(endPage);
        return getPdfText(file);
    }

    protected String getPdfText(String file) {
        return handleDoc(file, doc -> {
            StringBuilderWriter writer = new StringBuilderWriter(2048);
            getPdfStripper().writeText(doc, writer);
            String text = writer.toString();
            text = postProcessText(text);
            return text;
        });
    }

    protected <T> T handleDoc(String file, ThrowingFunction<PDDocument, T, IOException> handler) {
        try (PDDocument doc = PDDocument.load(new File(file))) {
            return handler.apply(doc);
        } catch (IOException e) {
            throw new SlimFixtureException("Unable to read PDF: " + file, e);
        }
    }

    protected String postProcessText(String text) {
        // spaces at end of line are not interesting
        return text.replaceAll(" +\\n", "\n");
    }

    public static PDFTextStripper createPdfTextStripper() {
        try {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            pdfStripper.setLineSeparator("\n");
            pdfStripper.setPageEnd("\n");
            return pdfStripper;
        } catch (IOException e) {
            throw new SlimFixtureException("Unable to create PDF toolkit", e);
        }
    }

    public PDFTextStripper getPdfStripper() {
        return pdfStripper;
    }
}