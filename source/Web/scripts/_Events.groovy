//import grails.util.GrailsUtil
//import org.apache.catalina.connector.Connector
//import org.apache.coyote.http11.Http11AprProtocol
//
//eventConfigureTomcat = {tomcat ->
//	def connector = new Connector("org.apache.coyote.http11.Http11NioProtocol")
//	connector.port = System.getProperty("server.port", "8080").toInteger()
//	connector.redirectPort = "8443"
//	connector.protocol = "HTTP/1.1"
//	connector.connectionTimeout = "20000"
//	connector.compression = "on"
//	connector.setProperty("compression", "on")
//	connector.compressionMinSize = "2048"
//	connector.setProperty("compressionMinSize", "2048")
//	connector.noCompressionUserAgents = "gozilla, traviata"
//	connector.setProperty("noCompressionUserAgents", "gozilla, traviata")
//	connector.compressableMimeType = "text/html,text/xml,text/plain,text/css,text/javascript,text/json,application/x-javascript,application/javascript,application/json,image/svg+xml"
//	connector.setProperty("compressableMimeType", "text/html,text/xml,text/plain,text/css,text/javascript,text/json,application/x-javascript,application/javascript,application/json,image/svg+xml")
//
//	tomcat.connector = connector
//	tomcat.service.addConnector connector
//}