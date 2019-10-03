//=====================================================
// Project: quarkus-jpa
// (c) Heike Winkelvoß
//=====================================================
package de.egladil.web.quarkus_jpa.filters;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.NoContentException;
import javax.ws.rs.ext.Provider;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;

import de.egladil.web.commons.error.AuthException;
import de.egladil.web.commons.utils.CommonHttpUtils;
import de.egladil.web.commons.utils.CommonStringUtils;

/**
 * OriginReferrerFilter
 */
@Provider
public class OriginReferrerFilter implements ContainerRequestFilter {

	@Inject
	Logger log;

	@Context
	private HttpServletRequest servletRequest;

	@ConfigProperty(name = "filters.origin-referrer-filter.blockOnMissingOriginReferer", defaultValue = "false")
	String blockOnMissingOriginReferer;

	@ConfigProperty(name = "filters.origin-referrer-filter.targetOrigins")
	String targetOrigins;

	private static final List<String> NO_CONTENT_PATHS = Arrays.asList(new String[] { "/favicon.ico" });

	@Override
	public void filter(final ContainerRequestContext requestContext) throws IOException {

		final String pathInfo = servletRequest.getPathInfo();
		if (NO_CONTENT_PATHS.contains(pathInfo) || "OPTIONS".equals(servletRequest.getMethod())) {
			throw new NoContentException(pathInfo);
		}

		validateOriginAndRefererHeader();
	}

	private void validateOriginAndRefererHeader() throws IOException {
		final String origin = servletRequest.getHeader("Origin");
		final String referer = servletRequest.getHeader("Referer");

		log.debug("Origin = [{}], Referer=[{}]", origin, referer);

		if (StringUtils.isBlank(origin) && StringUtils.isBlank(referer)) {
			final String details = "Header Origin UND Referer fehlen";
			if (Boolean.getBoolean(blockOnMissingOriginReferer)) {
				logErrorAndThrow(details);
			}
		}

		if (!StringUtils.isBlank(origin)) {
			checkHeaderTarget(origin);
		}
		if (!StringUtils.isBlank(referer)) {
			checkHeaderTarget(referer);
		}
	}

	private void checkHeaderTarget(final String headerValue) throws IOException {
		final String extractedValue = CommonStringUtils.extractOrigin(headerValue);
		if (extractedValue == null) {
			return;
		}

		if (targetOrigins != null && !"null".equals(targetOrigins)) {
			List<String> allowedOrigins = Arrays.asList(targetOrigins.split(","));

			if (!allowedOrigins.contains(extractedValue)) {
				final String details = "targetOrigin != extractedOrigin: [targetOrigins=" + targetOrigins
					+ ", extractedOriginOrReferer="
					+ extractedValue + "]";
				logErrorAndThrow(details);
			}
		}
	}

	private void logErrorAndThrow(final String details) throws IOException {
		final String dump = CommonHttpUtils.getRequesInfos(servletRequest);
		log.warn("Possible CSRF-Attack: {} {}", details, dump);
		throw new AuthException("different origin and referrer header");
	}

}
