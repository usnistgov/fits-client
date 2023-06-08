package gov.nist.hit.resources.deploy.client.test;

import gov.nist.healthcare.cds.domain.FixedDate;
import gov.nist.healthcare.cds.domain.TestCase;
import gov.nist.healthcare.cds.domain.TestPlan;
import gov.nist.healthcare.cds.domain.exception.UnresolvableDate;
import gov.nist.healthcare.cds.domain.wrapper.*;
import gov.nist.healthcare.cds.enumeration.EvaluationStatus;
import gov.nist.healthcare.cds.enumeration.SerieStatus;
import gov.nist.healthcare.cds.service.impl.SimpleTestCaseUtilService;
import gov.nist.hit.resources.deploy.client.SSLFITSClient;
import gov.nist.hit.resources.deploy.exception.InsupportedApiMethod;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

public class SSLFITSClientSnippet {

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, InsupportedApiMethod, UnresolvableDate, ParseException {
        String passToken = SSLFITSClient.tokenize("USERNAME", "PASSWORD");
        SSLFITSClient client = new SSLFITSClient("HOST", passToken);

        ResponseEntity<List<TestPlan>> testPlans = client.getTestPlans();

        TestPlan tp = testPlans.getBody().get(0);
        TestCase tc = tp.getTestCases().get(0);
        SimpleTestCaseUtilService testCaseUtilService = SimpleTestCaseUtilService.getInstance();

        LocalDate executionDate = LocalDate.now();
        System.out.println("Execution Date : " + executionDate);
        System.out.println("Executing TestCase : " + tc.getUid() + " (" + tc.getId() + ")");
        TestCasePayLoad payload = testCaseUtilService.getTestCasePayload(tc, executionDate);

        // USE PAYLOAD TO RUN TEST CASE ON A CDS ENGINE
        payload.getEvaluationDate();
        payload.getDateOfBirth();
        payload.getGender();
        payload.getImmunizations();
        payload.getTestCaseNumber();
        payload.getTestCaseId();
        ValidationRequest cdsEngineResponse = simulateTestCaseExecution(tc, payload);
        // -----


        ResponseEntity<List<Report>> reports = client.validate(Collections.singletonList(cdsEngineResponse));
    }


    public static ValidationRequest simulateTestCaseExecution(TestCase testCase, TestCasePayLoad payload) throws ParseException {
        /* Do something with the payload and send testcase data to cds engine and get response
           In this simulation I will just hardcode a fake response from an Engine
         */
        List<ResponseVaccinationEvent> events = new ArrayList<>();
        for(TestCasePayLoad.VaccinationEventPayLoad vaccinationEventPayLoad: payload.getImmunizations()) {
            // For each testcase vaccination event, create an evaluation response
            ResponseVaccinationEvent vaccinationEvent = new ResponseVaccinationEvent();
            vaccinationEvent.setAdministred(vaccinationEventPayLoad.getRef());
            vaccinationEvent.setDate(new FixedDate(vaccinationEventPayLoad.getAdministred()));

            // Set evaluation response to "VALID"
            ActualEvaluation evaluation = new ActualEvaluation();
            evaluation.setVaccine(vaccinationEventPayLoad.getRef());
            evaluation.setStatus(EvaluationStatus.VALID);
            vaccinationEvent.setEvaluations(Collections.singleton(
                    evaluation
            ));

            events.add(vaccinationEvent);
        }

        // Create forecast for Immunization at index 0
        ActualForecast forecast = new ActualForecast();
        forecast.setVaccine(payload.getImmunizations().get(0).getRef());
        // Hardcoded values
        forecast.setEarliest(LocalDate.parse("10/04/2022", FixedDate.formatter));
        forecast.setRecommended(LocalDate.parse("10/04/2022", FixedDate.formatter));
        forecast.setPastDue(LocalDate.parse("10/04/2024", FixedDate.formatter));
        forecast.setDoseNumber("2");
        forecast.setSerieStatus(SerieStatus.N);


        return new ValidationRequest(
                Collections.singletonList(forecast),
                events,
                testCase,
                payload.getEvaluationDate()
        );
    }
}
