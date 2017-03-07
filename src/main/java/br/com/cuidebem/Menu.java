package br.com.cuidebem;

import java.io.IOException;
import java.io.InputStream;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.model.UploadedFile;

import br.com.cuidebem.exceptions.DaoException;
import br.com.cuidebem.model.UsersPhoto;

@ManagedBean(name = "menu")
@ViewScoped
public class Menu extends UserMB {

	private UploadedFile file;

	public void uploadUserPhoto() {
		if (file != null && file.getContents().length > 0) {
			try {
				InputStream inputStream = file.getInputstream();
				UsersPhoto usersPhoto = usersPhotoDao.savePhoto(getUser().getEmail(), inputStream);
				setUserPhoto(Util.generateStream(usersPhoto.getPhoto()));
				Util.addSuccessMessage("Foto atualizada com sucesso");
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
			Util.addErrorMessage(e.getMessage());
		}
	}

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}

}
