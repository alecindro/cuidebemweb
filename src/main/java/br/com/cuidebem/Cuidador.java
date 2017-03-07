package br.com.cuidebem;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import br.com.cuidebem.exceptions.DaoException;
import br.com.cuidebem.model.Checkin;
import br.com.cuidebem.model.Convitecuidador;
import br.com.cuidebem.model.Paciente;
import br.com.cuidebem.service.CheckinDAO;
import br.com.cuidebem.service.ConvitecuidadorDAO;
import br.com.cuidebem.service.PacienteDao;

@ViewScoped
@ManagedBean(name = "cuidador")
public class Cuidador extends Menu {

	private List<Convitecuidador> convites;

	private List<Paciente> pacientes;
	@EJB
	private PacienteDao pacienteDAO;

	@EJB
	private CheckinDAO checkinDAO;

	@EJB
	private ConvitecuidadorDAO convitecuidadorDAO;

	@Inject
	private Paciente paciente;

	private Checkin checkin;

	private boolean check;

	@PostConstruct
	private void init() {
		initCheckin();
		initConvite();
	}

	private void initConvite() {
		try {
			convites = convitecuidadorDAO.findConviteEnabled(getUser().getEmail());
		} catch (DaoException e) {
			Util.addErrorMessage(e.getMessage());
		}
	}

	private void initCheckin() {
		try {
			checkin = checkinDAO.findCheckinbyUser(getUser());
			check = true;
			updateStatus();
		} catch (DaoException e) {
			Util.addErrorMessage(e.getMessage());
		}
	}

	public String checkout() {
		try {
			checkinDAO.checkout(checkin);
			checkin = null;
			updateStatus();
			return "pm:checkin";
		} catch (DaoException e) {
			Util.addErrorMessage(e.getMessage());
		}
		return "";
	}

	public void updateStatus() throws DaoException {
		if (checkin == null) {
			check = false;
			pacientes = pacienteDAO.findByUser(getUser().getEmail());
		}else{
			paciente = checkin.getPaciente();
		}
	}

	public void checkin(Paciente paciente) {
		if (check) {
			Util.addErrorMessage("Deve-se fazer o checkout primeiro.");
		}
		try {
			this.paciente = paciente;
			checkin = checkinDAO.checkIN(new Date(), getUser(), paciente);
			check = true;
		} catch (DaoException e) {
			Util.addErrorMessage(e.getMessage());
		}
	}

	public void aceitaConvite(Convitecuidador cc) {

		try {
			convitecuidadorDAO.aceitarConvite(getUser(), cc);
			initConvite();
			updateStatus();
		} catch (DaoException e) {
			Util.addErrorMessage(e.getMessage());
		}
	}

	public Paciente getPaciente() {
		return paciente;
	}

	public void setPaciente(Paciente paciente) {
		this.paciente = paciente;
	}

	public boolean isCheck() {
		return check;
	}

	public void setCheck(boolean check) {
		this.check = check;
	}

	public List<Paciente> getPacientes() {
		return pacientes;
	}

	public void setPacientes(List<Paciente> pacientes) {
		this.pacientes = pacientes;
	}

	public List<Convitecuidador> getConvites() {
		return convites;
	}

	public void setConvites(List<Convitecuidador> convites) {
		this.convites = convites;
	}
	
	

}
