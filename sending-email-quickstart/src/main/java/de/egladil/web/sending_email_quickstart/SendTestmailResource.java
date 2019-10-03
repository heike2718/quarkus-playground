// =====================================================
// Project: quarkus-hello
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.sending_email_quickstart;

import javax.annotation.security.PermitAll;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import io.vertx.ext.mail.MailClient;

/**
 * SendTestmailResource
 */
@ApplicationScoped
@Path("/mail")
@PermitAll
public class SendTestmailResource {

	@Inject
	Logger log;

	@Inject
	Mailer mailer;

	@Inject
	MailClient client;

	// @Inject
	// ReactiveMailer reactiveMailer;

	@GET
	@Path("/simple")
	public Response sendASimpleEmail() {

		try {

			mailer.send(Mail.withText("hdwinkel@egladil.de", "A simple email from quarkus-hello", "Wake up! This is my body"));
			log.info("Mail sent successfully");
			return Response.accepted().entity("OK").build();
		} catch (Throwable e) {

			log.error("mail could not be sent: {}", e.getMessage(), e);
			return Response.serverError().entity(e.getMessage()).build();
		}
	}
}
