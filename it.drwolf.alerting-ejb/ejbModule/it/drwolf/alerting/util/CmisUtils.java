package it.drwolf.alerting.util;

import it.drwolf.alerting.entity.AppParam;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.alfresco.cmis.client.AlfrescoFolder;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.ObjectId;
import org.apache.chemistry.opencmis.client.api.OperationContext;
import org.apache.chemistry.opencmis.client.api.Rendition;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.SessionFactory;
import org.apache.chemistry.opencmis.client.runtime.ObjectIdImpl;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.enums.BaseTypeId;
import org.apache.chemistry.opencmis.commons.enums.BindingType;
import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;
import org.apache.commons.io.IOUtils;

public class CmisUtils {

	private Session session;

	public static final String NODEREF_PREFIX = "workspace://SpacesStore/";

	public static ObjectId ref2id(String ref) {
		if (("" + ref).length() < 10) {
			System.out.println("null ref");
		}
		return ref.startsWith(CmisUtils.NODEREF_PREFIX) ? new ObjectIdImpl(ref)
				: new ObjectIdImpl(CmisUtils.NODEREF_PREFIX + ref);
	}

	public Document createDocument(Folder parent, Map<String, Object> map) {
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(PropertyIds.OBJECT_TYPE_ID,
				"cmis:document,P:dw:caleearth");
		for (Entry<String, Object> e : map.entrySet()) {
			properties.put(e.getKey(), e.getValue());
		}

		return parent.createDocument(properties, null, null);
	}

	public AlfrescoFolder findOrCreateFolder(Folder parent, String folderName) {

		AlfrescoFolder folder = null;

		try {

			folder = (AlfrescoFolder) this.session.getObjectByPath(parent
					.getPath() + "/" + folderName);

		} catch (CmisObjectNotFoundException e) {
			HashMap<String, Object> props = new HashMap<String, Object>();
			props.put(PropertyIds.NAME, folderName);
			props.put(PropertyIds.OBJECT_TYPE_ID,
					BaseTypeId.CMIS_FOLDER.value());
			folder = (AlfrescoFolder) parent.createFolder(props, null, null,
					null, this.session.createOperationContext());
		}

		return folder;

	}

	public AlfrescoFolder findOrCreateFolder(String path, String folderName) {
		AlfrescoFolder parent = (AlfrescoFolder) this.session
				.getObjectByPath(path);
		return this.findOrCreateFolder(parent, folderName);

	}

	public Session getSession() {
		return this.session;
	}

	public Object getThumbnail(String id, String name) {
		if (id == null || id.length() > 20) {
			try {
				OperationContext context = this.session
						.createOperationContext();
				context.setRenditionFilterString("*");
				Document doc = (Document) this.session.getObject(
						CmisUtils.ref2id(id), context);
				if (doc.getContentStream().getMimeType().contains("image")) {
					for (Rendition r : doc.getRenditions()) {
						if (name.equals(r.getTitle())) {
							return IOUtils.toByteArray(r.getContentStream()
									.getStream());
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return this.getClass().getResourceAsStream("image-missing.png");
	}

	public void login() {

		Map<String, String> parameter = new HashMap<String, String>();

		// Set the user credentials
		parameter.put(SessionParameter.USER,
				AppParam.ALFRESCO_USERNAME.getValue());
		parameter.put(SessionParameter.PASSWORD,
				AppParam.ALFRESCO_PASSWORD.getValue());

		// Specify the connection settings
		parameter.put(SessionParameter.ATOMPUB_URL,
				AppParam.ALFRESCO_URL.getValue() + "/service/cmis");
		parameter.put(SessionParameter.BINDING_TYPE,
				BindingType.ATOMPUB.value());

		// Set the alfresco object factory
		parameter.put(SessionParameter.OBJECT_FACTORY_CLASS,
				"org.alfresco.cmis.client.impl.AlfrescoObjectFactoryImpl");

		// Create a session
		SessionFactory factory = SessionFactoryImpl.newInstance();
		this.session = factory.getRepositories(parameter).get(0)
				.createSession();
	}
}
