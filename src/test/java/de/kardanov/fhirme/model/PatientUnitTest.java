package de.kardanov.fhirme.model;

import de.kardanov.fhirme.model.Person.Gender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

import static de.kardanov.fhirme.model.Address.AddressBuilder.address;
import static de.kardanov.fhirme.model.Patient.PatientBuilder.patient;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@RunWith(SpringRunner.class)
public class PatientUnitTest {

    private static final String FIRST_NAME = "Max";
    private static final String LAST_NAME = "Mustermann";

    @Test
    public void testThatPatientCanBeBuilt() {
        Address address = address("Welserstr. 10-12").in("10777", "Berlin", "Germany").build();
        LocalDate dateOfBirth = LocalDate.parse("1985-01-01");
        String email = "test@example.com";

        Patient patient = patient(FIRST_NAME, LAST_NAME).bornOn(dateOfBirth).ofGender(Gender.MALE).livingAt(address).withEmail(email).build();

        assertThat(patient).isNotNull();
        assertThat(patient).extracting("firstName", "lastName").as("Name must be set").doesNotContainNull().containsExactly(FIRST_NAME, LAST_NAME);
        assertThat(patient).extracting("address").as("Address must be set").isEqualToComparingFieldByField(address);
        assertThat(patient).extracting("email").as("Email must be set").isEqualTo(email);
        assertThat(patient).extracting("gender").as("Gender must be set").isEqualTo(Gender.MALE);
    }

    @Test
    public void testThatPatientToStringMethodWorksAsExpected() {
        Patient patient = patient(FIRST_NAME, LAST_NAME).build();
        assertThat(patient.toString()).isEqualTo(FIRST_NAME + " " + LAST_NAME + " (id: " + patient.getId() + ")");
    }

    @Test
    public void testThatUnknownGenderWillBeReturnedIfNotSetForPatient() {
        Patient patient = patient(FIRST_NAME, LAST_NAME).build();
        assertThat(patient).extracting("gender").as("UNKNOWN gender must be returned").isEqualTo(Gender.UNKNOWN);
    }

    @Test
    public void testThatNullPointerExceptionWillBeThrownIfRequiredNamePropertiesNotSpecified() {
        assertThatThrownBy(() -> patient(null, LAST_NAME).build())
                .isInstanceOf(NullPointerException.class).hasMessage("First name must be provided");
        assertThatThrownBy(() -> patient(FIRST_NAME, null).build())
                .isInstanceOf(NullPointerException.class).hasMessage("Last name must be provided");
    }

    @Test
    public void testThatIllegalArgumentExceptionWillBeThrownIfInvalidEmailAddressSpecified() {
        assertThatThrownBy(() -> patient(FIRST_NAME, LAST_NAME).withEmail("@invalid_email_address@").build())
                .isInstanceOf(IllegalArgumentException.class).hasMessage("Email address must be valid");
    }

    @Test
    public void testThatUniqueIdWillBeGenerated() {
        Patient patient = patient(FIRST_NAME, LAST_NAME).build();
        assertThat(patient).extracting("id").asString().isNotNull().matches("[a-z0-9\\-.]{1,36}");
    }
}
