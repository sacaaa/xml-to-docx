import util.DataHandler;

import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) {
        // Load the XML and DOCX data
        var xmlPath = DataHandler.getFilePath("/raw.xml");
        var docxPath = DataHandler.getFilePath("/raw.docx");
        var xmlData = DataHandler.loadXml(xmlPath);
        var docxData = DataHandler.loadDocx(docxPath);

        // Replace the placeholders in the DOCX data with the corresponding values from the XML data
        Pattern pattern = Pattern.compile("<(.*?)>");
        DataHandler.replacePlaceholderInParagraphs(docxData, xmlData, pattern);
        DataHandler.replacePlaceholderInTables(docxData, xmlData, pattern);

        // Replace the placeholder with the logo image
        var imageFilePath = DataHandler.getFilePath("/logo.png");
        DataHandler.replacePlaceholderWithLogo(docxData, imageFilePath, "[logo]");

        // Save the modified DOCX data
        DataHandler.saveDocx(docxData, "xml_to_docx_result.docx");
    }

}
