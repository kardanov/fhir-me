package de.kardanov.fhirme.model;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

public class Address implements Serializable {

    public static final long serialVersionUID = 1L;

    private String street;
    private String zip;
    private String city;
    private String country;

    private Address() {
    }

    public String getStreet() {
        return street;
    }

    public String getZip() {
        return zip;
    }

    public String getCity() {
        return city;
    }

    @Nullable
    public String getCountry() {
        return country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Address address = (Address) o;
        return street.equals(address.getStreet())
                && zip.equals(address.getZip())
                && city.equals(address.getCity())
                && Objects.equals(country, address.getCountry());
    }

    @Override
    public int hashCode() {
        return Objects.hash(street, zip, city, country);
    }

    @Override
    public String toString() {
        return street + ", " + zip + " " + city + (Optional.ofNullable(country).isPresent() ? ", " + country : "");
    }

    public static class AddressBuilder {

        private String street;
        private String zip;
        private String city;
        private String country;

        private AddressBuilder() {
        }

        public static AddressBuilder address(String street) {
            requireNonNull(street, "Street must be provided");
            AddressBuilder builder = new AddressBuilder();
            builder.street = street;
            return builder;
        }

        public AddressBuilder in(String zip, String city, @Nullable String country) {
            requireNonNull(zip, "ZIP must be provided");
            requireNonNull(city, "City must be provided");
            this.zip = zip;
            this.city = city;
            this.country = country;
            return this;
        }

        public Address build() {
            Address address = new Address();
            address.street = street;
            address.zip = zip;
            address.city = city;
            address.country = country;
            return address;
        }
    }
}
