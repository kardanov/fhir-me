package de.kardanov.fhirme.util;

import ca.uhn.fhir.model.dstu2.resource.BaseResource;
import ca.uhn.fhir.model.primitive.IdDt;
import de.kardanov.fhirme.model.Resource;

public interface ResourceDataConverter<T1 extends Resource, T2 extends BaseResource> {

    /**
     * Converts from local data format to remote data format.
     */
    T2 convert(T1 localData);

    /**
     * Converts from remote data format to local data format.
     */
    T1 convert(T2 remoteData);

    default IdDt getId(T1 localData) {
        return new IdDt(localData.getId());
    }

    default String getId(T2 remoteData) {
        return remoteData.getId().getIdPart();
    }
}
