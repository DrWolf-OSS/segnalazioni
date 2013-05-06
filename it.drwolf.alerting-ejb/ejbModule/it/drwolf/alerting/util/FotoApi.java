package it.drwolf.alerting.util;

import it.drwolf.alerting.entity.AppParam;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.alfresco.cmis.client.AlfrescoDocument;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

@Path("/foto")
public class FotoApi {
	private String getFileName(MultivaluedMap<String, String> header) {

		String[] contentDisposition = header.getFirst("Content-Disposition").split(";");

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
		String mime = "";

		Map<String, List<InputPart>> uploadForm = input.getFormDataMap();

		for (List<InputPart> inputParts : uploadForm.values()) {

			for (InputPart inputPart : inputParts) {

				try {

					MultivaluedMap<String, String> header = inputPart.getHeaders();
					fileName = this.getFileName(header);

					// convert the uploaded file to inputstream
					InputStream inputStream = inputPart.getBody(InputStream.class, null);

					CmisUtils cmisUtils = new CmisUtils();
					cmisUtils.login();

					Map<String, Object> props = new HashMap<String, Object>();
					props.put(PropertyIds.NAME, fileName);
					props.put(PropertyIds.OBJECT_TYPE_ID, "cmis:document");

					if (fileName.toLowerCase().endsWith("jpg")) {
						mime = "image/jpeg";

					} else if (fileName.toLowerCase().endsWith("csv")) {
						mime = "text/plain";
					}

					Folder base = (Folder) cmisUtils.getSession().getObjectByPath(AppParam.ALFRESCO_CALEEARTH_PATH.getValue());
					AlfrescoDocument doc = (AlfrescoDocument) cmisUtils.createDocument(base, props);
					props = new HashMap<String, Object>();

					{
						Session s = cmisUtils.getSession();
						ContentStream csi = s.getObjectFactory().createContentStream(fileName, 1000000000, mime, inputStream);
						doc.setContentStream(csi, true, true);
					}

					return Response.status(200).entity("upload ok: " + fileName + ", " + inputStream.available() + " bytes").build();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		}
		return Response.status(500).entity("upload failed").build();
	}

}
