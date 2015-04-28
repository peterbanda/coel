package edu.banda.coel.server;

import org.apache.commons.lang.StringUtils;
import org.hibernate.cfg.DefaultNamingStrategy;

public class NamingStrategy extends DefaultNamingStrategy {

	@Override
	public String tableName(String tableName) {
		return StringUtils.lowerCase(tableName);
	}

	@Override
	public String columnName(String columnName) {
		return StringUtils.lowerCase(columnName);
	}
}
