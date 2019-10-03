//=====================================================
// Project: quarkus-jpa
// (c) Heike Winkelvoß
//=====================================================
package de.egladil.web.quarkus_jpa.error;

import javax.inject.Inject;
import javax.ws.rs.core.NoContentException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;

import de.egladil.web.commons.error.AuthException;
import de.egladil.web.commons.payload.MessagePayload;
import de.egladil.web.commons.payload.ResponsePayload;

/**
 * CustomExceptionMapper
 */
@Provider
public class CustomExceptionMapper implements ExceptionMapper<Exception> {

	@Inject
	Logger log;

	private String generalError = "Es ist ein Serverfehler aufgetreten. Bitte senden Sie eine Mail an info@egladil.de";

	@Override
	public Response toResponse(final Exception exception) {

		if (exception instanceof NoContentException) {
			return Response.status(204).build();
		}

		if (exception instanceof AuthException) {
			ResponsePayload payload = ResponsePayload.messageOnly(MessagePayload.error(exception.getMessage()));
			log.warn(exception.getMessage());
			return Response.status(401)
				.entity(payload)
				.build();
		}

		log.error(exception.getMessage(), exception);

		ResponsePayload payload = ResponsePayload.messageOnly(MessagePayload.error(generalError));
		return Response.serverError()
			.header("X-Auth-Error", "Serverfehler")
			.entity(payload)
			.build();
	}

}
