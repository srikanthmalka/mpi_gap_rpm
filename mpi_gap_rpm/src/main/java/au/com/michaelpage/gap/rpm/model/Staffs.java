package au.com.michaelpage.gap.rpm.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Plural for staff is staff, but added s to make it consistent 
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "staff")
public class Staffs {
	@XmlElement(name = "staff", type = Staff.class)
	private List<Staff> staffs = new ArrayList<Staff>();
	
	public Staffs() {}
	
	public Staffs(List<Staff> staffs) {
		this.staffs = staffs;
	}

	public List<Staff> getStaffs() {
		return staffs;
	}

	public void setStaffs(List<Staff> staffs) {
		this.staffs = staffs;
	}

}
