package data;

import lombok.Setter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represents the table section of the XML file.
 */
@Setter
@XmlRootElement(name = "table")
public class Table {

    /**
     * The row data section of the document,
     * represented by an array of {@link RowData}.
     */
    private RowData[] rowData;

    @XmlElement(name = "row-data")
    public RowData[] getRowData() {
        return rowData;
    }

}
