package de.kardanov.fhirme;

import de.kardanov.fhirme.model.Patient;
import de.kardanov.fhirme.repository.PatientNotFoundException;
import de.kardanov.fhirme.repository.PatientRepository;
import de.kardanov.fhirme.repository.entity.PatientEntity;
import de.kardanov.fhirme.util.PatientResourceDataConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;

import static de.kardanov.fhirme.util.ResourceEncoder.encoder;
import static de.kardanov.fhirme.util.ResourceParser.parser;
import static java.util.stream.Collectors.toSet;

@RestController
@RequestMapping("/api")
public class PatientController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PatientController.class);

    private final PatientRepository patientRepository;
    private final PatientResourceDataConverter dataConverter;

    private static final Function<PatientEntity, ca.uhn.fhir.model.dstu2.resource.Patient> PARSE_PATIENT =
            (patient) -> parser().parse(ca.uhn.fhir.model.dstu2.resource.Patient.class, patient.getJsonData());

    @Autowired
    public PatientController(PatientRepository patientRepository, PatientResourceDataConverter dataConverter) {
        this.patientRepository = patientRepository;
        this.dataConverter = dataConverter;
    }

    @GetMapping("/patients")
    public Collection<Patient> getAllPatients() {
        LOGGER.info("Getting all database entries");
        return patientRepository.findAll().stream()
                .map(PARSE_PATIENT)
                .map(dataConverter::convert)
                        .collect(toSet());
    }

    @GetMapping("/patients/{id}")
    public Patient getPatientById(@PathVariable(value = "id") String id) {
        LOGGER.info("Getting database entry for id #" + id);
        return patientRepository.findById(id)
                .map(PARSE_PATIENT)
                .map(dataConverter::convert)
                        .orElseThrow(() -> new PatientNotFoundException(id));
    }

    @PostMapping("/patients")
    public Patient createPatient(@Valid @RequestBody Patient patient) {
        Optional.ofNullable(patient)
                .map(dataConverter::convert)
                .map(encoder()::encode)
                .map(encodedPatientData -> new PatientEntity(patient.getId(), encodedPatientData))
                        .ifPresent(patientRepository::save);
        LOGGER.info("Created database entry for " + patient);
        return patient;
    }

    @PutMapping("/patients/{id}")
    public Patient updatePatient(@PathVariable(value = "id") String id, @Valid @RequestBody Patient patient) {
        PatientEntity entity = patientRepository.findById(id).orElseThrow(() -> new PatientNotFoundException(id));
        Optional.ofNullable(patient)
                .map(dataConverter::convert)
                .map(encoder()::encode)
                .map(entity::setJsonData)
                        .ifPresent(patientRepository::save);
        LOGGER.info("Updated database entry for " + patient);
        return patient;
    }

    @DeleteMapping("/patients/{id}")
    public ResponseEntity<?> deletePatient(@PathVariable(value = "id") String id) {
        PatientEntity entity = patientRepository.findById(id).orElseThrow(() -> new PatientNotFoundException(id));
        patientRepository.delete(entity);
        LOGGER.info("Deleted database entry for id #" + id);
        return ResponseEntity.ok().build();
    }
}
