// =====================================================
// Project: sending-email-quickstart
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.sending_email_quickstart;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.MockMailbox;
import io.quarkus.test.junit.QuarkusTest;

/**
 * MailTest
 */
@QuarkusTest
public class MailTest {

	private static final String TO = "hdwinkel@egladil.de";

	@Inject
	MockMailbox mailbox;

	@BeforeEach
	void init() {

		mailbox.clear();
	}

	@Test
	void testTextMail() throws IOException {

		// call a REST endpoint that sends email
		given()
			.when()
			.get("/mail/simple")
			.then()
			.statusCode(202)
			.body(is("OK"));

		// verify that it was sent
		List<Mail> sent = mailbox.getMessagesSentTo(TO);
		assertNotNull(sent);
		assertEquals(1, sent.size());

		Mail actual = sent.get(0);

		assertTrue(actual.getText().contains("Wake up!"));

		assertEquals("A simple email from quarkus-hello", actual.getSubject());
		assertEquals(1, mailbox.getTotalMessagesSent());
	}

}
