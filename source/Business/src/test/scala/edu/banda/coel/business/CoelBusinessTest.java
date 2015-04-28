package edu.banda.coel.business;

import org.springframework.test.context.ContextConfiguration;

import com.banda.core.test.Spring4Test;

/**
 * @author © Peter Banda
 * @since 2014
 */
@ContextConfiguration(locations = {"classpath:math-conf.xml"})
public abstract class CoelBusinessTest extends Spring4Test {

}
