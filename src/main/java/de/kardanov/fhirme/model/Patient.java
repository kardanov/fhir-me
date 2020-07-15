package de.kardanov.fhirme.model;

import javax.annotation.Nullable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Objects;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

public class Patient extends Person {

    public static final long serialVersionUID = 1L;

    private LocalDate dateOfBirth;
    private Address address;
    private String email;

    private Patient() {
    }

    @Nullable
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    @Nullable
    public Address getAddress() {
        return address;
    }

    @Nullable
    public String getEmail() {
        return email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Patient patient = (Patient) o;
        return getFirstName().equals(patient.getFirstName())
                && getLastName().equals(patient.getLastName())
                && Objects.equals(dateOfBirth, patient.dateOfBirth);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirstName(), getLastName(), dateOfBirth);
    }

    @Override
    public String toString() {
        return firstName + " " + lastName + " (id: " + getId() + ")";
    }

    public static class PatientBuilder {

        private static final Pattern EMAIL_ADDRESS = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

        private String firstName;
        private String lastName;
        private String id;
        private Gender gender;
        private LocalDate dateOfBirth;
        private Address address;
        private String email;

        private PatientBuilder() {
        }

        public static PatientBuilder patient(String firstName, String lastName) {
            requireNonNull(firstName, "First name must be provided");
            requireNonNull(lastName, "Last name must be provided");
            PatientBuilder builder = new PatientBuilder();
            builder.firstName = firstName;
            builder.lastName = lastName;
            return builder;
        }

        public PatientBuilder withId(String id) {
            this.id = id;
            return this;
        }

        public PatientBuilder bornOn(Instant dateOfBirth) {
            this.dateOfBirth = dateOfBirth.atZone(ZoneId.systemDefault()).toLocalDate();
            return this;
        }

        public PatientBuilder bornOn(LocalDate dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
            return this;
        }

        public PatientBuilder ofGender(Gender gender) {
            this.gender = gender;
            return this;
        }

        public PatientBuilder livingAt(Address address) {
            this.address = address;
            return this;
        }

        public PatientBuilder withEmail(String email) {
            checkArgument(isValid(email), "Email address must be valid");
            this.email = email;
            return this;
        }

        public Patient build() {
            Patient patient = new Patient();
            patient.firstName = firstName;
            patient.lastName = lastName;
            patient.id = id;
            patient.dateOfBirth = dateOfBirth;
            patient.gender = gender;
            patient.address = address;
            patient.email = email;
            return patient;
        }

        private static boolean isValid(String email) {
            return EMAIL_ADDRESS.matcher(email).find();
        }
    }
}
