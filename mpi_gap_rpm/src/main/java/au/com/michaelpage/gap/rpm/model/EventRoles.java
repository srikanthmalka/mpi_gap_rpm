package au.com.michaelpage.gap.rpm.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "event_role")
public class EventRoles {
	@XmlElement(name = "event_role", type = EventRole.class)
	private List<EventRole> eventRoles = new ArrayList<EventRole>();
	
	public EventRoles() {}
	
	public EventRoles(List<EventRole> eventRoles) {
		this.eventRoles = eventRoles;
	}

	public List<EventRole> getEventRoles() {
		return eventRoles;
	}

	public void setEventRoles(List<EventRole> eventRoles) {
		this.eventRoles = eventRoles;
	}
	
}
