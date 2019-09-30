package au.com.michaelpage.gap.rpm.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "opportunity")
public class Opportunities {
	@XmlElement(name = "opportunity", type = Opportunity.class)
	private List<Opportunity> opportunities = new ArrayList<Opportunity>();
	
	public Opportunities() {}
	
	public Opportunities(List<Opportunity> opportunities) {
		this.opportunities = opportunities;
	}

	public List<Opportunity> getOpportunities() {
		return opportunities;
	}

	public void setOpportunities(List<Opportunity> opportunities) {
		this.opportunities = opportunities;
	}
}
