grails.servlet.version = "3.0"  // Change depending on target container compliance (2.5 or 3.0)
grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir	= "target/test-reports"
grails.project.target.level = 1.6
grails.project.source.level = 1.6
//grails.project.war.file = "target/${appName}-${appVersion}.war"
grails.server.port.http = 8080
//grails.war.resources = { stagingDir ->
//	delete(file:"${stagingDir}/WEB-INF/lib/slf4j-api-1.5.2.jar")
//	copy(file: "grails-app/conf/server.properties", tofile: "${stagingDir}/WEB-INF/server.properties")
//	copy(file: "grails-app/conf/server.properties", tofile: "${stagingDir}/WEB-INF/classes/server.properties")
//	copy(file: "grails-app/conf/server.properties", tofile: "${stagingDir}/WEB-INF/lib/server.properties")
//}

// grails.tomcat.nio = true

grails.tomcat.jvmArgs = ["-server", "-XX:MaxPermSize=512m", "-XX:MaxNewSize=256m", "-XX:NewSize=256m",
	"-Xms768m", "-Xmx1024m", "-XX:SurvivorRatio=128", "-XX:MaxTenuringThreshold=0",
   "-XX:+UseTLAB", "-XX:+UseConcMarkSweepGC", "-XX:+CMSClassUnloadingEnabled",
   "-XX:+CMSIncrementalMode", "-XX:-UseGCOverheadLimit", "-XX:+ExplicitGCInvokesConcurrent"]

grails.project.dependency.resolution = {

	// pom true
    // inherit Grails' default dependencies
    inherits( "global" ) {
        // uncomment to disable ehcache
        // excludes 'ehcache'
    }
    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
	checksums true // Whether to verify checksums on resolve

    repositories {
		inherits true

        grailsPlugins()
        grailsHome()
        grailsCentral()

		mavenLocal()
		mavenCentral()
        // uncomment the below to enable remote dependency resolution
		// from public Maven repositories
		
//		String mavenSettings = "${System.getProperty('maven.home')}/conf/settings.xml"
//		def settings = new XmlSlurper().parseText(new File(mavenSettings).text)
//		String local = "${settings.localRepository}"
//      mavenLocal(local)
//      mavenCentral()
        //mavenRepo "http://snapshots.repository.codehaus.org"
        //mavenRepo "http://repository.codehaus.org"
        //mavenRepo "http://download.java.net/maven/2/"
        //mavenRepo "http://repository.jboss.com/maven2/"
    }

    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.
        // runtime 'mysql:mysql-connector-java:5.1.5'
    }

	plugins {
		runtime ":hibernate:$grailsVersion"
		runtime ":resources:1.1.6"
		build ":tomcat:$grailsVersion"
	}
}