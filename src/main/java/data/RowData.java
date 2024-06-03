package data;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the row data section of the XML file.
 * The elements are numbered based on their order, starting from 1.
 * Therefore, the element at index 0 corresponds to value1.
 */
@Getter
@Setter
@XmlRootElement(name = "row-data")
@XmlAccessorType(XmlAccessType.FIELD)
public class RowData {

    @XmlElements({
            @XmlElement(name = "value1", type = String.class),
            @XmlElement(name = "value2", type = String.class),
    })
    private List<String> values = new ArrayList<>();

}