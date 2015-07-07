package it.drwolf.iscrizioni.session;

import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.commons.mail.SimpleEmail;
import org.hibernate.validator.Email;
import org.jboss.envers.VersionsReader;
import org.jboss.envers.VersionsReaderFactory;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.international.StatusMessage.Severity;

import au.com.bytecode.opencsv.CSVReader;
import it.drwolf.iscrizioni.entity.AppParam;
import it.drwolf.iscrizioni.entity.CategoriaOpzioniServizio;
import it.drwolf.iscrizioni.entity.Gruppo;
import it.drwolf.iscrizioni.entity.IscRevisionEntity;
import it.drwolf.iscrizioni.entity.Iscritto;
import it.drwolf.iscrizioni.entity.OpzioneServizio;
import it.drwolf.iscrizioni.entity.Servizio;
import it.drwolf.iscrizioni.util.IdGenerator;

@Name("iscrittoHome")
public class IscrittoHome extends EntityHome<Iscritto> {

	private static final long serialVersionUID = 8508651279246558361L;

	private String email;

	private String query;

	private String secret;

	private String importData;

	private Integer revision;

	private HashSet<OpzioneServizio> opzioni = new HashSet<OpzioneServizio>();

	@In(create = true)
	private ServizioList servizioList;

	private Boolean notifica = false;

	public void aggiungiServizio(Servizio s) {
		this.getInstance().getServizi().add(s);
	}

	private void beforeImport(Iscritto i) {
		for (OpzioneServizio os : i.getOpzioniServizi()) {
			i.getServizi().add(os.getCategoriaOpzioniServizio().getServizio());
		}
	}

	private void beforeUpdate() {
		Iscritto i = this.getInstance();
		this.beforeUpdate(i);
	}

	private void beforeUpdate(Iscritto i) {
		i.setOpzioniServizi(new ArrayList<OpzioneServizio>());
		for (Servizio s : this.servizioList.getResultList()) {
			i.getServizi().remove(s);
			for (CategoriaOpzioniServizio cos : s.getCategorieOpzioni()) {
				for (OpzioneServizio os : cos.getOpzioniServizio4Iscritto()) {
					i.getOpzioniServizi().add(os);

					if (!i.getServizi().contains(s)) {
						i.getServizi().add(s);
					}
				}

				if (cos.getOpzioneServizio4Iscritto() != null) {
					i.getOpzioniServizi().add(cos.getOpzioneServizio4Iscritto());

				}

			}
		}
	}

	@Override
	protected Iscritto createInstance() {
		Iscritto iscritto = new Iscritto();
		iscritto.setId(IdGenerator.newId(this.getEntityManager()));
		return iscritto;
	}

	@SuppressWarnings("unchecked")
	public void createNew() {
		List<Iscritto> l = this.getEntityManager().createQuery("from Iscritto where email=:email")
				.setParameter("email", this.email).getResultList();
		if (l.size() > 0) {
			Iscritto iscritto = l.get(0);
			String url = this.getEditUrl(iscritto);

			this.sendSimpleMail(iscritto.getEmail(),
					this.getEntityManager().find(AppParam.class, AppParam.APP_NAME).getValue() + " - Nuova Iscrizione",
					"E' stata richiesta una nuova iscrizione per " + iscritto.getEmail()
							+ ", l'utente risulta gia' iscritto,  per modificare o cancellare l'iscrizione visita il seguente link:\r\n\r\n"
							+ url);
		} else {
			Iscritto iscritto = new Iscritto();
			iscritto.setId(IdGenerator.newId(this.getEntityManager()));
			iscritto.setEmail(this.email);
			this.getEntityManager().persist(iscritto);

			String url = this.getEditUrl(iscritto);

			this.sendSimpleMail(iscritto.getEmail(),
					this.getEntityManager().find(AppParam.class, AppParam.APP_NAME).getValue() + " - Nuova Iscrizione",
					"E' stata richiesta una nuova iscrizione per " + iscritto.getEmail()
							+ " per completare o annullare l'iscrizione visita il seguente link:\r\n\r\n" + url);

		}

		FacesMessages.instance().add(
				this.getEntityManager().find(AppParam.class, AppParam.APP_NEW_SUBSCRIPTION).getValue(), this.email);
	}

	public Iscritto getDefinedInstance() {
		return this.isIdDefined() ? this.getInstance() : null;
	}

	private String getEditUrl(Iscritto iscritto) {
		final String i = "/ie/";
		return this.getUrl(iscritto, i);
	}

	@Email(message = "L'email inserita non \u0232 valida")
	public String getEmail() {
		return this.email;
	}

	@Factory("hiddenFields")
	public List<String> getHiddenFields() {
		AppParam p = this.getEntityManager().find(AppParam.class, AppParam.APP_FIELDS_HIDDEN);
		if (p == null || p.getValue() == null) {
			return Collections.emptyList();
		} else {
			return Arrays.asList(p.getValue().toLowerCase().split(","));
		}
	}

	public String getImportData() {
		return this.importData;
	}

	public String getIscrittoId() {
		return (String) this.getId();
	}

	public Boolean getNotifica() {
		return this.notifica;
	}

	public String getPrivacyUrl() {
		return this.getEntityManager().find(AppParam.class, AppParam.APP_PRIVACY_URL).getValue();
	}

	public String getQuery() {
		return this.query;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String getQueryResult() {
		try {
			if (this.getEntityManager().find(AppParam.class, AppParam.APP_SECRET).getValue().equals(this.getSecret())) {
				String res = "OK\n";
				List results = this.getEntityManager().createQuery(this.query).getResultList();
				if (results.size() == 0) {
					return res;
				}
				if (results.get(0) instanceof Iscritto) {
					List<Iscritto> l = results;
					for (Iscritto iscritto : l) {
						res += iscritto.getId() + "\t";
						res += iscritto.getEmail() + "\t";
						res += iscritto.getCognome() + "\t";
						res += iscritto.getNome() + "\t";
						res += iscritto.getCellulare() + "\t";
						res += iscritto.getIndirizzo() + "\t";
						res += iscritto.isTextMail() + "\t";
						res += iscritto.getConsenso() + "\t";
						res += iscritto.getConfermato() + "\t";
						res += "\n";
					}
					return res;
				} else if (results.get(0) instanceof Gruppo) {
					List<Gruppo> l = results;
					for (Gruppo gruppo : l) {
						res += gruppo.getId() + "\t";
						res += gruppo.getNome();
						res += "\n";
					}
					return res;

				} else {
					return "KO\nquery non consentita";
				}

			} else {
				return "KO\nsecret errato";
			}
		} catch (Exception e) {
			return "KO\n" + e.getMessage();
		}
	}

	@Factory("requiredFields")
	public List<String> getRequiredFields() {
		AppParam p = this.getEntityManager().find(AppParam.class, AppParam.APP_FIELDS_REQ);
		if (p == null || p.getValue() == null) {
			return Collections.emptyList();
		} else {
			return Arrays.asList(p.getValue().toLowerCase().split(","));
		}
	}

	public Integer getRevision() {
		return this.revision;
	}

	public List<Object[]> getRevisions() {
		VersionsReader reader = VersionsReaderFactory.get(this.getEntityManager());
		List<Number> revisions = reader.getRevisions(this.getEntityClass(), this.getIscrittoId());
		List<Object[]> infos = new ArrayList<Object[]>();
		for (Number n : revisions) {
			try {
				IscRevisionEntity ire = reader.findRevision(IscRevisionEntity.class, n);
				infos.add(new Object[] { n, ire.getUsername(), new Date(ire.getTimestamp()) });
			} catch (Exception e) {
				System.out.println(e);
			}
		}
		return infos;
	}

	public String getSecret() {
		return this.secret;
	}

	public List<Servizio> getServiziDisponibili() {
		final List<Servizio> resultList = new ArrayList<Servizio>(this.servizioList.getResultList());
		resultList.removeAll(this.getInstance().getServizi());
		return resultList;
	}

	private String getUrl(Iscritto iscritto) {
		final String i = "/i/";
		return this.getUrl(iscritto, i);
	}

	private String getUrl(Iscritto iscritto, final String i) {
		String url = this.getEntityManager().find(AppParam.class, AppParam.APP_URL).getValue();

		url += i + iscritto.getId();
		return url;
	}

	@SuppressWarnings("unchecked")
	public void importa() throws IOException {

		System.out.println("importData: " + this.importData);

		CSVReader reader = null;
		if (("" + this.importData).startsWith("/")) {
			reader = new CSVReader(new FileReader(this.importData));
		} else {
			reader = new CSVReader(new StringReader(this.importData));

		}

		List<String[]> all = reader.readAll();

		String[] header = all.get(0);

		System.out.println(all.size() + " lines");

		for (int r = 1; r < all.size(); r++) {
			String[] line = all.get(r);
			Iscritto iscritto = new Iscritto();
			String[] gruppi = new String[0];
			String[] opzioni = new String[0];
			for (int i = 0; i < line.length; i++) {
				if (header[i].equalsIgnoreCase("cap")) {
					iscritto.setCap(line[i]);
				}
				if (header[i].equalsIgnoreCase("cellulare")) {
					iscritto.setCellulare(line[i]);
				}
				if (header[i].equalsIgnoreCase("cognome")) {
					iscritto.setCognome(line[i]);
				}
				if (header[i].equalsIgnoreCase("email")) {
					iscritto.setEmail(line[i]);
				}
				if (header[i].equalsIgnoreCase("fax")) {
					iscritto.setFax(line[i]);
				}
				if (header[i].equalsIgnoreCase("indirizzo")) {
					iscritto.setIndirizzo(line[i]);
				}
				if (header[i].equalsIgnoreCase("localita")) {
					iscritto.setLocalita(line[i]);
				}
				if (header[i].equalsIgnoreCase("nome")) {
					iscritto.setNome(line[i]);
				}
				if (header[i].equalsIgnoreCase("ragionesociale")) {
					iscritto.setRagioneSociale(line[i]);
				}
				if (header[i].equalsIgnoreCase("textmail")) {
					iscritto.setTextMail(line[i].equals("true"));
				}
				if (header[i].equalsIgnoreCase("gruppi")) {
					gruppi = line[i].split("\\s");
				}
				if (header[i].equalsIgnoreCase("opzioni")) {
					opzioni = line[i].split("\\s");
				}

			}
			if (iscritto.getEmail() != null) {
				List<Iscritto> resultList = this.getEntityManager().createQuery("from Iscritto where email=:email")
						.setParameter("email", iscritto.getEmail()).getResultList();
				if (resultList.size() > 0) {
					iscritto = resultList.get(0);
					for (int i = 0; i < line.length; i++) {
						if (header[i].equalsIgnoreCase("cap")) {
							iscritto.setCap(line[i]);
						}
						if (header[i].equalsIgnoreCase("cellulare")) {
							iscritto.setCellulare(line[i]);
						}
						if (header[i].equalsIgnoreCase("cognome")) {
							iscritto.setCognome(line[i]);
						}
						if (header[i].equalsIgnoreCase("email")) {
							iscritto.setEmail(line[i]);
						}
						if (header[i].equalsIgnoreCase("fax")) {
							iscritto.setFax(line[i]);
						}
						if (header[i].equalsIgnoreCase("indirizzo")) {
							iscritto.setIndirizzo(line[i]);
						}
						if (header[i].equalsIgnoreCase("localita")) {
							iscritto.setLocalita(line[i]);
						}
						if (header[i].equalsIgnoreCase("nome")) {
							iscritto.setNome(line[i]);
						}
						if (header[i].equalsIgnoreCase("ragionesociale")) {
							iscritto.setRagioneSociale(line[i]);
						}
						if (header[i].equalsIgnoreCase("textmail")) {
							iscritto.setTextMail(line[i].equals("true"));
						}
						if (header[i].equalsIgnoreCase("gruppi")) {
							gruppi = line[i].split("\\s");
						}
						if (header[i].equalsIgnoreCase("opzioni")) {
							opzioni = line[i].split("\\s");
						}

					}

					FacesMessages.instance().add(Severity.INFO, iscritto.getEmail() + " già presente");
					System.out.println("già presente " + iscritto.getEmail());
				}
			}
			for (String oid : opzioni) {
				iscritto.getOpzioniServizi().add(this.getEntityManager().find(OpzioneServizio.class, oid));
			}
			this.beforeImport(iscritto);
			if (iscritto.getId() == null) {
				iscritto.setId(IdGenerator.newId(this.getEntityManager()));
				this.getEntityManager().persist(iscritto);
			} else {
				this.getEntityManager().merge(iscritto);
			}
			for (String gid : gruppi) {
				this.getEntityManager()
						.createNativeQuery(
								"insert ignore into Gruppo_Iscritto (gruppi_id,iscritti_id) values (:gid,:iid)")
						.setParameter("gid", gid).setParameter("iid", iscritto.getId());
			}
			FacesMessages.instance().add(Severity.INFO, iscritto.getEmail() + " importato");
			System.out.println("importato " + iscritto.getEmail());
			this.getEntityManager().flush();
			this.getEntityManager().clear();
		}
		reader.close();

	}

	public boolean isWired() {
		return true;
	}

	public List<Servizio> loadServizi() {
		this.opzioni.clear();
		List<Servizio> servizi = this.servizioList.getResultList();
		for (Servizio s : servizi) {
			for (CategoriaOpzioniServizio cat : s.getCategorieOpzioni()) {
				for (OpzioneServizio os : this.getInstance().getOpzioniServizi()) {

					if (os.getCategoriaOpzioniServizio().getId().equals(cat.getId())) {
						this.opzioni.add(os);
						if (cat.getMultiple()) {
							cat.getOpzioniServizio4Iscritto().add(os);
						} else {
							cat.setOpzioneServizio4Iscritto(os);
						}
					}

				}
				if (this.getInstance().getOpzioniServizi().size() == 0) {
					for (OpzioneServizio os : cat.getOpzioniServizio()) {
						if (os.getDef()) {
							this.opzioni.add(os);
							if (cat.getMultiple()) {
								cat.getOpzioniServizio4Iscritto().add(os);
							} else {
								cat.setOpzioneServizio4Iscritto(os);
							}
						}
					}
				}

			}
		}
		return servizi;
	}

	@Override
	public String persist() {
		this.beforeUpdate();
		String res = super.persist();
		this.email = this.getInstance().getEmail();
		this.rememberCode();
		return res;

	}

	@SuppressWarnings("unchecked")
	public void rememberCode() {
		List<Iscritto> l = this.getEntityManager().createQuery("from Iscritto where email=:email")
				.setParameter("email", this.email).getResultList();
		if (l.size() > 0) {
			Iscritto iscritto = l.get(0);
			String url = this.getUrl(iscritto);

			this.sendSimpleMail(iscritto.getEmail(),
					this.getEntityManager().find(AppParam.class, AppParam.APP_NAME).getValue() + " - Codice utente",
					"Il codice utente per " + iscritto.getEmail() + " e' il seguente:\r\n\r\n" + iscritto.getId()
							+ "\r\n\r\n"

			+ "Per accedere direttamente ai dettagli dell'iscrizione visita il seguente link:\r\n" + url);
		}
		FacesMessages.instance().add(
				this.getEntityManager().find(AppParam.class, AppParam.APP_EDIT_SUBSCRIPTION).getValue(), this.email);

	}

	@Override
	public String remove() {
		Iscritto i = this.getInstance();
		for (Gruppo g : i.getGruppi()) {
			g.getIscritti().remove(i);
		}
		return super.remove();
	}

	public void rimuoviServizio(Servizio s) {

		this.getInstance().getServizi().remove(s);
	}

	private void sendSimpleMail(String to, String subject, String message) {
		try {
			SimpleEmail email = new SimpleEmail();
			EntityManager em = this.getEntityManager();

			email.setHostName(em.find(AppParam.class, AppParam.APP_MAIL_HOST).getValue());
			email.setFrom(em.find(AppParam.class, AppParam.APP_MAIL_FROM_ADDRESS).getValue(),
					em.find(AppParam.class, AppParam.APP_MAIL_FROM_NAME).getValue());

			AppParam port = em.find(AppParam.class, AppParam.APP_MAIL_PORT);
			if (port != null) {
				email.setSmtpPort(Integer.parseInt(port.getValue()));
			}

			AppParam pwd = em.find(AppParam.class, AppParam.APP_MAIL_PASSWORD);
			if (pwd != null) {
				AppParam user = em.find(AppParam.class, AppParam.APP_MAIL_USER);
				email.setAuthentication(user.getValue(), pwd.getValue());
			}
			email.addTo(to);
			email.setSubject(subject);
			email.setMsg(message);
			email.send();
		} catch (Exception e) {
			FacesMessages.instance()
					.add("Errore nella spedizione del messaggio di conferma, per favore riprova più tardi");
		}

	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setImportData(String importData) {
		this.importData = importData;
	}

	public void setIscrittoId(String id) {
		this.setId(id);
	}

	public void setNotifica(Boolean notifica) {
		this.notifica = notifica;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public void setRevision(Integer revision) {
		this.revision = revision;
		if (revision == null) {
			return;
		}
		VersionsReader reader = VersionsReaderFactory.get(this.getEntityManager());
		this.setInstance(reader.find(this.getEntityClass(), this.getIscrittoId(), this.revision));
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	@Override
	public String update() {
		this.beforeUpdate();
		return super.update();
	}

	@SuppressWarnings("unchecked")
	public void updateAll() {
		for (Iscritto i : (List<Iscritto>) this.getEntityManager().createQuery("from Iscritto where confermato=true")
				.setMaxResults(150).getResultList()) {
			i.setConfermato(false);
			this.getEntityManager().merge(i);
			this.getEntityManager().flush();
			System.out.println("merged " + i);
		}
	}

	public String verifyId() {
		return this.isManaged() ? "OK" : "KO";
	}

	public void wire() {
		this.getInstance();
	}
}
