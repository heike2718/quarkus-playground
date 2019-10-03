// =====================================================
// Project: quarkus-extconfig
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.quarkus_extconfig.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.config.spi.ConfigSource;
import org.slf4j.Logger;

/**
 * SecretPropertiesSource is connected with a properties file in the file system.<br>
 * <br>
 * The location of this file must be configured by setting the property 'secret.config.path'. This property is resolved
 * by the MP configuration mechanism.<br>
 * <br>
 * The purpose of redirecting to an external file is to avoid secret properties being checked into version control.
 */
@ApplicationScoped
public class SecretPropertiesSource implements ConfigSource {

	@Inject
	Logger log;

	@ConfigProperty(name = "secret.config.path")
	String pathSecretProperties;

	private Map<String, String> properties = new HashMap<>();

	/**
	 *
	 */
	public SecretPropertiesSource() {

		if (log != null) {

			log.debug("=== (SPS-1) ===");
		} else {

			System.out.println("=== (SPS-1) ===");
		}

	}

	@Override
	public Map<String, String> getProperties() {

		if (properties.isEmpty()) {

			log.debug("=== (SPS-5) ===");

			loadProperties();
		}

		return properties;
	}

	@Override
	public String getValue(final String propertyName) {

		log.debug("=== (SPS-2): propertyName={} ===", propertyName);

		if (properties.isEmpty()) {

			log.debug("=== (SPS-3) ===");

			loadProperties();
		}

		String val = properties.get(propertyName);

		if (val == null) {

			throw new RuntimeException("property '" + propertyName + "' is missing in " + pathSecretProperties);
		}
		return val;
	}

	@Override
	public String getName() {

		return getClass().getSimpleName();
	}

	private void loadProperties() {

		final Properties props = new Properties();

		try (InputStream in = new FileInputStream(new File(pathSecretProperties))) {

			props.load(in);

			props.keySet().stream().forEach(k -> {

				String key = (String) k;
				String value = props.getProperty((String) key);
				properties.put((String) key, value);
			});

			log.debug("=== (SPS-4) ===");

		} catch (IOException e) {

			log.error("error reading external configuration: {}", e.getMessage(), e);

		}
	}

	@Override
	public int getOrdinal() {

		return 90;
	}

}
