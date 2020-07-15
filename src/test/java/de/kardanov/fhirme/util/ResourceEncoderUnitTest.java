package de.kardanov.fhirme.util;

import de.kardanov.fhirme.model.Address;
import de.kardanov.fhirme.model.Patient;
import de.kardanov.fhirme.model.Person;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

import static de.kardanov.fhirme.model.Address.AddressBuilder.address;
import static de.kardanov.fhirme.model.Patient.PatientBuilder.patient;
import static de.kardanov.fhirme.util.ResourceEncoder.encoder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@RunWith(SpringRunner.class)
public class ResourceEncoderUnitTest {

    private final PatientResourceDataConverter dataConverter = new PatientResourceDataConverter();

    @Test
    public void testThatPatientResourceWillBeProperlyEncoded() {
        String firstName = "Max";
        String lastName = "Mustermann";
        Address address = address("Welserstr. 10-12").in("10777", "Berlin", "Germany").build();
        LocalDate dateOfBirth = LocalDate.parse("1985-01-01");
        String email = "test@example.com";
        Patient patient = patient(firstName, lastName).bornOn(dateOfBirth).ofGender(Person.Gender.MALE).livingAt(address).withEmail(email).build();

        String encodedPatientData = encoder().encode(dataConverter.convert(patient));
        String expectedJson = "{\"resourceType\":\"Patient\",\"id\":\"" + patient.getId() + "\",\"name\":[{\"text\":\"" + patient + "\",\"family\":[\"" + lastName + "\"],\"given\":[\"" + firstName + "\"]}],\"telecom\":[{\"value\":\"" + email + "\"}],\"gender\":\"male\",\"birthDate\":\"1985-01-01\",\"address\":[{\"use\":\"home\",\"type\":\"physical\",\"text\":\"" + address + "\",\"line\":[\"" + address.getStreet() + "\"],\"city\":\"" + address.getCity() + "\",\"postalCode\":\"" + address.getZip() + "\",\"country\":\"" + address.getCountry() + "\"}]}";
        assertThat(encodedPatientData).isNotNull().isEqualTo(expectedJson);
    }

    @Test
    public void testThatNullPointerExceptionWillBeThrownIfPatientResourceDataNotProvided() {
        assertThatThrownBy(() -> encoder().encode(null))
                .isInstanceOf(NullPointerException.class).hasMessage("theResource can not be null");
    }
}
