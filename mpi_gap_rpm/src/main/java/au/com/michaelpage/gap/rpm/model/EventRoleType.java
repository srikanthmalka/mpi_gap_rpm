package au.com.michaelpage.gap.rpm.model;

public enum EventRoleType {
	CANDIDATE("D");
	
	private String code;
	
	
	
	public String getCode() {
		return code;
	}



	EventRoleType(String code) {
		this.code = code;
	}
}
