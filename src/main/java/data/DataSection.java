package data;

import lombok.Setter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represents the data section of the XML file.
 * The elements are numbered based on their order, starting from 1.
 * Therefore, the element at index 0 corresponds to data1.
 */
@Setter
@XmlRootElement(name="data_section1")
public class DataSection {

    private String[] data;

    @XmlElement(name="data")
    public String[] getData() {
        return data;
    }

}
