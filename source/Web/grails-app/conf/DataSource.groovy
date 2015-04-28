def host = System.getenv("COEL_DBHOST")
def port = System.getenv("COEL_DBPORT")
def db = System.getenv("COEL_DB")

dataSource {
	pooled = false
	driverClassName = "org.postgresql.Driver"
	dialect = "org.hibernate.dialect.PostgreSQLDialect"
	username = System.getenv("COEL_DBUSER")
	password = System.getenv("COEL_DBPASSWORD")
	url = "jdbc:postgresql://${host}:${port}/${db}"
}

hibernate {
	// Manual flushing for hibernate
//    cache.use_second_level_cache=false
//    cache.use_query_cache=true
    cache.provider_class='org.hibernate.cache.EhCacheProvider'	
    config.location = "classpath:hibernate.cfg.xml"
}
// environment specific settings
environments {
	development {
		dataSource {
			dbCreate = "validate"
		}
	}
	test {
		dataSource {
			dbCreate = "validate"
		}
	}
	production {
		dataSource {
			dbCreate = "validate"
		}
	}
}