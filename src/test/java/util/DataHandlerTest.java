package util;


import data.Root;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class DataHandlerTest {

    String correctXmlPath = DataHandler.getFilePath("/raw.xml");

    String correctDocxPath = DataHandler.getFilePath("/raw.docx");

    @Test
    void success_loadXml() {
        var xmlData = DataHandler.loadXml(correctXmlPath);
        assertNotNull(xmlData);
        assertEquals(Root.class, xmlData.getClass());
    }

    @Test
    void failure_loadXml() {
        assertThrows(RuntimeException.class, () -> DataHandler.loadXml("/raw1.xml"));
    }

    @Test
    void success_loadDocx() {
        var docxData = DataHandler.loadDocx(correctDocxPath);
        assertNotNull(docxData);
        assertEquals(XWPFDocument.class, docxData.getClass());
    }

    @Test
    void failure_loadDocx() {
        assertThrows(RuntimeException.class, () -> DataHandler.loadDocx("/raw1.docx"));
    }

    @Test
    void saveDocx() {
        var docxData = new XWPFDocument();
        var docxPath = "src/test/resources/save_test.docx";

        assertThrows(RuntimeException.class, () -> DataHandler.saveDocx(null, docxPath));
        assertThrows(RuntimeException.class, () -> DataHandler.saveDocx(docxData, null));

        DataHandler.saveDocx(docxData, docxPath);
        assertTrue(new File(docxPath).exists());
    }

    @Test
    void getFilePath() {
        assertEquals(correctXmlPath, DataHandler.getFilePath("/raw.xml"));
        assertEquals(correctDocxPath, DataHandler.getFilePath("/raw.docx"));
        assertThrows(RuntimeException.class, () -> DataHandler.getFilePath("/raw1.xml"));
        assertThrows(RuntimeException.class, () -> DataHandler.getFilePath("/raw1.docx"));
    }

    @Test
    void replacePlaceholderInParagraphs() {
        // Assets
        XWPFDocument docxDataForValidPattern = DataHandler.loadDocx(correctDocxPath);
        XWPFDocument docxDataForInValidPattern = DataHandler.loadDocx(correctDocxPath);
        Root xmlData = DataHandler.loadXml(correctXmlPath);
        Pattern validPattern = Pattern.compile("<(.*?)>");
        Pattern inValidPattern = Pattern.compile("<test_pattern>");

        // Assert before replacement for valid pattern
        boolean found = false;
        for (var paragraph : docxDataForValidPattern.getParagraphs()) {
            var matcher = validPattern.matcher(paragraph.getText());
            if (matcher.find()) {
                found = true;
                break;
            }
        }
        assertTrue(found);

        // Assert before replacement for invalid pattern
        found = false;
        for (var paragraph : docxDataForInValidPattern.getParagraphs()) {
            var matcher = inValidPattern.matcher(paragraph.getText());
            if (matcher.find()) {
                found = true;
                break;
            }
        }
        assertFalse(found);

        // Replace placeholders
        DataHandler.replacePlaceholderInParagraphs(docxDataForValidPattern, xmlData, validPattern);
        DataHandler.replacePlaceholderInParagraphs(docxDataForInValidPattern, xmlData, inValidPattern);

        // Assert after replacement
        for (var paragraph : docxDataForValidPattern.getParagraphs()) {
            assertFalse(validPattern.matcher(paragraph.getText()).find());
        }
        for (var paragraph : docxDataForInValidPattern.getParagraphs()) {
            assertFalse(inValidPattern.matcher(paragraph.getText()).find());
        }
    }

    @Test
    void replacePlaceholderInTables() {
        // Assets
        XWPFDocument docxDataForValidPattern = DataHandler.loadDocx(correctDocxPath);
        XWPFDocument docxDataForInValidPattern = DataHandler.loadDocx(correctDocxPath);
        Root xmlData = DataHandler.loadXml(correctXmlPath);
        Pattern validPattern = Pattern.compile("<(.*?)>");
        Pattern inValidPattern = Pattern.compile("<test_pattern>");

        // Assert before replacement for valid pattern
        boolean found = false;
        for (var table : docxDataForValidPattern.getTables()) {
            for (var row : table.getRows()) {
                for (var cell : row.getTableCells()) {
                    var matcher = validPattern.matcher(cell.getText());
                    if (matcher.find()) {
                        found = true;
                        break;
                    }
                }
                if (found) break;
            }
            if (found) break;
        }
        assertTrue(found);

        // Assert before replacement for invalid pattern
        found = false;
        for (var table : docxDataForInValidPattern.getTables()) {
            for (var row : table.getRows()) {
                for (var cell : row.getTableCells()) {
                    var matcher = inValidPattern.matcher(cell.getText());
                    if (matcher.find()) {
                        found = true;
                        break;
                    }
                }
                if (found) break;
            }
            if (found) break;
        }
        assertFalse(found);

        // Replace placeholders
        DataHandler.replacePlaceholderInTables(docxDataForValidPattern, xmlData, validPattern);
        DataHandler.replacePlaceholderInTables(docxDataForInValidPattern, xmlData, inValidPattern);

        // Assert after replacement
        for (var table : docxDataForValidPattern.getTables()) {
            for (var row : table.getRows()) {
                for (var cell : row.getTableCells()) {
                    assertFalse(validPattern.matcher(cell.getText()).find());
                }
            }
        }
        for (var table : docxDataForInValidPattern.getTables()) {
            for (var row : table.getRows()) {
                for (var cell : row.getTableCells()) {
                    assertFalse(inValidPattern.matcher(cell.getText()).find());
                }
            }
        }
    }

    @Test
    void replacePlaceholderWithLogo() {
        // Assets
        XWPFDocument docxData = DataHandler.loadDocx(correctDocxPath);
        String imageFilePath = DataHandler.getFilePath("/logo.png");
        String placeholder = "[logo]";

        // Assert before replacement
        boolean found = false;
        for (var header : docxData.getHeaderList()) {
            for (var paragraph : header.getParagraphs()) {
                if (paragraph.getText().contains(placeholder)) {
                    found = true;
                    break;
                }
            }
            if (found) break;
        }
        assertTrue(found);

        // Replace placeholders
        DataHandler.replacePlaceholderWithLogo(docxData, imageFilePath, placeholder);

        // Assert after replacement
        found = false;
        for (var header : docxData.getHeaderList()) {
            for (var paragraph : header.getParagraphs()) {
                if (paragraph.getText().contains(placeholder)) {
                    found = true;
                    break;
                }
            }
            if (found) break;
        }
        assertFalse(found);
    }

    @Test
    void replacePlaceholder() {
        Root xmlData = DataHandler.loadXml(correctXmlPath);
        assertEquals(xmlData.getCustomerDetails().getName(),
                DataHandler.replacePlaceholder("name", xmlData));
        assertEquals(xmlData.getDocumentData().getDataSection().getData()[0],
                DataHandler.replacePlaceholder("data1", xmlData));
        assertEquals(xmlData.getDocumentData().getDataSection().getData()[1],
                DataHandler.replacePlaceholder("data2", xmlData));
        assertEquals(xmlData.getDocumentData().getDataSection().getData()[2],
                DataHandler.replacePlaceholder("data3", xmlData));
        assertEquals(xmlData.getDocumentData().getDataSection().getData()[3],
                DataHandler.replacePlaceholder("data4", xmlData));
        assertEquals(xmlData.getCustomerDetails().getBirthplace(),
                DataHandler.replacePlaceholder("birthplace", xmlData));
        assertEquals(xmlData.getCustomerDetails().getBirthday(),
                DataHandler.replacePlaceholder("birthday", xmlData));
        assertEquals(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd.")),
                DataHandler.replacePlaceholder("date", xmlData));

        xmlData.getCustomerDetails().setCustomerType(1);
        assertEquals("not placed",
                DataHandler.replacePlaceholder("order_status", xmlData));

        xmlData.getCustomerDetails().setCustomerType(2);
        assertEquals("placed",
                DataHandler.replacePlaceholder("order_status", xmlData));

        xmlData.getCustomerDetails().setCustomerType(3);
        assertEquals("delivered",
                DataHandler.replacePlaceholder("order_status", xmlData));

        assertEquals("Unknown",
                DataHandler.replacePlaceholder("test", xmlData));
    }
}