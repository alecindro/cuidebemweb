package br.com.cuidebem.rs;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import br.com.cuidebem.exceptions.DaoException;
import br.com.cuidebem.exceptions.PreexistingEntityException;
import br.com.cuidebem.service.UsersDao;

@Path("/desbloqueio/{email}")
public class Desbloqueio {

	@EJB
	private UsersDao usersDao;

	@GET
	@Produces("text/html")
	public String aceite(@PathParam("email") String email) {
		try {
			usersDao.confirmaAceite(email);
			return PagesHTML.return_desbloqueio;
		} catch (DaoException e) {
			return PagesHTML.return_oops_desbloqueio.replace(PagesHTML.message, e.getMessage());
		} catch (PreexistingEntityException e1) {
			return PagesHTML.return_oops_desbloqueio.replace(PagesHTML.message, e1.getMessage());
		} catch (Exception e2) {
			return PagesHTML.return_oops_desbloqueio.replace(PagesHTML.message,
					PagesHTML.oops_desbloqueio + "<br/> Exceção: " + e2.getMessage());

		}

	}

}
