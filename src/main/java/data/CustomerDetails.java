package data;

import lombok.Setter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents the customer details section of the XML file.
 */
@Setter
@XmlRootElement(name="customer_details")
public class CustomerDetails {

    private String name;

    private String birthday;

    private String birthplace;

    private int customerType;

    private AddressData addressData;

    /**
     * Converts the given date to a custom format.
     *
     * @param birthday the date to convert
     * @return the date in the custom format
     */
    private String convertToCustomFormat(String birthday) {
        try {
            LocalDateTime dateTime = LocalDateTime.parse(birthday, DateTimeFormatter.ISO_DATE_TIME);
            return dateTime.format(DateTimeFormatter.ofPattern("yyyy.MM.dd."));
        } catch (Exception e) {
            return birthday; // return the original date if the conversion fails
        }
    }

    public void setBirthday(String birthday) {
        this.birthday = convertToCustomFormat(birthday);
    }

    @XmlElement(name="name")
    public String getName() {
        return name;
    }

    @XmlElement(name="birthday")
    public String getBirthday() {
        return birthday;
    }

    @XmlElement(name="birthplace")
    public String getBirthplace() {
        return birthplace;
    }

    @XmlElement(name="customer_type")
    public int getCustomerType() {
        return customerType;
    }

    @XmlElement(name="address_data")
    public AddressData getAddressData() {
        return addressData;
    }

}