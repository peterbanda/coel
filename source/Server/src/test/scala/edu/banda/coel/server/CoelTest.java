package edu.banda.coel.server;

import org.springframework.test.context.ContextConfiguration;

import com.banda.core.test.Spring4Test;

/**
 * List of spring configuration files
 */
@ContextConfiguration(locations = {
		"classpath:test-config.xml", "classpath:server.xml", "classpath:persistence.xml",
		"classpath:grid.xml", "classpath:service.xml" })
public class CoelTest extends Spring4Test {
}