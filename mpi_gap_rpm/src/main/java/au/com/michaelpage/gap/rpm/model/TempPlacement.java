package au.com.michaelpage.gap.rpm.model;

import java.util.Date;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import au.com.michaelpage.gap.common.util.Md5Util;
import au.com.michaelpage.gap.common.util.Util;
import au.com.michaelpage.gap.rpm.DbUnitDateAdapter;

@XmlType (name="temporary_booking")
public class TempPlacement {
	private Integer tempPlacementRef;
	private Integer eventRef;
	private Date startDate;
	private Date endDate;
	private Float ratePayment;
	private Float rateInvoice;
	private String timeUnit;
	private Float hoursPerDay;
	private Float daysPerWeek;
	private String emailAddress;
	private String md5EmailAddress = Md5Util.hash(UUID.randomUUID().toString()); // this is to make sure null email will always have randomly generated hash.
	private Float totalFee;
	private String opportunityRef;
	private Date createTimestamp;
	
	public Integer getTempPlacementRef() {
		return tempPlacementRef;
	}
	
	@XmlAttribute(name="temporary_booking_ref")
	public void setTempPlacementRef(Integer tempPlacementRef) {
		this.tempPlacementRef = tempPlacementRef;
	}
	
	public Integer getEventRef() {
		return eventRef;
	}
	
	@XmlAttribute(name="event_ref")
	public void setEventRef(Integer eventRef) {
		this.eventRef = eventRef;
	}
	
	public Date getStartDate() {
		return startDate;
	}
	
	@XmlAttribute(name="start_date")
	@XmlJavaTypeAdapter(DbUnitDateAdapter.class)
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	public Date getEndDate() {
		return endDate;
	}
	
	@XmlAttribute(name="end_date")
	@XmlJavaTypeAdapter(DbUnitDateAdapter.class)
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	public Float getRatePayment() {
		return ratePayment;
	}
	
	@XmlAttribute(name="rate1_payment")
	public void setRatePayment(Float ratePayment) {
		this.ratePayment = ratePayment;
	}
	
	public Float getRateInvoice() {
		return rateInvoice;
	}
	
	@XmlAttribute(name="rate1_invoice")
	public void setRateInvoice(Float rateInvoice) {
		this.rateInvoice = rateInvoice;
	}
	
	public String getTimeUnit() {
		return timeUnit;
	}
	
	@XmlAttribute(name="time_unit")
	public void setTimeUnit(String timeUnit) {
		this.timeUnit = timeUnit;
	}
	public Float getHoursPerDay() {
		return hoursPerDay;
	}
	
	@XmlAttribute(name="hours_per_day")
	public void setHoursPerDay(Float hoursPerDay) {
		this.hoursPerDay = hoursPerDay;
	}
	
	public Float getDaysPerWeek() {
		return daysPerWeek;
	}
	
	@XmlAttribute(name="days_per_week")
	public void setDaysPerWeek(Float daysPerWeek) {
		this.daysPerWeek = daysPerWeek;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	@XmlAttribute(name="email")
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
		if (!Util.isEmpty(this.emailAddress)) {
			this.emailAddress = this.emailAddress.replaceAll("!", "").trim().toLowerCase(); 
			this.md5EmailAddress = Md5Util.hash(this.emailAddress);
		}
	}

	public String getMd5EmailAddress() {
		return md5EmailAddress;
	}

	public void setMd5EmailAddress(String md5EmailAddress) {
		this.md5EmailAddress = md5EmailAddress;
	}
	
	public Float getTotalFee() {
		return totalFee;
	}

	@XmlAttribute(name="total_fee")
	public void setTotalFee(Float totalFee) {
		this.totalFee = totalFee;
	}

	public String getOpportunityRef() {
		return opportunityRef;
	}

	@XmlAttribute(name="opportunity_ref")
	public void setOpportunityRef(String opportunityRef) {
		this.opportunityRef = opportunityRef;
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
