package it.drwolf.alerting.util;

import it.drwolf.alerting.entity.AppParam;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.mail.Session;
import javax.persistence.EntityManager;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;

@Name("formatter")
public class Formatter {


	public String formatDate(Date data) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		return sdf.format(data);
	}

}
