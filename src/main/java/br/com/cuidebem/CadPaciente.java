package br.com.cuidebem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.inject.Inject;

import org.apache.shiro.SecurityUtils;

import br.com.cuidebem.control.exceptions.DaoException;
import br.com.cuidebem.model.Paciente;
import br.com.cuidebem.model.Patologias;
import br.com.cuidebem.service.PacienteDao;

@RequestScoped
@ManagedBean(name="cadPaciente")
public class CadPaciente {

	private int genero;
	private boolean plano_saude;
	private String desc_plano_saude;
	private Patologias[] selected_patologias= new Patologias[Patologias.values().length];
	private List<Patologias> patologias;
	private String outra_patologia;
	private String nome;
	private String sobrenome;
	private Date dataNascimento;
	private boolean principal = true;
	
	@Inject
	private PacienteDao pacienteDao;
	
	@PostConstruct
	public void init(){
		patologias = Arrays.asList(Patologias.values());
	}
	
	public int getGenero() {
		return genero;
	}
	public void setGenero(int genero) {
		this.genero = genero;
	}
	public boolean isPlano_saude() {
		return plano_saude;
	}
	public void setPlano_saude(boolean plano_saude) {
		this.plano_saude = plano_saude;
	}
	public String getDesc_plano_saude() {
		return desc_plano_saude;
	}
	public void setDesc_plano_saude(String desc_plano_saude) {
		this.desc_plano_saude = desc_plano_saude;
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
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getSobrenome() {
		return sobrenome;
	}
	public void setSobrenome(String sobrenome) {
		this.sobrenome = sobrenome;
	}
	public Date getDataNascimento() {
		return dataNascimento;
	}
	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}
	public String getOutra_patologia() {
		return outra_patologia;
	}
	public void setOutra_patologia(String outra_patologia) {
		this.outra_patologia = outra_patologia;
	}
	
	public void salvarPaciente(){
		String email = "alecindrocastilho@gmail.com";//SecurityUtils.getSubject().toString();
		Paciente paciente = new Paciente(nome, sobrenome, genero, dataNascimento, desc_plano_saude);
		List<String> patologiasSelected = new ArrayList<String>();
		for(Patologias _pat : selected_patologias ){
			patologiasSelected.add(_pat.getDescricao());
		}
		if(outra_patologia != null){
		patologiasSelected.add(outra_patologia);
		}
		try {
			pacienteDao.save(paciente, email, patologiasSelected, principal);
		} catch (DaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean isPrincipal() {
		return principal;
	}

	public void setPrincipal(boolean principal) {
		this.principal = principal;
	}
	
	
	
	
}
