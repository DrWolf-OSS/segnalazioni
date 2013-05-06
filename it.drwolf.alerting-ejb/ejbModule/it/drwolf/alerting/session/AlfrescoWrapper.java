package it.drwolf.alerting.session;

import it.drwolf.alerting.entity.Segnalazione;
import it.drwolf.alerting.util.CmisUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.ObjectId;
import org.apache.chemistry.opencmis.client.api.OperationContext;
import org.apache.chemistry.opencmis.client.api.Rendition;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.runtime.ObjectIdImpl;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.commons.io.IOUtils;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.security.Credentials;

@Name("alfrescoWrapper")
@Scope(ScopeType.CONVERSATION)
public class AlfrescoWrapper {

	public static ObjectId ref2id(String ref) {
		if (("" + ref).length() < 10) {
			System.out.println("null ref");
		}
		return ref.startsWith(AlfrescoWrapper.NODEREF_PREFIX) ? new ObjectIdImpl(ref) : new ObjectIdImpl(AlfrescoWrapper.NODEREF_PREFIX + ref);
	}

	@In
	private Credentials credentials;

	@In
	private CmisUtils cmisUtils;

	public static final String NODEREF_PREFIX = "workspace://SpacesStore/";

	@In
	private AlfrescoAdminIdentity alfrescoAdminIdentity;

	@In
	private EntityManager entityManager;

	@In(value = "#{facesContext.externalContext}", required = false)
	private ExternalContext extCtx;

	private boolean owned = true;

	private boolean initialized = false;

	public void addAttachment(Segnalazione segnalazione, Document node) {
		if ((node == null) || (node.getId() == null)) {
			return;
		}
		if (segnalazione.getAllegati().contains(node.getId())) {
			return;
		}
		segnalazione.getAllegati().add(node.getId());
	}

	public void download() {

		FacesContext context = FacesContext.getCurrentInstance();
		Map<String, String> params = context.getExternalContext().getRequestParameterMap();
		String id = params.get("docId");
		this.download(id);
	}

	public void download(String id) {
		HttpServletResponse response = (HttpServletResponse) this.extCtx.getResponse();

		Document d = (Document) this.alfrescoAdminIdentity.getSession().getObject(AlfrescoWrapper.ref2id(id));

		ContentStream cs = d.getContentStream();

		response.setContentType(cs.getMimeType());

		response.addHeader("Content-disposition", "attachment; filename=\"" + d.getProperty(PropertyIds.NAME).getValueAsString() + "\"");
		response.setHeader("Expires", "0");
		response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
		try {
			response.setContentLength((int) cs.getLength());
			ServletOutputStream os = response.getOutputStream();

			byte[] buffer = new byte[8192];
			int len;
			while ((len = cs.getStream().read(buffer)) != -1) {
				os.write(buffer, 0, len);
			}
			os.flush();
			os.close();
			FacesContext.getCurrentInstance().responseComplete();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public CmisObject getCmisObject(String ref) {

		ObjectId o = AlfrescoWrapper.ref2id(ref);
		Session adminSession = this.alfrescoAdminIdentity.getSession();
		return adminSession.getObject(o);
	}

	public String getNodeName(String ref) {
		if (("" + ref).length() < 10) {
			return "NO-ID";
		}
		return this.alfrescoAdminIdentity.getSession().getObject(AlfrescoWrapper.ref2id(ref)).getProperty(PropertyIds.NAME).getValueAsString();

	}

	public synchronized Object getThumbnail(String id, String name) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		if (id == null || id.length() > 20) {
			try {
				Session session = this.cmisUtils.getSession();
				OperationContext context = session.createOperationContext();

				context.setRenditionFilterString("*");

				Document doc = (Document) session.getObject(AlfrescoWrapper.ref2id(id), context);

				for (Rendition r : doc.getRenditions()) {
					if (r != null && name.equals(r.getTitle())) {
						byte[] buffer = new byte[4096];
						InputStream is = r.getContentStream().getStream();
						int read;
						while ((read = is.read(buffer)) != -1) {
							baos.write(buffer, 0, read);
						}
						is.close();

						return baos.toByteArray();

					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		try {
			if (baos.size() == 0) {
				return IOUtils.toByteArray(AlfrescoWrapper.class.getResourceAsStream("image-missing.png"));
			}
			return baos.toByteArray();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// public Folder getFotoFolder() {
	// ObjectId nodeRef = this.alfrescoAdminIdentity.getFotoFolderRef();
	// Session adminSession = this.alfrescoAdminIdentity.getSession();
	//
	// return (Folder) adminSession.getObjectByPath(((Folder)
	// adminSession.getObject(nodeRef)).getPath() + "/" +
	// this.credentials.getUsername());
	// }

	private void init(Segnalazione segnalazione) {
		this.initialized = true;
	}

	public boolean isOwned() {
		return this.owned;
	}

}
