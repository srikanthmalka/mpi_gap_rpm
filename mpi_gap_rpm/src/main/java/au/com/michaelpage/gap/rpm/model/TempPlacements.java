package au.com.michaelpage.gap.rpm.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "temporary_booking")
public class TempPlacements {
	@XmlElement(name = "temporary_booking", type = TempPlacement.class)
	private List<TempPlacement> tempPlacements = new ArrayList<TempPlacement>();
	
	public TempPlacements() {}
	
	public TempPlacements(List<TempPlacement> tempPlacements) {
		this.tempPlacements = tempPlacements;
	}

	public List<TempPlacement> getTempPlacements() {
		return tempPlacements;
	}

	public void setTempPlacements(List<TempPlacement> tempPlacements) {
		this.tempPlacements = tempPlacements;
	}
}
