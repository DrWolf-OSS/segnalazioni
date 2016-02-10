package it.drwolf.alerting.util;

public enum Constants {
	ADMIN("admin"), SMISTATORE("smistatore"), CITTADINO("cittadino"), SOGGETTO_AGGIUNTIVO("soggettoAggiuntivo"), INIZIA(
			"inizia"), UFFICIO_COMPETENTE("ufficioCompetente"), IMPIEGATO("impiegato"), CHIUDI_SEGNALAZIONE(
					"chiudiSegnalazione"), ATTENDI_CHIUSURA("attendiChiusura"), CANALE_WWW_NOME("www"), CANALE_WWW_DESC(
							"Internet"), CANALE_TEL_NOME("tel"), CANALE_TEL_DESC("Telefono"), CANALE_PERSONA_NOME(
									"persona"), CANALE_PERSONA_DESC("Di Persona"), ATTENDI_CHIUSURA_SEGNALAZIONE(
											"attendiChiusuraSegnalazione"), TERMINA_PROCESSO("terminaProcesso");

	private String value;

	private Constants(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return this.value;
	}

}
