package br.com.cuidebem.rs;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import br.com.cuidebem.exceptions.DaoException;
import br.com.cuidebem.model.Convitecuidador;
import br.com.cuidebem.model.Users;
import br.com.cuidebem.model.UsersPaciente;
import br.com.cuidebem.service.ConvitecuidadorDAO;
import br.com.cuidebem.service.UsersDao;
import br.com.cuidebem.service.UsersPacienteDao;

@Path("/confirmaCuidador/{idconvite}/{email}")
public class AceiteCuidador {
	
	@EJB
	private UsersDao usersDao;
	@EJB
	private ConvitecuidadorDAO conviteCuidadorDAO;
	@EJB
	private UsersPacienteDao usersPacienteDao;
	
	@GET
    @Produces("text/html")
    public String aceite(@PathParam("idconvite") int idconvite, @PathParam("email") String email) {
		try {
			
			Users user = usersDao.find(email);
			if (user == null){
				return PagesHTML.return_cadastro_cuidador.replace(PagesHTML.email, email).replace(PagesHTML.idconvite, String.valueOf(idconvite));
			}
			Convitecuidador conviteCuidador = conviteCuidadorDAO.find(idconvite);
			if(conviteCuidador == null){
				return PagesHTML.return_oops.replace(PagesHTML.message, "Tivemos um problema ao processar a mensagem. Por favor, desconsidere o convite");
			}
			UsersPaciente usersPaciente = new UsersPaciente();
			usersPaciente.setEnabled(true);
			usersPaciente.setIdpaciente(conviteCuidador.getIdpaciente());
			usersPaciente.setPrincipal(false);
			usersPaciente.setUsers(user);
			usersPacienteDao.create(usersPaciente);
			conviteCuidador.setPendente(false);
			conviteCuidadorDAO.edit(conviteCuidador);
			return PagesHTML.return_confirma_cuidador;
		} catch (DaoException e) {
			return PagesHTML.return_oops.replace(PagesHTML.message, e.getMessage());
		}  catch(Exception e2){
			return PagesHTML.return_oops.replace(PagesHTML.message, PagesHTML.oops + "<br/> Exceção: "+e2.getMessage());

		}
        
    }


}
