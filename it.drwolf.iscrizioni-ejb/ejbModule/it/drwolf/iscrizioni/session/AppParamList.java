package it.drwolf.iscrizioni.session;

import it.drwolf.iscrizioni.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("appParamList")
public class AppParamList extends EntityQuery<AppParam> {

	
	private static final long serialVersionUID = 8906362748073211848L;

	private static final String EJBQL = "select appParam from AppParam appParam";

	private static final String[] RESTRICTIONS = {
			"lower(appParam.id) like concat(lower(#{appParamList.appParam.id}),'%')",
			"lower(appParam.value) like concat(lower(#{appParamList.appParam.value}),'%')", };

	private AppParam appParam = new AppParam();

	public AppParamList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public AppParam getAppParam() {
		return appParam;
	}
}
