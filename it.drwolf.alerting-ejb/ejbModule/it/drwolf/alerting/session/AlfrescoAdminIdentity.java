package it.drwolf.alerting.session;

import org.apache.chemistry.opencmis.client.api.ObjectId;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

@AutoCreate
@Name("alfrescoAdminIdentity")
@Scope(ScopeType.APPLICATION)
public class AlfrescoAdminIdentity extends AlfrescoIdentity {

	private ObjectId fotoFolderRef;

	public ObjectId getFotoFolderRef() {
		return this.fotoFolderRef;
	}

	public void setFotoFolderRef(ObjectId fotoFolderRef) {
		this.fotoFolderRef = fotoFolderRef;
	}

}
