package gov.nist.hit.resources.deploy.model;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import gov.nist.healthcare.cds.domain.SoftwareConfig;
import gov.nist.healthcare.cds.domain.TestCase;
import gov.nist.healthcare.cds.domain.TestCaseGroup;
import gov.nist.healthcare.cds.domain.TestPlan;
import gov.nist.healthcare.cds.domain.wrapper.Report;
import gov.nist.hit.resources.deploy.api.FITSClient;
import gov.nist.hit.resources.deploy.client.SSLFITSClient;
import gov.nist.hit.resources.deploy.exception.InsupportedApiMethod;

public class Main {

	public static void main(String[] args) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, IOException, InsupportedApiMethod {
//		java.lang.System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
		FITSClient client = new SSLFITSClient("https://fits.nist.gov/", "hossam",  "12QWASZx");
		List<TestPlan> tps = client.getTestPlans().getBody();
		List<TestCase> tcl = new ArrayList<>();
		
//		for(TestPlan tp : tps){
//			System.out.println("[TP] "+tp.getName());
//			for(TestCaseGroup )
//		}
		
		
		List<SoftwareConfig> scl = client.getSoftwareConfiguration().getBody();
		tcl = Arrays.asList(tps.get(0).getTestCaseGroups().get(0).getTestCases().get(0));
		System.out.println("SIZE TCL : "+tcl.size());
		List<Report> report = client.execute(scl.get(0), new Date(), tcl).getBody();
		System.out.println(report);
		System.out.println("SIZE REPORT : "+ report.size());
		Report r = report.get(0);
		System.out.println(r.getFailures());
		System.out.println(r.getFcValidation().get(0).getEarliest().getValue());
//		client.execute(null, null, new ArrayList<TestCase>());
//		System.out.println(client.getTestPlans());
	}
}
