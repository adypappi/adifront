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

	 public static final String PDF_FILE_URL = "http://www.matcl.gov.ml/sites/default/files/public/RESULTATS%20PROVISOIRES%20COMPLETS%20BUREAU%20DE%20VOTE%20PAR%20BUREAU%20DE%20VOTE%20SCRUTIN%20DU%2012%20AOUT%202018.pdf";
        public static final int NUMBER_OF_FIRST_LINES_TO_SKIP = 9;
        public static final String PDF_FILE = "Mali_2018_President_Election_Second_Round.pdf";

        public static final int OUT_BUFFER_SIZE = 8192 * 1204;
        public static final int IN_BUFFER_SIZE = 16384;

        public static void main(String[] args) throws IOException {

            try (PDDocument document = PDDocument.load(downloadPdfFile())) {
                //document.getClass();
                PDFTextStripperByArea stripper = new PDFTextStripperByArea();
                stripper.setSortByPosition(true);
                PDFTextStripper tStripper = new PDFTextStripper();
                tStripper.setWordSeparator(";");
                String pdfFileInText = tStripper.getText(document);

                // split by whitespace
                List<String> lines = filterPdfTableHeader(pdfFileInText.split("\\r?\\n"));
                for (String line : lines) {
                    System.out.println(line);
                }

            }

        }

        private static final File downloadPdfFile() throws IOException {
            Path filePath = Paths.get("E:", "opendata", "malielections", "2018", PDF_FILE);
            File downloadedFile = filePath.toFile();
            if (!Files.exists(filePath)) {
                downloadedFile = Files.createFile(filePath).toFile();
                final URLConnection request = new URL(PDF_FILE_URL).openConnection();
                try (final InputStream inputStream = request.getInputStream();
                     final FileOutputStream fileStream = new FileOutputStream(downloadedFile);
                     final BufferedOutputStream outputStream = new BufferedOutputStream(fileStream, OUT_BUFFER_SIZE);) {
                    final byte[] data = new byte[IN_BUFFER_SIZE];
                    int bytesRead = 0;
                    while ((bytesRead = inputStream.read(data)) != -1) {
                        outputStream.write(data, 0, bytesRead);
                    }
                }
                downloadedFile = filePath.toFile();
            }
            return downloadedFile;
        }

        private static List<String>  filterPdfTableHeader(String[] allLines) {
            return Arrays.asList(allLines).stream().skip(NUMBER_OF_FIRST_LINES_TO_SKIP).filter(p->!p.contains("SCISSE")
                    &&(!p.contains("RESULTATS"))
                    &&(!p.contains("ELECTION"))
                    &&(!p.contains("SCRUTIN"))).collect(Collectors.toList());
        }

    
}
