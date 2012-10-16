package it.drwolf.alerting.util;

import it.drwolf.alerting.entity.AppParam;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

@Path("/foto")
public class FotoApi {
	private String getFileName(MultivaluedMap<String, String> header) {

		String[] contentDisposition = header.getFirst("Content-Disposition")
				.split(";");

		for (String filename : contentDisposition) {
			if (filename.trim().startsWith("filename")) {

				String[] name = filename.split("=");

				String finalFileName = name[1].trim().replaceAll("\"", "");
				return finalFileName;
			}
		}

		return "unknown";
	}

	@SuppressWarnings("unchecked")
	@POST
	@Path("/upload")
	@Consumes("multipart/form-data")
	/*
	 * nomefileJPG; data; ora; Satelliti; latitudine; longitudine; altitudine;
	 * precisione; via; civico; deviceID; descrizione
	 * 
	 * 1349775764508.jpg;10/09/2012; 11:42:55; 0; 43.865421; 11.167655; 65; 1;
	 * via tal dei tali; 23; 351722053751586; prova upload
	 */
	public Response uploadFile(MultipartFormDataInput input) {

		String fileName = "";

		Map<String, List<InputPart>> uploadForm = input.getFormDataMap();

		for (List<InputPart> inputParts : uploadForm.values()) {

			for (InputPart inputPart : inputParts) {

				try {

					MultivaluedMap<String, String> header = inputPart
							.getHeaders();
					fileName = this.getFileName(header);

					// convert the uploaded file to inputstream
					InputStream inputStream = inputPart.getBody(
							InputStream.class, null);

					CmisUtils cmisUtils = new CmisUtils();
					cmisUtils.login();

					if (fileName.toLowerCase().endsWith("csv")) {
						List<String> lines = IOUtils
								.readLines(new InputStreamReader(inputStream));
						String[] h = lines.get(0).split(";");
						String[] b = lines.get(1).split(";");
						Map<String, String> map = new HashMap<String, String>();
						for (int i = 0; i < h.length; i++) {
							map.put(h[i], b[i]);
						}

						Map<String, Object> props = new HashMap<String, Object>();

						if (map.get("segnalazione") != null) {
							props.put("dw:segnalazione",
									Integer.parseInt(map.get("segnalazione")));
						}
						if (map.get("deviceID") != null) {
							props.put("dw:deviceId", map.get("deviceId"));
						}
						if (map.get("longitudine") != null) {
							props.put("dw:lon",
									Double.parseDouble(map.get("longitudine")));
						}
						if (map.get("latitudine") != null) {
							props.put("dw:lat",
									Double.parseDouble(map.get("latitudine")));
						}
						if (map.get("altitudine") != null) {
							props.put("dw:alt",
									Double.parseDouble(map.get("altitudine")));
						}

						SimpleDateFormat sdf = new SimpleDateFormat(
								"dd/MM/yyyy HH:mm:SS");

						if (map.get("data") != null && map.get("ora") != null) {
							Date date = sdf.parse(map.get("data") + " "
									+ map.get("ora"));
							Calendar c = Calendar.getInstance();
							c.setTime(date);
							props.put("dw:data", c);
						}

						props.put(PropertyIds.NAME, map.get("nomefileJPG"));

						Folder base = (Folder) cmisUtils.getSession()
								.getObjectByPath(
										AppParam.ALFRESCO_CALEEARTH_PATH
												.getValue());

						cmisUtils.createDocument(base, props);

					} else if (fileName.toLowerCase().endsWith("jpg")) {
						Session s = cmisUtils.getSession();
						String id = s
								.query("select cmis:objectId from cmis:document where IN_TREE('"
										+ s.getObjectByPath(
												AppParam.ALFRESCO_CALEEARTH_PATH
														.getValue()).getId()
										+ "') and cmis:name='" + fileName + "'",
										false).iterator().next()
								.getPropertyById("cmis:objectId")
								.getFirstValue().toString();
						Document doc = (Document) s.getObject(id);

						ContentStream csi = s.getObjectFactory()
								.createContentStream(fileName, 1000000000,
										"image/jpeg", inputStream);

						doc.setContentStream(csi, true, true);

					}

					return Response
							.status(200)
							.entity("upload ok: " + fileName + ", "
									+ inputStream.available() + " bytes")
							.build();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		}
		return Response.status(500).entity("upload failed").build();
	}

}
