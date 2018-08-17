package net.adipappi.parse.data;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;

public class ReadPdfFileLineByline {

	public static final int NUMBER_OF_FIRST_LINES_TO_SKIP = 9;
	public static final String PDF_FILE = "/home/timsli/perso/Election_Malienne/Mali_2018_President_Election_Second_Round.pdf";

    public static void main(String[] args) throws IOException {

        try (PDDocument document = PDDocument.load(new File(PDF_FILE))) {

            document.getClass();

                PDFTextStripperByArea stripper = new PDFTextStripperByArea();
                stripper.setSortByPosition(true);

                PDFTextStripper tStripper = new PDFTextStripper();

                String pdfFileInText = tStripper.getText(document);
                //System.out.println("Text:" + st);

				// split by whitespace
                List<String> lines = filterPdfTableHeader(pdfFileInText.split("\\r?\\n"));
                for (String line : lines) {
                    System.out.println(line);
                }

        }

    }
    
    private static List<String>  filterPdfTableHeader(String[] allLines) {
        	return Arrays.asList(allLines).stream().skip(NUMBER_OF_FIRST_LINES_TO_SKIP).filter(p->!p.contains("SCISSE")&&(!p.contains("RESULTATS"))&&(!p.contains("ELECTION"))&&(!p.contains("SCRUTIN"))).collect(Collectors.toList());
    }
    
}