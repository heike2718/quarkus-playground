// =====================================================
// Project: quarkus-hello
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.sending_email_quickstart.util;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * LoggerProducer
 */
@Singleton
public class LoggerProducer {

	@Produces
	public Logger produceLogger(final InjectionPoint injectionPoint) {

		return LoggerFactory.getLogger(injectionPoint.getMember().getDeclaringClass().getName());
	}

}
