package gov.nist.hit.resources.deploy.api;

import java.time.LocalDate;
import java.util.List;

import gov.nist.healthcare.cds.domain.wrapper.ValidationRequest;
import org.springframework.http.ResponseEntity;
import gov.nist.healthcare.cds.domain.TestCase;
import gov.nist.healthcare.cds.domain.TestPlan;
import gov.nist.healthcare.cds.domain.wrapper.Report;
import gov.nist.hit.resources.deploy.exception.InsupportedApiMethod;
import gov.nist.hit.resources.deploy.model.ClientSoftwareConfig;

public interface FITSClient {

	public ResponseEntity<List<TestPlan>> getTestPlans() throws InsupportedApiMethod;
	public ResponseEntity<List<Report>> execute(ClientSoftwareConfig software, LocalDate relativeAssessmentDate, List<TestCase> tcs) throws InsupportedApiMethod;
	public ResponseEntity<List<Report>> validate(List<ValidationRequest> requests) throws InsupportedApiMethod;
	public boolean validCredentials();
	public ResponseEntity<List<ClientSoftwareConfig>> getSoftwareConfiguration() throws InsupportedApiMethod;
	
}
