package de.kardanov.fhirme.util;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import org.hl7.fhir.instance.model.api.IBaseResource;

public class ResourceParser {

    private static ResourceParser resourceParser;
    private static IParser parser;

    private ResourceParser() {
        FhirContext context = FhirContext.forDstu2();
        parser = context.newJsonParser();
    }

    public static ResourceParser parser() {
        if (resourceParser == null) {
            resourceParser = new ResourceParser();
        }
        return resourceParser;
    }

    public <T extends IBaseResource> T parse(Class<T> resourceType, String resourceAsJson) {
        return parser.parseResource(resourceType, resourceAsJson);
    }
}
