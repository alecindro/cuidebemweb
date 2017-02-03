package br.com.cuidebem;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import br.com.cuidebem.exceptions.DaoException;
import br.com.cuidebem.model.Paciente;
import br.com.cuidebem.model.PacientePhoto;
import br.com.cuidebem.model.PatologiaPaciente;
import br.com.cuidebem.model.Patologias;
import br.com.cuidebem.model.UsersPaciente;
import br.com.cuidebem.service.PacienteDao;
import br.com.cuidebem.service.PacientePhotoDao;
import br.com.cuidebem.service.PatologiaPacienteDao;
import br.com.cuidebem.service.UsersPacienteDao;

@ViewScoped
@ManagedBean(name = "cadPaciente")
public class CadPaciente {

	private Patologias[] selected_patologias = new Patologias[Patologias.values().length+1];
	private List<Patologias> patologias;
	private String outra_patologia;
	private String email;
	private boolean principal = true;
	private UploadedFile file;
	private StreamedContent pacientePhoto;
	
	@EJB
	private PatologiaPacienteDao patologiaPacienteDao;

	@Inject
	private Paciente paciente;

	@EJB
	private PacienteDao pacienteDao;
	
	@EJB
	private UsersPacienteDao usersPacienteDao; 
	
	@EJB 
	private PacientePhotoDao pacientePhotoDao;

	private String idpaciente;

	@PostConstruct
	public void init() {
		patologias = Arrays.asList(Patologias.values());
		email = UtilSecurity.getUser();
	}

	public String getIdpaciente() {
		return idpaciente;
	}

	public void setIdpaciente(String idpaciente) {
		this.idpaciente = idpaciente;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public void editPaciente(Integer _idpaciente){
		if(_idpaciente!= null){
				loadPaciente(_idpaciente);
				loadPatologias(_idpaciente);
				loadPrincipal(_idpaciente);			
				loadPhoto(_idpaciente);
		}
	}

	public Paciente getPaciente() {
		/**if (paciente.getIdpaciente() == null && idpaciente != null) {
			try{
			Integer _idpaciente = Integer.valueOf(idpaciente);
			loadPaciente(_idpaciente);
			loadPatologias(_idpaciente);
			loadPrincipal(_idpaciente);			
			}catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}**/
		return paciente;
	}

	public void setPaciente(Paciente paciente) {
		this.paciente = paciente;
	}

	public Patologias[] getSelected_patologias() {
		return selected_patologias;
	}

	public void setSelected_patologias(Patologias[] selected_patologias) {
		this.selected_patologias = selected_patologias;
	}

	public List<Patologias> getPatologias() {
		return patologias;
	}

	public void setPatologias(List<Patologias> patologias) {
		this.patologias = patologias;
	}

	public String getOutra_patologia() {
		return outra_patologia;
	}

	public void setOutra_patologia(String outra_patologia) {
		this.outra_patologia = outra_patologia;
	}

	public String salvarPaciente() {
		try {
			String email = UtilSecurity.getUser();

			List<String> patologiasSelected = new ArrayList<String>();
			for (Patologias _pat : selected_patologias) {
				patologiasSelected.add(_pat.getDescricao());
			}
			if (outra_patologia != null) {
				patologiasSelected.add(outra_patologia);
			}

			pacienteDao.save(paciente, email, patologiasSelected, principal);	
			Util.addSuccessMessage("Paciente cadastrado com sucesso.");
			return "pm:listpaciente";
		} catch (DaoException e) {
			Util.addErrorMessage("Erro ao cadastrar Paciente.");
			Util.addErrorMessage("Detalhe: " + e.getMessage());
			return "pm:cadpaciente";
		}
	}

	public boolean isPrincipal() {
		return principal;
	}

	public void setPrincipal(boolean principal) {
		this.principal = principal;
	}

	private void loadPaciente(Integer _idpaciente) {
		try {
			
			paciente = pacienteDao.find(_idpaciente);
			
		}  catch (DaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void loadPatologias(Integer _idpaciente){
		List<PatologiaPaciente> ppList;
		try {
			ppList = patologiaPacienteDao.findByIdPaciente(_idpaciente);
			int i =0;
			for(PatologiaPaciente pp : ppList ){
				Patologias _patologia = Patologias.find(pp.getPatologia());
				if(_patologia!=null){
					selected_patologias[i] = _patologia;
					i = i+1;	
				}else{
					outra_patologia = pp.getPatologia();
				}
				
			}
		} catch (DaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void loadPrincipal(Integer _idpaciente){
		UsersPaciente up;
		try {
			up = usersPacienteDao.findbyIdPaciente(_idpaciente);
			if(up != null){
				principal = up.isPrincipal();
			}
		} catch (DaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void loadPhoto(Integer _idpaciente){
		try {
			byte[] photo = pacientePhotoDao.findPacientePhoto(_idpaciente);
			pacientePhoto = Util.generateStream(photo);
		} catch (DaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void upload() {
        if(file != null && file.getContents().length > 0) {
        	
        	try {
    			InputStream inputStream = file.getInputstream();
    			PacientePhoto pacPhoto = pacientePhotoDao.savePhoto(paciente.getIdpaciente(),inputStream);
    			pacientePhoto = Util.generateStream(pacPhoto.getPhoto());
    			Util.addSuccessMessage("Foto atualizada com sucesso");
    		} catch (DaoException e) {
    			Util.addErrorMessage(e.getMessage());
    		} catch (IOException e) {
    			Util.addErrorMessage(e.getMessage());
			}
        	
        }
      //  return "pm:updateresp";
    }

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}

	public StreamedContent getPacientePhoto() {
	
		return pacientePhoto;
	}

	public void setPacientePhoto(StreamedContent pacientePhoto) {
		this.pacientePhoto = pacientePhoto;
	}
	


}
