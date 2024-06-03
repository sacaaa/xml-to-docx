package data;

import lombok.Setter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represents the root element of the XML file.
 */
@Setter
@XmlRootElement(name="root")
public class Root {

    /**
     * The customer details section of the document,
     * represented by an instance of {@link CustomerDetails}.
     */
    private CustomerDetails customerDetails;

    /**
     * The document data section of the document,
     * represented by an instance of {@link DocumentData}.
     */
    private DocumentData documentData;

    @XmlElement(name="customer_details")
    public CustomerDetails getCustomerDetails() {
        return customerDetails;
    }

    @XmlElement(name="document_data")
    public DocumentData getDocumentData() {
        return documentData;
    }

}