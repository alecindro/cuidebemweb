package br.com.cuidebem;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import br.com.cuidebem.exceptions.DaoException;
import br.com.cuidebem.model.UsersPhoto;
import br.com.cuidebem.service.UsersPhotoDao;

@ManagedBean(name="menu")
@ViewScoped
public class Menu {
	
	private StreamedContent userPhoto;
	private String email;
	private UploadedFile file;
	
	@EJB 
	private UsersPhotoDao usersPhotoDao;
	
	@PostConstruct
	private void init() {
		email = UtilSecurity.getUser();
		if (email  != null) {
			try {
				byte[] photo = usersPhotoDao.findUserPhoto(email);
				userPhoto = Util.generateStream(photo);
			} catch (DaoException e) {
				Util.addErrorMessage("Erro ao carregar o menu:" + e.getMessage());
			}
		}
	}
	
	public void upload() {
		if (file != null && file.getContents().length > 0) {
			String user = UtilSecurity.getUser();
			try {
				InputStream inputStream = file.getInputstream();
				UsersPhoto usersPhoto = usersPhotoDao.savePhoto(user, inputStream);
				userPhoto = Util.generateStream(usersPhoto.getPhoto());
				Util.addSuccessMessage("Foto atualizada com sucesso");
				// return "pm:updateresp";
			} catch (DaoException e) {
				Util.addErrorMessage(e.getMessage());
			} catch (IOException e) {
				Util.addErrorMessage(e.getMessage());
			}
		}
		 
	}
	
	public void logout() {
		UtilSecurity.logout();
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		try {
			ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public StreamedContent getUserPhoto() {
		return userPhoto;
	}

	public void setUserPhoto(StreamedContent userPhoto) {
		this.userPhoto = userPhoto;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}
	
	


}
