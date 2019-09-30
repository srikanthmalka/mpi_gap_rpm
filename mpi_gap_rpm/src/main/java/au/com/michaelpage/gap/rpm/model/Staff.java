package au.com.michaelpage.gap.rpm.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType (name="staff")
public class Staff {
	private Integer staffRef;
	private Integer personTypeRef;
	private String team;
	private String position;
	private String respUserCode;
	private String emailAddress;
	
	public Integer getStaffRef() {
		return staffRef;
	}
	
	@XmlAttribute(name="staff_ref")
	public void setStaffRef(Integer staffRef) {
		this.staffRef = staffRef;
	}
	
	public Integer getPersonTypeRef() {
		return personTypeRef;
	}
	
	
	@XmlElement(name="person_type_ref")
	public void setPersonTypeRef(Integer personTypeRef) {
		this.personTypeRef = personTypeRef;
	}
	
	public String getTeam() {
		return team;
	}
	
	@XmlAttribute(name="team")
	public void setTeam(String team) {
		this.team = team;
	}
	
	public String getPosition() {
		return position;
	}
	
	@XmlAttribute(name="position")
	public void setPosition(String position) {
		this.position = position;
	}
	
	public String getRespUserCode() {
		return respUserCode;
	}
	
	@XmlAttribute(name="resp_user_code")
	public void setRespUserCode(String respUserCode) {
		this.respUserCode = respUserCode;
	}
	
	public String getEmailAddress() {
		return emailAddress;
	}
	
	@XmlAttribute(name="email_address")
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

}
