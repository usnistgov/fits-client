package gov.nist.hit.resources.deploy.client;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import gov.nist.healthcare.cds.domain.TestCase;
import gov.nist.healthcare.cds.domain.TestPlan;
import gov.nist.healthcare.cds.domain.wrapper.Report;
import gov.nist.hist.resources.deploy.ssl.SSLRestTemplateFactory;
import gov.nist.hit.resources.deploy.api.FITSClient;
import gov.nist.hit.resources.deploy.config.FITSApiConfig;
import gov.nist.hit.resources.deploy.exception.InsupportedApiMethod;
import gov.nist.hit.resources.deploy.model.Action;
import gov.nist.hit.resources.deploy.model.ClientSoftwareConfig;
import gov.nist.hit.resources.deploy.model.ClientTransientExecRequest;


@Service
public class SSLFITSClient implements FITSClient {

	private RestTemplate wire;
	private FITSApiConfig config;
	private String token;
	private String host;
	
	
	public SSLFITSClient(String host, String token) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, IOException{
		this.token = token;
		this.host = host;
		config = new FITSApiConfig();
		wire = SSLRestTemplateFactory.createSSLRestTemplate();
	}
	
	public SSLFITSClient(String host, String username, String password) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, IOException{
		this(host,tokenize(username,password));
	}
	
	private String createURL(String mapping){
		return host + mapping; 
	}

	public HttpHeaders headers(){
		HttpHeaders headers = new HttpHeaders();
	    headers.add("Authorization", "Basic " + token);
	    return headers;
	}
	
	@Override
	public ResponseEntity<List<Report>> execute(ClientSoftwareConfig software, Date relativeAssessmentDate, List<TestCase> tcs) throws InsupportedApiMethod {
		ClientTransientExecRequest exec = new ClientTransientExecRequest();
		for(TestCase tc : tcs){
			exec.addTestCase(tc.getId());
		}
		exec.setSoftware(software);
		exec.setDate(relativeAssessmentDate);
		HttpEntity<?> httpEntity = new HttpEntity<Object>(exec,this.headers());
		System.out.println(config.urlFor(Action.EXECUTE));
		ResponseEntity<List<Report>> response = wire.exchange(createURL(config.urlFor(Action.EXECUTE)), HttpMethod.POST, httpEntity, new ParameterizedTypeReference<List<Report>>() {});
		return response;
	}

	@Override
	public ResponseEntity<List<TestPlan>> getTestPlans() throws InsupportedApiMethod {
		HttpEntity<?> httpEntity = new HttpEntity<Object>(this.headers());
		System.out.println(createURL(config.urlFor(Action.TESTPLANS)));
		ResponseEntity<List<TestPlan>> response = wire.exchange(createURL(config.urlFor(Action.TESTPLANS)), HttpMethod.GET, httpEntity, new ParameterizedTypeReference<List<TestPlan>>() {});
		return response;
	}
	
	@Override
	public ResponseEntity<List<ClientSoftwareConfig>> getSoftwareConfiguration() throws InsupportedApiMethod {
		HttpEntity<?> httpEntity = new HttpEntity<Object>(this.headers());
		System.out.println(createURL(config.urlFor(Action.TESTPLANS)));
		ResponseEntity<List<ClientSoftwareConfig>> response = wire.exchange(createURL(config.urlFor(Action.CONFIG)), HttpMethod.GET, httpEntity, new ParameterizedTypeReference<List<ClientSoftwareConfig>>() {});
		return response;
	}

	@Override
	public boolean validCredentials() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Basic " + token);
		HttpEntity<String> entity = new HttpEntity<String>(headers);
    	ResponseEntity<String> response = wire.exchange(this.createURL(config.login()), HttpMethod.GET,  entity, String.class);
    	return response.getStatusCode() == HttpStatus.OK;
	}

	public RestTemplate getWire() {
		return wire;
	}

	public void setWire(RestTemplate wire) {
		this.wire = wire;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}
	
	public static String tokenize(String username, String password){
		String plainCreds = username+":"+password;
    	byte[] plainCredsBytes = plainCreds.getBytes();
    	byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
    	return new String(base64CredsBytes);
	}



	
}
