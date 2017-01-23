package br.com.cuidebem;

import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.inject.Inject;

import br.com.cuidebem.control.exceptions.DaoException;
import br.com.cuidebem.model.Users;
import br.com.cuidebem.service.UsersDao;
import br.com.security.quali.password.UtilPassword;

@ManagedBean(name = "cadCuideBem")
public class CadCuideBem {

	private String nome;
	private String email;
	private String senha;
	private String confirmeSenha;
	private Integer tipoCadastro;
	private static final int TIPORESP = 1;
	private static final int TIPOCUID = 2;

	@Inject
	private UsersDao usersDao;

	@Inject
	private Emailsvc emailSVC;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getConfirmeSenha() {
		return confirmeSenha;
	}

	public void setConfirmeSenha(String confirmeSenha) {
		this.confirmeSenha = confirmeSenha;
	}

	public Integer getTipoCadastro() {
		return tipoCadastro;
	}

	public void setTipoCadastro(Integer tipoCadastro) {
		this.tipoCadastro = tipoCadastro;
	}

	public String cadastrar() {
		if (!senha.equals(confirmeSenha)) {
			// add message
			return null;
		}
		if (tipoCadastro == null) {
			// add message
			return null;
		}
		try{
		saveUser();
		emailSVC.confirmarEmail(email);
        return "/resources/boasvindas.xhtml";
		}catch(DaoException e){
			e.printStackTrace();
		}
		return "";
	}

	private void saveUser() throws DaoException {
		String _senha = UtilPassword.genPassword(senha);
		Users user = new Users(email, _senha, tipoCadastro, nome);
		user.setBlocked(true);
		user.setAlterLogin(new Date());
		user.setDatacadastro(new Date());
		usersDao.create(user);

	}

}
