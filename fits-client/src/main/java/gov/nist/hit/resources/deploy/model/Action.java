package gov.nist.hit.resources.deploy.model;

public enum Action {
	
	TESTPLANS("testplans"),
	EXECUTE("execute"),
	VALIDATE("validate"),
	CONFIG("config");

	private String code;

	
	private Action(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	
}
