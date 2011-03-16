package it.drwolf.alerting.session;

import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.bpm.CreateProcess;

@Name("processManager")
@AutoCreate
public class ProcessManager {

	@CreateProcess(definition = "segnalazione")
	public void createProcess() {

	}
}
