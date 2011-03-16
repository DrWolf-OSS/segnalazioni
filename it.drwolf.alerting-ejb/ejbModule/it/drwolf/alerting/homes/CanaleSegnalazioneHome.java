package it.drwolf.alerting.homes;

import it.drwolf.alerting.entity.*;
import java.util.ArrayList;
import java.util.List;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("canaleSegnalazioneHome")
public class CanaleSegnalazioneHome extends EntityHome<CanaleSegnalazione> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3911578785549719013L;

	public void setCanaleSegnalazioneId(Integer id) {
		setId(id);
	}

	public Integer getCanaleSegnalazioneId() {
		return (Integer) getId();
	}

	@Override
	protected CanaleSegnalazione createInstance() {
		CanaleSegnalazione canaleSegnalazione = new CanaleSegnalazione();
		return canaleSegnalazione;
	}

	public void wire() {
	}

	public boolean isWired() {
		return true;
	}

	public CanaleSegnalazione getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<Segnalazione> getSegnalaziones() {
		return getInstance() == null ? null : new ArrayList<Segnalazione>(
				getInstance().getSegnalaziones());
	}

}
