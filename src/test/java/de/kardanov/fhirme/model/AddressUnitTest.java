package de.kardanov.fhirme.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import static de.kardanov.fhirme.model.Address.AddressBuilder.address;
import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
public class AddressUnitTest {

    @Test
    public void testThatAddressCanBeBuilt() {
        String street = "Welserstr. 10-12";
        String zip = "10777";
        String city = "Berlin";
        String country = "Germany";

        Address address = address(street).in(zip, city, country).build();

        assertThat(address).isNotNull().extracting("street", "zip", "city", "country").doesNotContainNull().containsExactly(street, zip, city, country);
    }

    @Test
    public void testThatNullPointerExceptionWillBeThrownIfRequiredStreetPropertiesNotSpecified() {
        assertThatThrownBy(() -> address(null).build())
                .isInstanceOf(NullPointerException.class).hasMessage("Street must be provided");
    }

    @Test
    public void testThatNullPointerExceptionWillBeThrownOnlyIfRequiredPropertiesNotSpecified() {
        String street = "Welserstr. 10-12";
        String zip = "10777";
        String city = "Berlin";

        assertThatThrownBy(() -> address(street).in(null, city, null).build())
                .isInstanceOf(NullPointerException.class).hasMessage("ZIP must be provided");
        assertThatThrownBy(() -> address(street).in(zip, null, null).build())
                .isInstanceOf(NullPointerException.class).hasMessage("City must be provided");
        assertThatCode(() -> address(street).in(zip, city, null).build()).doesNotThrowAnyException();
    }
}
