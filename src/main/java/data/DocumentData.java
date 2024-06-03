package data;

import lombok.Setter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represents the data section of the XML file.
 */
@Setter
@XmlRootElement(name = "document_data")
public class DocumentData {

    /**
     * The data section of the document,
     * represented by an instance of {@link DataSection}.
     */
    private DataSection dataSection;

    /**
     * The table section of the document,
     * represented by an instance of {@link Table}.
     */
    private Table table;

    @XmlElement(name = "data_section1")
    public DataSection getDataSection() {
        return dataSection;
    }

    @XmlElement(name = "table")
    public Table getTable() {
        return table;
    }

}
