package gov.nist.hit.resources.deploy.model;


import javax.persistence.EnumType;
import javax.persistence.Enumerated;


import gov.nist.healthcare.cds.enumeration.FHIRAdapter;

public class ClientSoftwareConfig {
	
	private String name;
	private String endPoint;
	@Enumerated(EnumType.STRING)
	private FHIRAdapter connector;
	private String userId;
	private String password;
	private String facilityId;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEndPoint() {
		return endPoint;
	}
	public void setEndPoint(String endPoint) {
		this.endPoint = endPoint;
	}
	public FHIRAdapter getConnector() {
		return connector;
	}
	public void setConnector(FHIRAdapter connector) {
		this.connector = connector;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getFacilityId() {
		return facilityId;
	}
	public void setFacilityId(String facilityId) {
		this.facilityId = facilityId;
	}
	
}
