package it.drwolf.alerting.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.jboss.seam.annotations.Name;

@Name("formatter")
public class Formatter {


	public String formatDate(Date data) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		return sdf.format(data);
	}

}
