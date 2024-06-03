package data;

import lombok.Setter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represents the address data section of the XML file.
 */
@Setter
@XmlRootElement(name="address_data")
public class AddressData {

    private int postalCode;

    private String city;

    private String street;

    @XmlElement(name="postal_code")
    public int getPostalCode() {
        return postalCode;
    }

    @XmlElement(name="city")
    public String getCity() {
        return city;
    }

    @XmlElement(name="street_address")
    public String getStreet() {
        return street;
    }

}
