package de.egladil.web.quarkus_extconfig;

import java.util.Optional;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import de.egladil.web.quarkus_extconfig.config.ChangeablePropertiesSource;

@RequestScoped
@Path("/greeting")
public class GreetingResource {

	@ConfigProperty(name = "greeting.message")
	String message;

	@ConfigProperty(name = "greeting.suffix", defaultValue = "!")
	String suffix;

	@ConfigProperty(name = "greeting.name")
	Optional<String> name;

	@Inject
	ChangeablePropertiesSource changeablePropertiesSource;

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String hello() {

		if (changeablePropertiesSource != null) {

			return message + " " + name.orElse("world") + suffix + " This property will eventually change '"
				+ changeablePropertiesSource.getValue("changing")
				+ "'";
		}

		return message + " " + name.orElse("world") + suffix + " Could not load the changeable property";
	}

}
