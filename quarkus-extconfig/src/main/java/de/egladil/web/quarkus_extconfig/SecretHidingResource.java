// =====================================================
// Project: quarkus-extconfig
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.quarkus_extconfig;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;

import de.egladil.web.quarkus_extconfig.config.SecretPropertiesSource;

/**
 * SecretHidingResource
 */
@RequestScoped
@Path("/secret")
public class SecretHidingResource {

	@Inject
	Logger log;

	@Inject
	SecretPropertiesSource secretPropertiesSource;

	/**
	 *
	 */
	public SecretHidingResource() {

		if (log != null) {

			log.debug("=== (SHR-1) ===");
		} else {

			System.out.println("=== (SHR-1) ===");
		}

	}

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String hello() {

		if (secretPropertiesSource != null) {

			log.debug("=== (SHR-2) ===");

			return " The secret is '" + secretPropertiesSource.getValue("secret") + "'";
		}

		log.debug("=== (SHR-3) ===");

		return "Could not load the secret :/";
	}

}
