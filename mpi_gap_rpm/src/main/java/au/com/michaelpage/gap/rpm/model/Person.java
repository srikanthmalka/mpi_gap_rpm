package au.com.michaelpage.gap.rpm.model;

import java.util.Date;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import au.com.michaelpage.gap.common.util.Md5Util;
import au.com.michaelpage.gap.common.util.Util;
import au.com.michaelpage.gap.rpm.DbUnitDateAdapter;

@XmlType (name="person")
public class Person {
	private Integer personRef;
	private String emailAddress;
	private String md5EmailAddress = Md5Util.hash(UUID.randomUUID().toString()); // this is to make sure null email will always have randomly generated hash.
	private Date createTimestamp;
	
	public Integer getPersonRef() {
		return personRef;
	}
	
	@XmlAttribute(name="person_ref")
	public void setPersonRef(Integer personRef) {
		this.personRef = personRef;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	@XmlAttribute(name="email_address")
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

	@XmlAttribute(name="email_address_hashed")
	public void setMd5EmailAddress(String md5EmailAddress) {
		if (!Util.isEmpty(md5EmailAddress)) {
			this.md5EmailAddress = md5EmailAddress;
		}
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
