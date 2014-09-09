package it.drwolf.alerting.util;

import it.drwolf.alerting.entity.Intervento;
import it.drwolf.alerting.entity.Segnalazione;
import it.drwolf.alerting.lists.ListaSegnalazioni;
import it.drwolf.alerting.session.AlertingController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.security.Identity;
import org.jbpm.taskmgmt.exe.TaskInstance;

@Name("xlsGenerator")
public class XlsGenerator {
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	private SimpleDateFormat xsdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	@In
	private ListaSegnalazioni listaSegnalazioni;
	@In
	private AlertingController alertingController;
	@In
	private ArrayList<TaskInstance> pooledTaskInstanceList;
	@In
	private ArrayList<TaskInstance> taskInstanceList;

	@In(value = "#{facesContext.externalContext}")
	private ExternalContext extCtx;

	private void downloadExcel(ByteArrayOutputStream excel, String filename) {
		HttpServletResponse response = (HttpServletResponse) this.extCtx.getResponse();
		response.setContentType("application/excel");

		response.addHeader("Content-disposition", "attachment; filename=\"" + filename + ".xls\"");
		response.setHeader("Expires", "0");
		response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
		response.setHeader("Pragma", "public");
		try {
			response.setContentLength(excel.size());
			ServletOutputStream os = response.getOutputStream();
			excel.writeTo(os);
			os.flush();
			os.close();
			FacesContext.getCurrentInstance().responseComplete();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private HSSFWorkbook generaHome() {
		boolean gestore = this.alertingController.isGestore(Identity.instance().getCredentials().getUsername());
		HSSFWorkbook wb = new HSSFWorkbook();
		if (this.pooledTaskInstanceList.size() > 0) {
			HSSFSheet sdpic = wb.createSheet("Segnalazioni da prendere in carico");
			HSSFRow row0 = sdpic.createRow(0);
			row0.createCell(0).setCellValue(new HSSFRichTextString("Segnalazione"));
			int i = 0;
			for (TaskInstance task : this.pooledTaskInstanceList) {
				i++;
				HSSFRow row = sdpic.createRow(i);
				row.createCell(0).setCellValue(new HSSFRichTextString(this.alertingController.getTitleForTask(task)));
			}
		}
		if (gestore) {

			HashMap<String, HSSFCellStyle> cmap = new HashMap<String, HSSFCellStyle>();
			HSSFCellStyle red = wb.createCellStyle();
			red.setFillForegroundColor(new HSSFColor.RED().getIndex());
			red.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

			HSSFCellStyle yellow = wb.createCellStyle();
			yellow.setFillForegroundColor(new HSSFColor.YELLOW().getIndex());
			yellow.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

			HSSFCellStyle green = wb.createCellStyle();
			green.setFillForegroundColor(new HSSFColor.GREEN().getIndex());
			green.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

			HSSFCellStyle blue = wb.createCellStyle();
			blue.setFillForegroundColor(new HSSFColor.BLUE().getIndex());
			blue.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

			HSSFCellStyle black = wb.createCellStyle();

			cmap.put("rosso", red);
			cmap.put("giallo", yellow);
			cmap.put("verde", green);
			cmap.put("blu", blue);
			cmap.put("nero", black);

			HSSFSheet interventi = wb.createSheet("Interventi");
			HSSFRow row0 = interventi.createRow(0);
			row0.createCell(0).setCellValue(new HSSFRichTextString("Intervento"));
			row0.createCell(1).setCellValue(new HSSFRichTextString("Stato"));
			row0.createCell(2).setCellValue(new HSSFRichTextString("Scadenza"));
			row0.createCell(3).setCellValue(new HSSFRichTextString("Squadra"));
			row0.createCell(4).setCellValue(new HSSFRichTextString("Segnalazione"));
			int i = 0;
			for (Intervento intervento : this.listaSegnalazioni.getInterventi()) {
				i++;
				HSSFRow row = interventi.createRow(i);
				row.createCell(0).setCellValue(new HSSFRichTextString(intervento.getOggetto()));
				row.createCell(1).setCellValue(new HSSFRichTextString(intervento.getStato().toString()));
				HSSFCell cell = row.createCell(2);
				if (intervento.getCodiceTriage().getId().equals("nero")) {
					cell.setCellValue(new HSSFRichTextString(this.xsdf.format(intervento.getScadenza())));

				} else {
					cell.setCellValue(new HSSFRichTextString(this.sdf.format(intervento.getScadenza())));
				}
				cell.setCellStyle(cmap.get(intervento.getCodiceTriage().getId()));

				row.createCell(3).setCellValue(new HSSFRichTextString(intervento.getSquadraIntervento().toString()));

				if (intervento.getSegnalazione() != null) {
					row.createCell(4).setCellValue(new HSSFRichTextString(intervento.getSegnalazione().getOggetto()));
				}

			}
		}

		if (this.taskInstanceList.size() > 0) {
			HSSFSheet sil = wb.createSheet("Segnalazioni in lavorazione");
			HSSFRow row0 = sil.createRow(0);
			row0.createCell(0).setCellValue(new HSSFRichTextString("Segnalazione"));
			row0.createCell(1).setCellValue(new HSSFRichTextString("Stato"));
			int i = 0;
			for (TaskInstance task : this.taskInstanceList) {
				i++;
				HSSFRow row = sil.createRow(i);
				row.createCell(0).setCellValue(new HSSFRichTextString(this.alertingController.getTitleForTask(task)));
				row.createCell(1).setCellValue(new HSSFRichTextString(this.alertingController.getSegnalazioneFromJBPMTask(task).getStato().toString()));
			}
		}

		if (this.listaSegnalazioni.getMieSegnalazioni().size() > 0) {
			HSSFSheet lms = wb.createSheet("Le mie Segnalazioni");
			HSSFRow row0 = lms.createRow(0);
			row0.createCell(0).setCellValue(new HSSFRichTextString("Segnalazione"));
			row0.createCell(1).setCellValue(new HSSFRichTextString("Stato"));
			row0.createCell(2).setCellValue(new HSSFRichTextString("Data"));
			int i = 0;
			for (Segnalazione s : this.listaSegnalazioni.getMieSegnalazioni()) {
				i++;
				HSSFRow row = lms.createRow(i);
				row.createCell(0).setCellValue(new HSSFRichTextString(s.getOggetto()));
				row.createCell(1).setCellValue(new HSSFRichTextString(s.getStato().toString()));
				row.createCell(2).setCellValue(new HSSFRichTextString(this.sdf.format(s.getData())));
			}
		}

		return wb;

	}

	public void homeExcel() throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		baos.write(this.generaHome().getBytes());
		this.downloadExcel(baos, "home");
	}
}
