package it.drwolf.alerting.util.dialects;

import java.sql.Types;

import org.hibernate.Hibernate;
import org.hibernate.dialect.MySQLInnoDBDialect;

public class HiberanteMySqlDialect extends MySQLInnoDBDialect  {
    public HiberanteMySqlDialect() {
    	super();
    	registerColumnType(Types.LONGVARCHAR, Hibernate.STRING.getName());
    	registerHibernateType( Types.LONGVARCHAR, Hibernate.STRING.getName() );
    	registerHibernateType( Types.LONGVARBINARY, Hibernate.BLOB.getName() );
    }




}

