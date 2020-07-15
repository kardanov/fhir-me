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
import static de.kardanov.fhirme.util.ResourceParser.parser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@RunWith(SpringRunner.class)
public class ResourceParserUnitTest {

    private final PatientResourceDataConverter dataConverter = new PatientResourceDataConverter();

    @Test
    public void testThatPatientResourceWillBeProperlyParsed() {
        String firstName = "Max";
        String lastName = "Mustermann";
        Address address = address("Welserstr. 10-12").in("10777", "Berlin", "Germany").build();
        LocalDate dateOfBirth = LocalDate.parse("1985-01-01");
        String email = "test@example.com";
        Patient patient = patient(firstName, lastName).bornOn(dateOfBirth).ofGender(Person.Gender.MALE).livingAt(address).withEmail(email).build();
        String patientDataAsJson = "{\"resourceType\":\"Patient\",\"id\":\"" + patient.getId() + "\",\"name\":[{\"text\":\"" + patient + "\",\"family\":[\"" + lastName + "\"],\"given\":[\"" + firstName + "\"]}],\"telecom\":[{\"value\":\"" + email + "\"}],\"gender\":\"male\",\"birthDate\":\"1985-01-01\",\"address\":[{\"use\":\"home\",\"type\":\"physical\",\"text\":\"" + address + "\",\"line\":[\"" + address.getStreet() + "\"],\"city\":\"" + address.getCity() + "\",\"postalCode\":\"" + address.getZip() + "\",\"country\":\"" + address.getCountry() + "\"}]}";

        Patient parsedPatientData = dataConverter.convert(parser().parse(ca.uhn.fhir.model.dstu2.resource.Patient.class, patientDataAsJson));

        assertThat(parsedPatientData).isNotNull().isEqualToComparingFieldByField(patient);
    }

    @Test
    public void testThatNullPointerExceptionWillBeThrownIfPatientJsonNotProvided() {
        assertThatThrownBy(() -> parser().parse(ca.uhn.fhir.model.dstu2.resource.Patient.class, null)).isInstanceOf(NullPointerException.class);
    }
}
