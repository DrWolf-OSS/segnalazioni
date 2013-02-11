package it.drwolf.alerting;

import it.drwolf.alerting.session.AlfrescoIdentity;
import it.drwolf.alerting.session.AlfrescoWrapper;

import java.io.BufferedInputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.chemistry.opencmis.client.api.Document;
import org.jboss.seam.Component;
import org.jboss.seam.contexts.Context;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.contexts.Lifecycle;

public class DownloadServlet extends HttpServlet {

	private static final long serialVersionUID = -2774298036591076575L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("Chiamata alla servlet");
		Lifecycle.beginCall();
		AlfrescoWrapper aw = (AlfrescoWrapper) Component.getInstance("alfrescoWrapper");

		Context sessionContext = Contexts.getSessionContext();
		AlfrescoIdentity alfrescoAdminIdentity = (AlfrescoIdentity) sessionContext.get("alfrescoAdminIdentity");
		Document data = (Document) alfrescoAdminIdentity.getSession().getObject(AlfrescoWrapper.ref2id(req.getParameter("ref")));

		resp.setContentType(data.getContentStreamMimeType());

		BufferedInputStream bf = null;
		ServletOutputStream outStream = null;
		try {

			bf = new BufferedInputStream(data.getContentStream().getStream());
			outStream = resp.getOutputStream();

			byte[] buffer = new byte[4096];
			int b;
			while ((b = bf.read(buffer)) > -1) {
				outStream.write(buffer, 0, b);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			if (bf != null) {
				try {
					bf.close();
				} catch (IOException e) {
				}
			}
			if (outStream != null) {
				try {
					outStream.close();
				} catch (IOException e) {
				}
			}
		}

		Lifecycle.endCall();
	}
}
