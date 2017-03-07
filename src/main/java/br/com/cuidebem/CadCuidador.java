package br.com.cuidebem;

import java.util.Date;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import br.com.cuidebem.exceptions.DaoException;
import br.com.cuidebem.model.Convitecuidador;
import br.com.cuidebem.model.TypeUser;
import br.com.cuidebem.model.Users;
import br.com.cuidebem.model.UsersPaciente;
import br.com.cuidebem.service.ConvitecuidadorDAO;
import br.com.cuidebem.service.UsersDao;
import br.com.cuidebem.service.UsersPacienteDao;
import br.com.security.quali.password.UtilPassword;

@ManagedBean(name = "cadcuidador")
public class CadCuidador {

	private int idconvite;
	private String emailCuidador;
	private String confirmeSenha;

	@Inject
	private Users user;

	@EJB
	private UsersDao usersDao;

	@EJB
	private ConvitecuidadorDAO ccDAO;

	@EJB
	private UsersPacienteDao upDAO;

	@PostConstruct
	private void init() {
		Map<String,String> parameters= FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		emailCuidador = parameters.get("email");
		user.setEmail(parameters.get("email"));
		String _idconvite = parameters.get("idconvite");
		if(_idconvite!= null){
		idconvite = Integer.valueOf(_idconvite);
		}
		user.setTypeuser(TypeUser.CUIDADOR.getOrder());
	}

	public String cadastrar() {
		if (!user.getPassword().equals(confirmeSenha)) {
			Util.addErrorMessage("Senhas não conferem.");
			return null;
		}
		try {
			saveUser();
			return "/resources/boasvindas.xhtml";
		} catch (DaoException e) {
			e.printStackTrace();
		}
		return "";
	}

	private void saveUser() throws DaoException {
		String _senha = UtilPassword.genPassword(user.getPassword());
		Date data = new Date();
		user.setPassword(_senha);
		user.setEnabled(true);
		user.setBlocked(false);
		user.setAlterLogin(data);
		user.setDatacadastro(data);
		user.setActivation(data);
		user = usersDao.create(user);
		Convitecuidador cc = ccDAO.find(idconvite);
		cc.setPendente(false);
		ccDAO.edit(cc);
		UsersPaciente usersPaciente = new UsersPaciente();
		usersPaciente.setIdpaciente(cc.getIdpaciente());
		usersPaciente.setPrincipal(false);
		usersPaciente.setUsers(user);
		usersPaciente.setEnabled(true);
		upDAO.create(usersPaciente);
	}

	public int getIdconvite() {
		return idconvite;
	}

	public void setIdconvite(int idconvite) {
		this.idconvite = idconvite;
	}

	public String getEmailCuidador() {
		return emailCuidador;
	}

	public void setEmailCuidador(String emailCuidador) {
		this.emailCuidador = emailCuidador;
	}

	public String getConfirmeSenha() {
		return confirmeSenha;
	}

	public void setConfirmeSenha(String confirmeSenha) {
		this.confirmeSenha = confirmeSenha;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

}
