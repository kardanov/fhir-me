package de.kardanov.fhirme.model;

import java.io.Serializable;
import java.util.Optional;
import java.util.UUID;

public abstract class Resource implements Serializable {

    String id;

    public String getId() {
        if (Optional.ofNullable(id).orElse("").trim().isEmpty()) {
            id = UUID.randomUUID().toString();
        }
        return id;
    }
}
