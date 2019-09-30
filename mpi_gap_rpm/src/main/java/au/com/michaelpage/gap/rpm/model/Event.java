package au.com.michaelpage.gap.rpm.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import au.com.michaelpage.gap.rpm.DbUnitDateAdapter;

@XmlType (name="event")
public class Event {
	private Integer eventRef;
	private String type;
	private Integer opportunityRef;
	private String displayName;
	private String jobBoard;
	private Date createTimestamp;
	
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

	public Integer getOpportunityRef() {
		return opportunityRef;
	}

	@XmlAttribute(name="opportunity_ref")
	public void setOpportunityRef(Integer opportunityRef) {
		this.opportunityRef = opportunityRef;
	}

	public String getDisplayName() {
		return displayName;
	}

	@XmlAttribute(name="displayname")
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getJobBoard() {
		return jobBoard;
	}

	@XmlAttribute(name="job_board")
	public void setJobBoard(String jobBoard) {
		this.jobBoard = jobBoard;
	}

	public Date getCreateTimestamp() {
		return createTimestamp;
	}

	@XmlAttribute(name="create_timestamp")
	@XmlJavaTypeAdapter(DbUnitDateAdapter.class)
	public void setCreateTimestamp(Date createTimestamp) {
		this.createTimestamp = createTimestamp;
	}
	
}