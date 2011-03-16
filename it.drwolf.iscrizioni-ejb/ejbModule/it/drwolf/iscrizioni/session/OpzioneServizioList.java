package it.drwolf.iscrizioni.session;

import it.drwolf.iscrizioni.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("opzioneServizioList")
public class OpzioneServizioList extends EntityQuery<OpzioneServizio> {

	private static final String EJBQL = "select opzioneServizio from OpzioneServizio opzioneServizio";

	private static final String[] RESTRICTIONS = {
			"lower(opzioneServizio.id) like concat(lower(#{opzioneServizioList.opzioneServizio.id}),'%')",
			"lower(opzioneServizio.nome) like concat(lower(#{opzioneServizioList.opzioneServizio.nome}),'%')", };

	private OpzioneServizio opzioneServizio = new OpzioneServizio();

	public OpzioneServizioList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public OpzioneServizio getOpzioneServizio() {
		return opzioneServizio;
	}
}
