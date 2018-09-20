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
import gov.nist.healthcare.cds.domain.wrapper.ForecastValidation;
import gov.nist.healthcare.cds.domain.wrapper.Report;
import gov.nist.healthcare.cds.enumeration.FHIRAdapter;
import gov.nist.hit.resources.deploy.api.FITSClient;
import gov.nist.hit.resources.deploy.client.SSLFITSClient;
import gov.nist.hit.resources.deploy.exception.InsupportedApiMethod;

public class Main {

	public static void main(String[] args) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, IOException, InsupportedApiMethod {
//		java.lang.System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
		FITSClient client = new SSLFITSClient("http://localhost:8080/", "hossam",  "12QWASZx");
		List<TestPlan> tps = client.getTestPlans().getBody();
		List<TestCase> tcl = new ArrayList<>();
		TestCase tcCase = null;
		
		for(TestPlan tp : tps){
//			System.out.println("[TP] "+tp.getName());
			for(TestCaseGroup tcg : tp.getTestCaseGroups()){
//				System.out.println("[TCG] "+tcg.getName());
				for(TestCase tc : tcg.getTestCases()){
//					System.out.println("[TC] "+tc.getName());
					if(tc.getUid().equals("2013-0806")){
						System.out.println("FOUND");
						tcCase = tc;
						break;
					}
				}
			}
		}
		
		ClientSoftwareConfig softwareConfig = new ClientSoftwareConfig();
		softwareConfig.setConnector(FHIRAdapter.HL7);
		softwareConfig.setEndPoint("http://immlab.pagekite.me/aart/soap");
		softwareConfig.setUserId("TEMP_CONN");
		softwareConfig.setPassword("3DNCUXBH4JPC48SAY3U");
		softwareConfig.setFacilityId("66A");
		
		
//		List<SoftwareConfig> scl = client.getSoftwareConfiguration().getBody();
		tcl = Arrays.asList(tps.get(0).getTestCases().get(0));
		System.out.println("SIZE TCL : "+tcl.size());

		
		List<Report> report = client.execute(softwareConfig, new Date(), Arrays.asList(tcCase)).getBody();
		System.out.println(report);
		System.out.println("SIZE REPORT : "+ report.size());
		Report r = report.get(0);
		for(ForecastValidation fv : r.getFcValidation()){
			System.out.println(fv.getForecastRequirement().getEarliest().asDate());
			System.out.println(fv.getEarliest().getValue().asDate());
		}
//		r.getFcValidation()
//		client.execute(null, null, new ArrayList<TestCase>());
//		System.out.println(client.getTestPlans());
	}
}
