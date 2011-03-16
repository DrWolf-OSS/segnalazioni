package it.drwolf.eloise.web.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.NotNull;

@Entity
@Table(name = "AppParamWeb", catalog = "eloise")
public class AppParamWeb {
	private String id;
	private String value;

	public static final AppParamWeb SKIN = new AppParamWeb("skin", "blueSky");

	public static final AppParamWeb[] defaults = new AppParamWeb[] { AppParamWeb.SKIN };

	public AppParamWeb() {
	}

	public AppParamWeb(String key, String value) {
		super();
		this.id = key;
		this.value = value;
	}

	@Id
	@Column(unique = true, nullable = false)
	@NotNull
	public String getId() {
		return this.id;
	}

	public String getValue() {
		return this.value;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
