package de.kardanov.fhirme.util;

import ca.uhn.fhir.model.dstu2.composite.AddressDt;
import ca.uhn.fhir.model.dstu2.composite.ContactPointDt;
import ca.uhn.fhir.model.dstu2.composite.HumanNameDt;
import ca.uhn.fhir.model.dstu2.valueset.AddressTypeEnum;
import ca.uhn.fhir.model.dstu2.valueset.AddressUseEnum;
import ca.uhn.fhir.model.dstu2.valueset.AdministrativeGenderEnum;
import ca.uhn.fhir.model.primitive.DateDt;
import ca.uhn.fhir.model.primitive.StringDt;
import de.kardanov.fhirme.model.Patient;
import de.kardanov.fhirme.model.Person.Gender;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import static de.kardanov.fhirme.model.Address.AddressBuilder.address;
import static de.kardanov.fhirme.model.Patient.PatientBuilder.patient;
import static java.util.Collections.singletonList;
import static java.util.Objects.requireNonNull;

@Component
public class PatientResourceDataConverter implements ResourceDataConverter<Patient, ca.uhn.fhir.model.dstu2.resource.Patient> {

    @Override
    public Patient convert(ca.uhn.fhir.model.dstu2.resource.Patient remoteData) {
        requireNonNull(remoteData, "Patient data in remote format must be provided");

        HumanNameDt patientName = remoteData.getNameFirstRep();
        Patient.PatientBuilder patient =
                patient(patientName.getGivenFirstRep().getValueAsString(), patientName.getFamilyFirstRep().getValueAsString())
                        .withId(getId(remoteData))
                        .ofGender(Gender.valueOf(remoteData.getGender().toUpperCase()));

        Optional.ofNullable(remoteData.getBirthDate()).map(Date::toInstant).ifPresent(patient::bornOn);

        if (!remoteData.getAddress().isEmpty()) {
            AddressDt patientAddress = remoteData.getAddressFirstRep();
            patient.livingAt(
                    address(patientAddress.getLineFirstRep().getValue())
                            .in(patientAddress.getPostalCode(), patientAddress.getCity(), patientAddress.getCountry()).build());
        }

        if (!remoteData.getTelecom().isEmpty()) {
            patient.withEmail(remoteData.getTelecomFirstRep().getValue());
        }

        return patient.build();
    }

    @Override
    public ca.uhn.fhir.model.dstu2.resource.Patient convert(Patient localData) {
        requireNonNull(localData, "Patient data in local format must be provided");

        ca.uhn.fhir.model.dstu2.resource.Patient patient = new ca.uhn.fhir.model.dstu2.resource.Patient();

        patient.addName(new HumanNameDt()
                .setText(localData.toString())
                .setGiven(singletonList(new StringDt(localData.getFirstName())))
                .setFamily(singletonList(new StringDt(localData.getLastName()))));
        patient.setId(getId(localData));
        patient.setGender(AdministrativeGenderEnum.valueOf(localData.getGender().name()));

        Optional.ofNullable(localData.getDateOfBirth()).map(dateOfBirth -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(dateOfBirth.getYear(), dateOfBirth.getMonth().getValue() - 1, dateOfBirth.getDayOfMonth());
            return new DateDt(calendar);
        }).ifPresent(patient::setBirthDate);

        Optional.ofNullable(localData.getAddress()).ifPresent(address -> {
            AddressDt patientAddress = new AddressDt()
                    .setText(address.toString())
                    .setLine(singletonList(new StringDt(address.getStreet())))
                    .setPostalCode(address.getZip())
                    .setCity(address.getCity())
                    .setUse(AddressUseEnum.HOME)
                    .setType(AddressTypeEnum.PHYSICAL);
            Optional.ofNullable(address.getCountry()).ifPresent(patientAddress::setCountry);
            patient.addAddress(patientAddress);
        });

        Optional.ofNullable(localData.getEmail()).map(email -> new ContactPointDt().setValue(email)).ifPresent(patient::addTelecom);

        return patient;
    }
}
