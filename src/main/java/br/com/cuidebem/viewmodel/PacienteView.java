package br.com.cuidebem.viewmodel;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.util.List;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import br.com.cuidebem.exceptions.DaoException;
import br.com.cuidebem.model.Paciente;
import br.com.cuidebem.model.PatologiaPaciente;
import br.com.cuidebem.model.Patologias;
import br.com.cuidebem.model.Users;
import br.com.cuidebem.model.UsersPaciente;
import br.com.cuidebem.service.PacientePhotoDao;
import br.com.cuidebem.service.PatologiaPacienteDao;
import br.com.cuidebem.service.UsersDao;
import br.com.cuidebem.service.UsersPacienteDao;

public class PacienteView implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Paciente paciente;
	private List<Users> cuidadores;
	private boolean principal;
	private byte[] foto;
	private StreamedContent photoPaciente;
	private Patologias[] selected_patologias = new Patologias[Patologias.values().length + 1];
	private String outra_patologia;

	public Paciente getPaciente() {
		return paciente;
	}

	public void setPaciente(Paciente paciente) {
		this.paciente = paciente;
	}

	public List<Users> getCuidadores() {
		return cuidadores;
	}

	public void setCuidadores(List<Users> cuidadores) {
		this.cuidadores = cuidadores;
	}

	public boolean isPrincipal() {
		return principal;
	}

	public void setPrincipal(boolean principal) {
		this.principal = principal;
	}

	public byte[] getFoto() {
		return foto;
	}

	public void setFoto(byte[] foto) {
		if (foto != null) {
			ByteArrayInputStream bi = new ByteArrayInputStream(foto);
			photoPaciente = new DefaultStreamedContent(bi);
		}
		this.foto = foto;
	}

	public StreamedContent getPhotoPaciente() {
		return photoPaciente;
	}

	public void setPhotoPaciente(StreamedContent photoPaciente) {
		this.photoPaciente = photoPaciente;
	}

	public Patologias[] getSelected_patologias() {
		return selected_patologias;
	}

	public void setSelected_patologias(Patologias[] selected_patologias) {
		this.selected_patologias = selected_patologias;
	}

	public String getOutra_patologia() {
		return outra_patologia;
	}

	public void setOutra_patologia(String outra_patologia) {
		this.outra_patologia = outra_patologia;
	}

	public void loadPrincipal(UsersPacienteDao usersPacienteDao) throws DaoException {
		UsersPaciente up = usersPacienteDao.findbyIdPaciente(this.getPaciente().getIdpaciente());
		if (up != null) {
			this.setPrincipal(up.isPrincipal());
		}
	}

	public void loadPhoto(PacientePhotoDao pacientePhotoDao) throws DaoException {
		this.setFoto(pacientePhotoDao.findPacientePhoto(this.getPaciente().getIdpaciente()));
	}

	public void loadCuidadores(UsersDao usersDao) throws DaoException {
		this.setCuidadores(usersDao.findColaboradoresByPaciente(this.getPaciente().getIdpaciente()));
	}

	public void loadPatologias(PatologiaPacienteDao patologiaPacienteDao) throws DaoException {
		List<PatologiaPaciente> ppList = patologiaPacienteDao.findByIdPaciente(this.getPaciente().getIdpaciente());
		int i = 0;
		for (PatologiaPaciente pp : ppList) {
			Patologias _patologia = Patologias.find(pp.getPatologia());
			if (_patologia != null) {
				selected_patologias[i] = _patologia;
				i = i + 1;
			} else {
				outra_patologia = pp.getPatologia();
			}
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((paciente == null) ? 0 : paciente.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PacienteView other = (PacienteView) obj;
		if (paciente == null) {
			if (other.paciente != null)
				return false;
		} else if (!paciente.equals(other.paciente))
			return false;
		return true;
	}

}
