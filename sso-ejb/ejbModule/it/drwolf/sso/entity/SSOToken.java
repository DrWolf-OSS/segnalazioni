package it.drwolf.sso.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

@Entity
public class SSOToken {
	private String uuid;
	private List<Info> infos = new ArrayList<Info>();

	@Transient
	public HashMap<String, String> getInfo() {
		HashMap<String, String> map = new HashMap<String, String>();
		for (Info i : this.infos) {
			map.put(i.getKey(), i.getValue());
		}
		return map;
	}

	@OneToMany(mappedBy = "ssoToken", cascade = CascadeType.ALL)
	public List<Info> getInfos() {
		return this.infos;
	}

	@Id
	public String getUuid() {
		return this.uuid;
	}

	public void setInfo(HashMap<String, String> info) {
		if (info == null) {
			return;
		}
		for (Entry<String, String> e : info.entrySet()) {
			Info i = new Info();
			i.setSsoToken(this);
			i.setKey(e.getKey());
			i.setValue(e.getValue());
			this.getInfos().add(i);
		}
	}

	public void setInfos(List<Info> infos) {
		this.infos = infos;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
}
