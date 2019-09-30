package au.com.michaelpage.gap.rpm.model;

public enum EventType {
	CLIENT_INTERVIEW_ONE("P701"),
	CLIENT_INTERVIEW_TWO("P702"),
	CLIENT_INTERVIEW_THREE("P703"),
	CV_SENT("N23"),
	INVOICE("I");
	
	private String code;
	
	
	
	public String getCode() {
		return code;
	}



	EventType(String code) {
		this.code = code;
	}
}
