package br.com.cuidebem;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import br.com.cuidebem.exceptions.DaoException;
import br.com.cuidebem.model.Paciente;
import br.com.cuidebem.model.UsersPaciente;
import br.com.cuidebem.service.PacienteDao;
import br.com.cuidebem.service.PacientePhotoDao;
import br.com.cuidebem.service.UsersDao;
import br.com.cuidebem.service.UsersPacienteDao;
import br.com.cuidebem.viewmodel.PacienteView;

@ViewScoped
@ManagedBean(name="listPaciente")
public class ListPaciente {

	
	private List<PacienteView> pacientes;
	
	private String email = UtilSecurity.getUser();
	@EJB
	private PacienteDao pacienteDao;
	@EJB
	private UsersPacienteDao usersPacienteDao; 
	@EJB
	private PacientePhotoDao pacientePhotoDao;
	@EJB
	private UsersDao usersDao;

	
	@PostConstruct
	public void init(){
		
		 try {
			List<Paciente> _pacientes = pacienteDao.findByUser(email);
			initView(_pacientes);
		} catch (DaoException e) {
			Util.addErrorMessage(e.getMessage());
		}
		
	}
	
	private void initView(List<Paciente> _pacientes){
			pacientes = new ArrayList<PacienteView>();
		for(Paciente paciente : _pacientes){
			PacienteView pacienteView = new PacienteView();
			pacienteView.setPaciente(paciente);
			loadPrincipal(pacienteView);
			loadPhoto(pacienteView);
			loadCuidadores(pacienteView);
			pacientes.add(pacienteView);
			
		}
	}
	
	
	public void excluirPaciente(Paciente paciente){
		try {
			pacienteDao.remove(paciente);
			List<Paciente> _pacientes = pacienteDao.findByUser(email);
			initView(_pacientes);
			Util.addSuccessMessage("Paciente excluido com sucesso.");
		} catch (DaoException e) {
			Util.addErrorMessage("Erro ao excluir Paciente:" + e.getMessage());
		}
	}
	
	
	public List<PacienteView> getPacientes() {
		return pacientes;
	}



	public void setPacientes(List<PacienteView> pacientes) {
		this.pacientes = pacientes;
	}

	private void loadPrincipal(PacienteView pacienteView){
		UsersPaciente up;
		try {
			up = usersPacienteDao.findbyIdPaciente(pacienteView.getPaciente().getIdpaciente());
			if(up != null){
				pacienteView.setPrincipal(up.isPrincipal());
			}
		} catch (DaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void loadPhoto(PacienteView pacienteView){
		try {
			pacienteView.setFoto(pacientePhotoDao.findPacientePhoto(pacienteView.getPaciente().getIdpaciente()));
		} catch (DaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void loadCuidadores(PacienteView pacienteView){
		try {
			pacienteView.setCuidadores(usersDao.findColaboradoresByPaciente(pacienteView.getPaciente().getIdpaciente()));
		} catch (DaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
}
