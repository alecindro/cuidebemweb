package br.com.cuidebem.viewmodel;

import java.io.ByteArrayInputStream;
import java.util.List;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import br.com.cuidebem.model.Paciente;
import br.com.cuidebem.model.Users;

public class PacienteView {
	
	private Paciente paciente;
	private List<Users> cuidadores;	
	private boolean principal;
	private byte[] foto;
	private StreamedContent photoPaciente;
	
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
		if(foto != null){
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
