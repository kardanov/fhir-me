package de.kardanov.fhirme.model;

import java.util.Optional;

public abstract class Person extends Resource {

    public enum Gender {
        MALE,
        FEMALE,
        OTHER,
        UNKNOWN
    }

    String firstName;
    String lastName;
    Gender gender;

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Gender getGender() {
        return Optional.ofNullable(gender).orElse(Gender.UNKNOWN);
    }
}
