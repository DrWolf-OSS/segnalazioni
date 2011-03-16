package it.drwolf.alerting.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CollectionOfElements;

@Entity
public class GlobalRole {

	public static final String ADMIN = "admin";
	public static final String SUPERVISOR = "supervisor";

	public static final String[] defaults = new String[] { GlobalRole.ADMIN,
			GlobalRole.SUPERVISOR };

	private Integer id;
	private String name;
	private List<String> users;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return this.id;
	}

	@Column(unique = true, nullable = false)
	public String getName() {
		return this.name;
	}

	@CollectionOfElements
	public List<String> getUsers() {
		return this.users;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setUsers(List<String> users) {
		this.users = users;
	}
}
