package br.com.cuidebem;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.primefaces.model.StreamedContent;

import br.com.cuidebem.exceptions.DaoException;
import br.com.cuidebem.model.Users;
import br.com.cuidebem.service.AbstractDao;
import br.com.cuidebem.service.PacienteDao;
import br.com.cuidebem.service.PacientePhotoDao;
import br.com.cuidebem.service.PatologiaPacienteDao;
import br.com.cuidebem.service.UsersDao;
import br.com.cuidebem.service.UsersPacienteDao;
import br.com.cuidebem.service.UsersPhotoDao;
import br.com.cuidebem.viewmodel.PacienteView;

@ManagedBean(name = "userMB")
@ViewScoped
public class UserMB {

	@Inject
	private Users user;
	@EJB
	protected UsersPhotoDao usersPhotoDao;
	@EJB
	protected Emailsvc emailSVC;
	@EJB
	protected UsersDao usersDao;
	@EJB
	protected PatologiaPacienteDao patologiaPacienteDao;



	@EJB
	protected PacienteDao pacienteDao;

	@EJB
	protected UsersPacienteDao usersPacienteDao;

	@EJB
	protected PacientePhotoDao pacientePhotoDao;

	private StreamedContent userPhoto;

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	@PostConstruct
	private void init() {
		String _user = UtilSecurity.getUser();
		if (_user != null) {
			try {
				user = usersDao.find(_user);
				byte[] photo = usersPhotoDao.findUserPhoto(getUser().getEmail());
				userPhoto = Util.generateStream(photo);
			} catch (DaoException e) {
				Util.addErrorMessage(e.getMessage());
			}
		}
	}

	public StreamedContent getUserPhoto() {
		return userPhoto;
	}

	public void setUserPhoto(StreamedContent userPhoto) {
		this.userPhoto = userPhoto;
	}

	
	

}
