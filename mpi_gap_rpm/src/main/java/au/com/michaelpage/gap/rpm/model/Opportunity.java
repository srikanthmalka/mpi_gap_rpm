package au.com.michaelpage.gap.rpm.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import au.com.michaelpage.gap.rpm.DbUnitDateAdapter;

@XmlType (name="opportunity")
public class Opportunity {
	private Integer opportunityRef;
	private String staffDiscipline;
	private String staffBroadbeanLogin;
	private Date createTimestamp;
	private boolean newOpportunity;
	
	public Integer getOpportunityRef() {
		return opportunityRef;
	}
	
	@XmlAttribute(name="opportunity_ref")
	public void setOpportunityRef(Integer opportunityRef) {
		this.opportunityRef = opportunityRef;
	}

	public String getStaffDiscipline() {
		return staffDiscipline;
	}

	@XmlAttribute(name="staff_discipline")
	public void setStaffDiscipline(String staffDiscipline) {
		this.staffDiscipline = staffDiscipline;
	}

	public String getStaffBroadbeanLogin() {
		return staffBroadbeanLogin;
	}

	@XmlAttribute(name="staff_external_userid")
	public void setStaffBroadbeanLogin(String staffBroadbeanLogin) {
		this.staffBroadbeanLogin = staffBroadbeanLogin;
	}

	public Date getCreateTimestamp() {
		return createTimestamp;
	}

	@XmlAttribute(name="create_timestamp")
	@XmlJavaTypeAdapter(DbUnitDateAdapter.class)
	public void setCreateTimestamp(Date createTimestamp) {
		this.createTimestamp = createTimestamp;
	}

	public boolean isNewOpportunity() {
		return newOpportunity;
	}

	@XmlAttribute(name="is_new_opportunity")
	public void setNewOpportunity(boolean newOpportunity) {
		this.newOpportunity = newOpportunity;
	}
	
}
