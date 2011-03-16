package it.drwolf.alerting.entity;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.jboss.envers.Versioned;

@Versioned
@Entity
public class BPMInfo {

	private Integer id;

	private Long processId;

	private Long tokenId;

	private String currentTask;

	public String getCurrentTask() {
		return this.currentTask;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	public Integer getId() {
		return this.id;
	}

	public Long getProcessId() {
		return this.processId;
	}

	public Long getTokenId() {
		return this.tokenId;
	}

	public void setCurrentTask(String currentTask) {
		this.currentTask = currentTask;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setProcessId(Long processId) {
		this.processId = processId;
	}

	public void setTokenId(Long tokenId) {
		this.tokenId = tokenId;
	}

}
