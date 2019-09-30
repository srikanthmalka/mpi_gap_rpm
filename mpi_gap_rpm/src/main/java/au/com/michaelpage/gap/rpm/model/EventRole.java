package au.com.michaelpage.gap.rpm.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlType (name="event_role")
public class EventRole {
	private Integer eventRef;
	private String type;
	private Integer personRef;
	
	public Integer getPersonRef() {
		return personRef;
	}

	@XmlAttribute(name="person_ref")
	public void setPersonRef(Integer personRef) {
		this.personRef = personRef;
	}

	public Integer getEventRef() {
		return eventRef;
	}
	
	@XmlAttribute(name="event_ref")
	public void setEventRef(Integer eventRef) {
		this.eventRef = eventRef;
	}
	
	public String getType() {
		return type;
	}
	
	@XmlAttribute(name="type")
	public void setType(String type) {
		this.type = type;
	}
}
