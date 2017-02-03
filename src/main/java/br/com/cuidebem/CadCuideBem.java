package br.com.cuidebem;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.StreamedContent;

import br.com.cuidebem.exceptions.DaoException;
import br.com.cuidebem.model.Users;
import br.com.cuidebem.service.UsersDao;
import br.com.cuidebem.service.UsersPhotoDao;
import br.com.security.quali.password.UtilPassword;

@ManagedBean(name = "cadCuideBem")
@ViewScoped
public class CadCuideBem {

	private String nome;
	private String email;
	private String senha;
	private String confirmeSenha;
	private Integer tipoCadastro;

	private StreamedContent userPhoto;

	private String newPassword;
	private String repeatNewPassword;

	@EJB
	private UsersDao usersDao;

	@EJB
	private Emailsvc emailSVC;

	private Users user;


	@EJB
	private UsersPhotoDao usersPhotoDao;

	@PostConstruct
	private void init() {
		String _user = UtilSecurity.getUser();
		if (_user != null) {
			try {
				user = usersDao.find(_user);
				byte[] photo = usersPhotoDao.findUserPhoto(_user);
				userPhoto = Util.generateStream(photo);

			} catch (DaoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}



	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getRepeatNewPassword() {
		return repeatNewPassword;
	}

	public void setRepeatNewPassword(String repeatNewPassword) {
		this.repeatNewPassword = repeatNewPassword;
	}

	

	public void updatePassowrd() {
		String email = UtilSecurity.getUser();
		if (newPassword != null && repeatNewPassword != null) {
			if (!newPassword.equals(repeatNewPassword)) {
				Util.addErrorMessage("A senha não confere.");
			} else {
				try {
					usersDao.updatePassword(email, newPassword);
					Util.addSuccessMessage("Senha alterada com sucesso.");
					newPassword = "";
					repeatNewPassword = "";
				} catch (DaoException e) {
					Util.addErrorMessage("Erro ao alterar senha: " + e.getMessage());
				}
			}
		} else {
			Util.addErrorMessage("Favor digitar a nova senha.");
		}
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

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
		try {
			saveUser();
			emailSVC.confirmarEmail(email);
			return "/resources/boasvindas.xhtml";
		} catch (DaoException e) {
			e.printStackTrace();
		}
		return "";
	}

	public String atualizar() {
		try {
			usersDao.edit(user);
			Util.addSuccessMessage("Cadastro atualizado com sucesso.");

			return "pm:main";
		} catch (DaoException e) {
			// TODO Auto-generated catch block
			Util.addErrorMessage("Erro ao atualizar o cadastro.");
			return "pm:updateresp";
		}
	}

	private void saveUser() throws DaoException {
		String _senha = UtilPassword.genPassword(senha);
		Users user = new Users(email, _senha, tipoCadastro, nome);
		user.setBlocked(true);
		user.setAlterLogin(new Date());
		user.setDatacadastro(new Date());
		usersDao.create(user);

	}

	public StreamedContent getUserPhoto() {
		return userPhoto;
	}

	public void setUserPhoto(StreamedContent userPhoto) {
		this.userPhoto = userPhoto;
	}



}
