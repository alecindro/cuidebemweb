package br.com.cuidebem;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;

import org.primefaces.model.ByteArrayContent;
import org.primefaces.model.StreamedContent;

public class Util {

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
		ByteArrayContent result = new ByteArrayContent();
		if (photo != null) {
			return new ByteArrayContent(photo);
		}
		return null;
	}
}
