//=====================================================
// Project: quarkus-jpa
// (c) Heike Winkelvoß
//=====================================================
package de.egladil.web.quarkus_jpa;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import de.egladil.web.quarkus_jpa.domain.Pacemaker;

/**
 * PacemakerResource
 */
@Path("heartbeats")
@ApplicationScoped
@Produces("application/json")
@Consumes("application/json")
public class PacemakerResource {

	@Inject
	EntityManager entityManager;

	@GET
	public Pacemaker[] get() {
		return entityManager.createQuery("select p from Pacemaker p", Pacemaker.class).getResultList().toArray(new Pacemaker[0]);
	}

}
