package au.com.michaelpage.gap.rpm.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "permanent_booking")
public class PermPlacements {
	@XmlElement(name = "permanent_booking", type = PermPlacement.class)
	private List<PermPlacement> permPlacements = new ArrayList<PermPlacement>();
	
	public PermPlacements() {}
	
	public PermPlacements(List<PermPlacement> permPlacements) {
		this.permPlacements = permPlacements;
	}

	public List<PermPlacement> getPermPlacements() {
		return permPlacements;
	}

	public void setPermPlacements(List<PermPlacement> permPlacements) {
		this.permPlacements = permPlacements;
	}
}
