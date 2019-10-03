// =====================================================
// Project: quarkus-extconfig
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.quarkus_extconfig.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.config.spi.ConfigSource;
import org.slf4j.Logger;

/**
 * ChangeablePropertiesSource is connected with a properties file in the file system that eventually changes.<br>
 * <br>
 * The location of this file must be configured by setting the property 'changeable.config.path'. This property is
 * resolved by the MP configuration mechanism. <br>
 * <br>
 * Change detection is turned on by default and can be turned off by setting the property changeable.config.scan in
 * application.properties to true. The properties will be reloaded if and only if the underlying file changed
 * (lastModified).
 */
@ApplicationScoped
public class ChangeablePropertiesSource implements ConfigSource {

	private LocalDateTime lastReadTime = LocalDateTime.now();

	@ConfigProperty(name = "changeable.config.path")
	String pathExternalConfigFile;

	@ConfigProperty(name = "changeable.config.scan", defaultValue = "true")
	String checkChanges;

	@Inject
	Logger log;

	private Map<String, String> properties = new HashMap<>();

	@Override
	public Map<String, String> getProperties() {

		checkAndLoadIfNecessary();

		return properties;
	}

	public void checkAndLoadIfNecessary() {

		boolean extFileChanged = externalPropertiesChanged();

		if (extFileChanged) {

			log.info("properties not initialized or changed and are going to be (re)loaded");
			loadExternalProperties();
		}
	}

	@Override
	public int getOrdinal() {

		return 90;
	}

	@Override
	public String getValue(final String propertyName) {

		checkAndLoadIfNecessary();

		String val = properties.get(propertyName);

		if (val == null) {

			throw new RuntimeException("property '" + propertyName + "' is missing in " + pathExternalConfigFile);
		}
		return val;
	}

	@Override
	public String getName() {

		return getClass().getSimpleName();
	}

	private boolean externalPropertiesChanged() {

		if (this.properties.isEmpty()) {

			return true;
		}

		if (!checkChanges()) {

			return false;
		}

		File file = new File(pathExternalConfigFile);

		if (file.isFile()) {

			long lastModified = file.lastModified();
			LocalDateTime modDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(lastModified), ZoneId.systemDefault());
			return modDateTime.isAfter(lastReadTime);
		}

		return false;
	}

	private boolean checkChanges() {

		return Boolean.parseBoolean(checkChanges);
	}

	private void loadExternalProperties() {

		Properties props = new Properties();

		try (InputStream in = new FileInputStream(new File(pathExternalConfigFile))) {

			props.load(in);

			props.keySet().stream().forEach(k -> {

				String key = (String) k;
				String value = props.getProperty((String) key);
				properties.put((String) key, value);
			});

		} catch (IOException e) {

			log.error("error reading external configuration: {}", e.getMessage(), e);

		}

		lastReadTime = LocalDateTime.now();

	}

}
