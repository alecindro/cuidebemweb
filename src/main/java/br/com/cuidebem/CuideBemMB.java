package br.com.cuidebem;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import br.com.cuidebem.exceptions.DaoException;
import br.com.cuidebem.model.Convitecuidador;
import br.com.cuidebem.model.Paciente;
import br.com.cuidebem.model.PacientePhoto;
import br.com.cuidebem.model.Patologias;
import br.com.cuidebem.service.ConvitecuidadorDAO;
import br.com.cuidebem.viewmodel.PacienteView;
import br.com.security.quali.password.UtilPassword;

@ManagedBean(name = "cuideBemMB")
@ViewScoped
public class CuideBemMB extends Menu {

	@EJB
	protected ConvitecuidadorDAO conviteCuidadorDAO;

	// CadPaciente
	private List<Patologias> patologias;
	@Inject
	private PacienteView paciente;
	// ListPacientes
	private List<PacienteView> pacientes;
	// CadResponsavel
	private String confirmeSenha;
	private String newPassword;
	private String repeatNewPassword;

	// Convidar cuidador
	private String emailCuidador;

	@PostConstruct
	public void init() {
		patologias = Arrays.asList(Patologias.values());
		try {
			initPacientes();
		} catch (DaoException e) {
			Util.addErrorMessage(e.getMessage());
		}
	}

	// ListPacientes inicio
	private void initPacientes() throws DaoException {
		List<Paciente> _pacientes = pacienteDao.findByUser(getUser().getEmail());
		pacientes = new ArrayList<PacienteView>();
		for (Paciente paciente : _pacientes) {
			PacienteView pacienteView = new PacienteView();
			pacienteView.setPaciente(paciente);
			pacienteView.loadPrincipal(usersPacienteDao);
			pacienteView.loadPhoto(pacientePhotoDao);
			pacienteView.loadCuidadores(usersDao);
			pacienteView.loadPatologias(patologiaPacienteDao);
			pacientes.add(pacienteView);

		}
	}

	// Responsavel Inicio
	public void updatePassowrd() {
		if (newPassword != null && repeatNewPassword != null) {
			if (!newPassword.equals(repeatNewPassword)) {
				Util.addErrorMessage("A senha não confere.");
			} else {
				try {
					String newPasswordHash = UtilPassword.genPassword(newPassword);
					usersDao.updatePassword(getUser().getPassword(), newPasswordHash);
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

	public void convidarCuidador() {
		if (!Util.validateEmail(emailCuidador)) {
			Util.addErrorMessage("Email inválido: " + emailCuidador);
		} else {
			Convitecuidador cc = new Convitecuidador();
			cc.setDtconvite(new java.util.Date());
			cc.setEmailcuidador(emailCuidador);
			cc.setEmailresponsavel(getUser());
			cc.setIdpaciente(getPaciente().getPaciente());
			cc.setPendente(true);
			try {
				cc = conviteCuidadorDAO.create(cc);
				emailSVC.convidarCuidador(getUser().getNome(),emailCuidador,cc.getIdconvitecuidador());
				Util.addSuccessMessage("Email foi enviado ao cuidador.");
			} catch (DaoException e) {
				Util.addErrorMessage(e.getMessage());
			}
		}
	}

	public String cadastrar() {
		if (!getUser().getPassword().equals(confirmeSenha)) {
			Util.addErrorMessage("Senhas não conferem.");
			return null;
		}
		if (getUser().getTypeuser() == 0) {
			Util.addErrorMessage("Selecione o tipo de usuário");
			return null;
		}
		try {
			saveUser();
			emailSVC.confirmarEmail(getUser().getEmail());
			return "/resources/boasvindas.xhtml";
		} catch (DaoException e) {
			e.printStackTrace();
		}
		return "";
	}

	public String atualizarUsuario() {
		try {
			usersDao.edit(getUser());
			Util.addSuccessMessage("Cadastro atualizado com sucesso.");

			return "pm:main";
		} catch (DaoException e) {

			Util.addErrorMessage("Erro ao atualizar o cadastro.");
			return "pm:updateresp";
		}
	}

	private void saveUser() throws DaoException {
		String _senha = UtilPassword.genPassword(getUser().getPassword());
		getUser().setPassword(_senha);
		getUser().setBlocked(true);
		getUser().setAlterLogin(new Date());
		getUser().setDatacadastro(new Date());
		usersDao.create(getUser());

	}

	// Paciente Crud
	public void excluirPaciente(PacienteView pacienteView) {
		try {
			pacienteDao.remove(pacienteView.getPaciente());
			pacientes.remove(pacienteView);
			Util.addSuccessMessage("Paciente excluido com sucesso.");
		} catch (DaoException e) {
			Util.addErrorMessage("Erro ao excluir Paciente:" + e.getMessage());
		}
	}

	public void editPaciente(PacienteView paciente) {
		this.paciente = paciente;
		try {
			paciente.loadPhoto(pacientePhotoDao);
		} catch (DaoException e) {
			Util.addErrorMessage(e.getMessage());
		}
	}

	public void editPacienteView(PacienteView paciente) {
		this.paciente = paciente;
	}

	public String salvarPaciente() {
		try {
			List<String> patologiasSelected = new ArrayList<String>();
			for (Patologias _pat : paciente.getSelected_patologias()) {
				patologiasSelected.add(_pat.getDescricao());
			}
			if (paciente.getOutra_patologia() != null) {
				patologiasSelected.add(paciente.getOutra_patologia());
			}
			pacienteDao.save(paciente.getPaciente(), getUser().getEmail(), patologiasSelected, paciente.isPrincipal());
			Util.addSuccessMessage("Paciente cadastrado com sucesso.");
			return "pm:listpaciente";
		} catch (DaoException e) {
			Util.addErrorMessage("Erro ao cadastrar Paciente.");
			Util.addErrorMessage("Detalhe: " + e.getMessage());
			return "pm:cadpaciente";
		}
	}

	public void uploadPaciente() {
		if (getFile() != null && getFile().getContents().length > 0) {

			try {
				InputStream inputStream = getFile().getInputstream();
				PacientePhoto pacPhoto = pacientePhotoDao.savePhoto(paciente.getPaciente().getIdpaciente(),
						inputStream);
				paciente.setPhotoPaciente(Util.generateStream(pacPhoto.getPhoto()));
				Util.addSuccessMessage("Foto atualizada com sucesso");
			} catch (DaoException e) {
				Util.addErrorMessage(e.getMessage());
			} catch (IOException e) {
				Util.addErrorMessage(e.getMessage());
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

	public PacienteView getPaciente() {
		return paciente;
	}

	public void setPaciente(PacienteView paciente) {
		this.paciente = paciente;
	}

	public List<Patologias> getPatologias() {
		return patologias;
	}

	public void setPatologias(List<Patologias> patologias) {
		this.patologias = patologias;
	}

	public String getConfirmeSenha() {
		return confirmeSenha;
	}

	public void setConfirmeSenha(String confirmeSenha) {
		this.confirmeSenha = confirmeSenha;
	}

	// ListPacientes
	public List<PacienteView> getPacientes() {
		return pacientes;
	}

	public void setPacientes(List<PacienteView> pacientes) {
		this.pacientes = pacientes;
	}

	public String getEmailCuidador() {
		return emailCuidador;
	}

	public void setEmailCuidador(String emailCuidador) {
		this.emailCuidador = emailCuidador;
	}

}
