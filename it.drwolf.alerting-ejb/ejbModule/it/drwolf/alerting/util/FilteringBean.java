package it.drwolf.alerting.util;

import java.util.HashMap;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

@Scope(ScopeType.CONVERSATION)
@Name("filteringBean")
public class FilteringBean {

	public static class Filter {
		private String value;

		public Filter() {

		}

		// filterMethod="#{filteringBean.filterFor('tilist.task.oggetto').filter(alertingController.getSegnalazione(task).oggetto)}"
		public boolean filter(Object current) {
			if (this.value == null || "".equals(this.value.trim())) {
				return true;
			}
			try {
				return current == null ? false : current.toString()
						.toLowerCase().contains(this.value.toLowerCase());
			} catch (Exception e) {
				return false;
			}
		}

		public String getValue() {
			return this.value;
		}

		public void setValue(String value) {
			this.value = value;
		}

	}

	private HashMap<String, Filter> filterValues = new HashMap<String, Filter>();

	public Filter filterFor(String key) {
		if (!this.filterValues.containsKey(key)) {
			this.filterValues.put(key, new Filter());
		}

		return this.filterValues.get(key);
	}

}