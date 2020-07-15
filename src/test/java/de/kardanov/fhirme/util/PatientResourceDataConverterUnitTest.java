package de.kardanov.fhirme.util;

import de.kardanov.fhirme.model.Address;
import de.kardanov.fhirme.model.Patient;
import de.kardanov.fhirme.model.Person.Gender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;

import static de.kardanov.fhirme.model.Address.AddressBuilder.address;
import static de.kardanov.fhirme.model.Patient.PatientBuilder.patient;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@RunWith(SpringRunner.class)
public class PatientResourceDataConverterUnitTest {

    private final PatientResourceDataConverter dataConverter = new PatientResourceDataConverter();

    @Test
    public void testThatPatientDataWillBeConvertedFromLocalToRemoteAndBack() {
        Address address = address("Welserstr. 10-12").in("10777", "Berlin", "Germany").build();
        Patient initialPatientData = patient("Erika", "Mustermann").bornOn(Instant.parse("1985-01-01T10:00:00.00Z")).ofGender(Gender.FEMALE).livingAt(address).withEmail("test@example.com").build();

        Patient patientDataConvertedToRemoteAndBack = dataConverter.convert(dataConverter.convert(initialPatientData));

        assertThat(patientDataConvertedToRemoteAndBack).isNotNull().isEqualToComparingFieldByField(initialPatientData);
    }

    @Test
    public void testThatNullPointerExceptionWillBeThrownIfNoPatientDataIsProvided() {
        assertThatThrownBy(() -> {
            Patient patient = null;
            dataConverter.convert(patient);
        }).isInstanceOf(NullPointerException.class).hasMessage("Patient data in local format must be provided");

        assertThatThrownBy(() -> {
            ca.uhn.fhir.model.dstu2.resource.Patient patient = null;
            dataConverter.convert(patient);
        }).isInstanceOf(NullPointerException.class).hasMessage("Patient data in remote format must be provided");
    }
}