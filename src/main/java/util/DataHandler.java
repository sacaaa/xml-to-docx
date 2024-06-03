package util;

import data.OrderStatus;
import data.Root;
import data.RowData;
import org.apache.poi.common.usermodel.PictureType;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import javax.imageio.ImageIO;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The {@code DataHandler} class provides methods for loading and saving XML and DOCX data, replacing placeholders in
 * paragraphs and tables with the corresponding values from the XML data.
 */
public class DataHandler {

    /**
     * Loads XML data from the specified file.
     *
     * @param filePath the name of the file to load the XML data from
     * @return the {@link Root} object representing the XML data
     */
    public static Root loadXml(String filePath) {
        try (var fis = new FileInputStream(filePath)) {
            JAXBContext jaxbContext = JAXBContext.newInstance(Root.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            return (Root) unmarshaller.unmarshal(fis);
        } catch (JAXBException | IOException e) {
            throw new RuntimeException("Error loading XML data", e);
        }
    }

    /**
     * Loads DOCX data from the specified file.
     *
     * @param filePath the path of the file to load the DOCX data from
     * @return the {@link XWPFDocument} object representing the DOCX data
     */
    public static XWPFDocument loadDocx(String filePath) {
        try (var fis = new FileInputStream(filePath)) {
            return new XWPFDocument(fis);
        } catch (IOException e) {
            throw new RuntimeException("Error loading DOCX data", e);
        }
    }

    /**
     * Saves DOCX data to the specified file.
     *
     * @param docxData the {@link XWPFDocument} object representing the DOCX data
     * @param filePath the path of the file to save the DOCX data to
     */
    public static void saveDocx(XWPFDocument docxData, String filePath) {
        try (var fos = new FileOutputStream(filePath)) {
            docxData.write(fos);
        } catch (IOException e) {
            throw new RuntimeException("Error saving DOCX data", e);
        }
    }

    /**
     * Returns the path of the specified file.
     *
     * @param fileName the name of the file
     * @return the path of the file
     */
    public static String getFilePath(String fileName) {
        try {
            return (Objects.requireNonNull(DataHandler.class.getResource(fileName))).getPath();
        } catch (NullPointerException e) {
            throw new RuntimeException("Error loading file: " + fileName, e);
        }
    }

    /**
     * Replaces placeholders in the paragraphs with the corresponding values from the XML data.
     *
     * @param docxData the {@link XWPFDocument} object representing the DOCX data
     * @param xmlData the {@link Root} object representing the XML data
     * @param pattern the pattern for identifying placeholders in the DOCX file
     */
    public static void replacePlaceholderInParagraphs(XWPFDocument docxData, Root xmlData, Pattern pattern) {
        for (var paragraph : docxData.getParagraphs()) {
            Matcher matcher = pattern.matcher(paragraph.getText());

            if (matcher.find()) {
                String newText = matcher.replaceAll(replacePlaceholder(matcher.group(1), xmlData));

                for (var i = paragraph.getRuns().size() - 1; i >= 0; i--) {
                    paragraph.removeRun(i);
                }

                paragraph.createRun().setText(newText);
            }
        }
    }

    /**
     * Replaces placeholders in the tables with the corresponding values from the XML data.
     *
     * @param docxData the {@link XWPFDocument} object representing the DOCX data
     * @param xmlData the {@link Root} object representing the XML data
     * @param pattern the pattern for identifying placeholders in the DOCX file
     */
    public static void replacePlaceholderInTables(XWPFDocument docxData, Root xmlData, Pattern pattern) {
        for (var table : docxData.getTables()) {
            for (var i = 1; i < table.getRows().size(); i++) {
                for (var j = 0; j < table.getRow(i).getTableCells().size(); j++) {
                    var cell = table.getRow(i).getCell(j);
                    Matcher matcher = pattern.matcher(cell.getText());

                    if (matcher.find()) {
                        RowData rowData = xmlData.getDocumentData().getTable().getRowData()[i - 1];
                        String newValue = j % 2 == 0 ? rowData.getValues().get(0) : rowData.getValues().get(1);
                        String newText = matcher.replaceAll(newValue);
                        cell.setText(newText);
                    }
                }
            }
        }
    }

    /**
     * Replaces the "[mbh_logo]" placeholder with the MBH logo.
     *
     * @param docxData the {@link XWPFDocument} object representing the DOCX data
     * @param imageFilePath the path of the image file
     */
    public static void replacePlaceholderWithLogo(XWPFDocument docxData, String imageFilePath, String placeholder) {
        try {
            BufferedImage bufferedImage = ImageIO.read(new File(imageFilePath));
            int width = bufferedImage.getWidth();
            int height = bufferedImage.getHeight();

            try (var fis = new FileInputStream(imageFilePath)) {
                for (var header : docxData.getHeaderList()) {
                    for (var paragraph : header.getParagraphs()) {
                        if (paragraph.getText().contains(placeholder)) {
                            for (var i = paragraph.getRuns().size() - 1; i >= 0; i--) {
                                paragraph.removeRun(i);
                            }

                            XWPFRun run = paragraph.createRun();
                            run.addPicture(fis, PictureType.PNG, imageFilePath,
                                    Units.pixelToEMU(width), Units.pixelToEMU(height));
                        }
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException("Error loading image data", e);
            } catch (InvalidFormatException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading image dimensions", e);
        }
    }

    /**
     * Replaces the placeholder with the corresponding value from the XML data.
     *
     * @param placeholder the placeholder to replace
     * @param xmlData the XML data
     * @return the value corresponding to the placeholder
     */
    public static String replacePlaceholder(String placeholder, Root xmlData) {
        return switch (placeholder) {
            case "name" -> xmlData.getCustomerDetails().getName();
            case "data1" -> xmlData.getDocumentData().getDataSection().getData()[0];
            case "data2" -> xmlData.getDocumentData().getDataSection().getData()[1];
            case "data3" -> xmlData.getDocumentData().getDataSection().getData()[2];
            case "data4" -> xmlData.getDocumentData().getDataSection().getData()[3];
            case "birthplace" -> xmlData.getCustomerDetails().getBirthplace();
            case "birthday" -> xmlData.getCustomerDetails().getBirthday();
            case "date" -> LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd."));
            case "order_status" -> OrderStatus.getNameFromId(xmlData.getCustomerDetails().getCustomerType());
            default -> "Unknown";
        };
    }

}