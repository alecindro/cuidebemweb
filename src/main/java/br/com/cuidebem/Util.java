package br.com.cuidebem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.model.ByteArrayContent;
import org.primefaces.model.StreamedContent;

public class Util {
	
	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" 
	+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	private static Pattern patternEmail =  Pattern.compile(EMAIL_PATTERN);


	public static void addSuccessMessage(String message) {
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", message));

	}

	public static void addErrorMessage(String message) {
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", message));

	}

	public static Date convert(String dateinput, String pattern) throws ParseException {
		SimpleDateFormat formatDate = new SimpleDateFormat(pattern);
		return formatDate.parse(dateinput);
	}

	public static StreamedContent generateStream(byte[] photo) {
		if (photo != null) {
			return new ByteArrayContent(photo);
		}
		return null;
	}
	
	public static boolean validateEmail(String email){
		return patternEmail.matcher(email).matches();
	}
}