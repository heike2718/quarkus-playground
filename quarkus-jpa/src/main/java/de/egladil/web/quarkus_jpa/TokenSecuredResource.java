//=====================================================
// Project: quarkus-jpa
// (c) Heike Winkelvoß
//=====================================================
package de.egladil.web.quarkus_jpa;

import java.security.Principal;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.jwt.JsonWebToken;

/**
 * TokenSecuredResource
 */
@Path("/secured")
@RequestScoped
public class TokenSecuredResource {

	@Inject
	JsonWebToken jwt;

	@GET
	@Path("permit-all")
	@PermitAll
	@Produces(MediaType.TEXT_PLAIN)
	public String hello(@Context
	final SecurityContext ctx) {
		Principal caller = ctx.getUserPrincipal();
		String name = caller == null ? "anonymous" : caller.getName();
		boolean hasJWT = jwt != null && jwt.getName() != null;

		String helloReply = String.format("hello + %s, isSecure: %s, authScheme: %s, hasJWT: %s", name, ctx.isSecure(),
			ctx.getAuthenticationScheme(), hasJWT);
		return helloReply;
	}

	@GET
	@Path("roles-allowed")
	@RolesAllowed({ "ECHOER", "SUBSCRIBER" })
	@Produces(MediaType.TEXT_PLAIN)
	public String helloRolesAllowed(@Context
	final SecurityContext ctx) {

		Principal caller = ctx.getUserPrincipal();
		String name = caller == null ? "anonymous" : caller.getName();

		boolean hasJWT = jwt != null && jwt.getName() != null;

		String fullName = "noname";
		if (jwt.containsClaim(Claims.full_name.name())) {
			fullName = jwt.getClaim(Claims.full_name.name());
		} else {
			System.err.println("schade");
		}

		String helloReply = String.format("hello + %s, isSecure: %s, authScheme: %s, hasJWT: %s, sub: %s", fullName, ctx.isSecure(),
			ctx.getAuthenticationScheme(), hasJWT, name);

		return helloReply;
	}
}
