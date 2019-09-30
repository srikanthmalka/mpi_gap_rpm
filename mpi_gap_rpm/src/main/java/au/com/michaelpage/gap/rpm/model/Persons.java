package au.com.michaelpage.gap.rpm.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "person")
public class Persons {
	@XmlElement(name = "person", type = Person.class)
	private List<Person> persons = new ArrayList<Person>();
	
	public Persons() {}
	
	public Persons(List<Person> Persons) {
		this.persons = Persons;
	}

	public List<Person> getPersons() {
		return persons;
	}

	public void setPersons(List<Person> Persons) {
		this.persons = Persons;
	}
}
