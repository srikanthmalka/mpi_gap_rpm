package au.com.michaelpage.gap.rpm.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlType (name="permanent_booking")
public class PermPlacement {
	private Integer permPlacementRef;
	private Integer eventRef;
	private Float feeAmount;
	private String placementType;
	private String currencyCode;
	
	public Integer getPermPlacementRef() {
		return permPlacementRef;
	}
	
	@XmlAttribute(name="account_tran_ref")
	public void setPermPlacementRef(Integer permPlacementRef) {
		this.permPlacementRef = permPlacementRef;
	}
	
	public Integer getEventRef() {
		return eventRef;
	}
	
	@XmlAttribute(name="event_ref")
	public void setEventRef(Integer eventRef) {
		this.eventRef = eventRef;
	}

	public Float getFeeAmount() {
		return feeAmount;
	}

	@XmlAttribute(name="total_fee_amount")
	public void setFeeAmount(Float feeAmount) {
		this.feeAmount = feeAmount;
	}

	public String getPlacementType() {
		return placementType;
	}

	@XmlAttribute(name="placement_type")
	public void setPlacementType(String placementType) {
		this.placementType = placementType;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	@XmlAttribute(name="currency_code")
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	
}
