package de.kardanov.fhirme.util;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import org.hl7.fhir.instance.model.api.IBaseResource;

public class ResourceEncoder {

    private static ResourceEncoder resourceEncoder;
    private static IParser parser;

    private ResourceEncoder() {
        FhirContext context = FhirContext.forDstu2();
        parser = context.newJsonParser();
    }

    public static ResourceEncoder encoder() {
        if (resourceEncoder == null) {
            resourceEncoder = new ResourceEncoder();
        }
        return resourceEncoder;
    }

    public <T extends IBaseResource> String encode(T resource) {
        return parser.encodeResourceToString(resource);
    }
}
