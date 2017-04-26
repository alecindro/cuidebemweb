package br.com.cuidebem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import br.com.cuidebem.exceptions.DaoException;
import br.com.cuidebem.model.Checkin;
import br.com.cuidebem.model.Convitecuidador;
import br.com.cuidebem.model.Eventos;
import br.com.cuidebem.model.Paciente;
import br.com.cuidebem.model.rotinas.BemEstar;
import br.com.cuidebem.model.rotinas.Consultas;
import br.com.cuidebem.model.rotinas.Dor;
import br.com.cuidebem.model.rotinas.Eliminacoes;
import br.com.cuidebem.model.rotinas.Higiene;
import br.com.cuidebem.model.rotinas.InterOcorrencias;
import br.com.cuidebem.model.rotinas.Medicacao;
import br.com.cuidebem.model.rotinas.Nutricao;
import br.com.cuidebem.model.rotinas.Respiratorio;
import br.com.cuidebem.model.rotinas.Rotinas;
import br.com.cuidebem.model.rotinas.SinaisVitais;
import br.com.cuidebem.service.CheckinDAO;
import br.com.cuidebem.service.ConvitecuidadorDAO;
import br.com.cuidebem.service.EventosDAO;
import br.com.cuidebem.service.PacienteDao;

@ViewScoped
@ManagedBean(name = "cuidador")
public class Cuidador extends Menu {

	private List<Convitecuidador> convites;

	private List<Paciente> pacientes;
	@EJB
	private PacienteDao pacienteDAO;

	@EJB
	private CheckinDAO checkinDAO;

	@EJB
	private ConvitecuidadorDAO convitecuidadorDAO;

	@EJB
	private EventosDAO eventosDAO;

	@Inject
	private Paciente paciente;

	private Checkin checkin;

	private boolean check;
	
	
	private Rotinas[] rotinas = Rotinas.values();
	private Rotinas rotinaSelected;
	
	private SinaisVitais sinaisVitais;
	private Respiratorio respiratorio;
	private Nutricao nutricao;
	private Medicacao medicacao;
	private InterOcorrencias interOcorrencias;
	private Higiene higiene;
	private Eliminacoes eliminacoes;
	private Dor dor;
	private Consultas consultas;
	private BemEstar bemEstar;
	
	private Integer pressaoInitial;
	private Integer pressaoFinal;
	
	private String qtidade;
	private String aspecto;
	private String opcao;
	
	
	private List<Eventos> eventos = new ArrayList<Eventos>();
	@Inject
	private Eventos evento;

	@PostConstruct
	private void init() {
		initCheckin();   
		initConvite();
	}

	private void initConvite() {
		try {
			convites = convitecuidadorDAO.findConviteEnabled(getUser().getEmail());
		} catch (DaoException e) {
			Util.addErrorMessage(e.getMessage());
		}
	}

	private void initCheckin() {
		try {
			checkin = checkinDAO.findCheckinbyUser(getUser());
			check = true;
			updateStatus();
		} catch (DaoException e) {
			Util.addErrorMessage(e.getMessage());
		}
	}


	public void checkout() {
		try {
			checkinDAO.checkout(checkin);
			checkin = null;
			updateStatus();
			zeroEventos();
		} catch (DaoException e) {
			Util.addErrorMessage(e.getMessage());
		}
	}

	public void updateStatus() throws DaoException {
		if (checkin == null) {
			check = false;
			pacientes = pacienteDAO.findByUser(getUser().getEmail());
		} else {
			paciente = checkin.getPaciente();
			updateEventos();
		}
	}
	
	public void zeroEventos(){
		evento = new Eventos();
		rotinaSelected = null;
		sinaisVitais = null;
		eliminacoes = null;
	}
	
	public void selectedRotina(){
	
		if(rotinaSelected == null){
			rotinaSelected = Rotinas.SINALVITAL;
			sinaisVitais = SinaisVitais.PRESSAOARTERIAL;
		}
		
		
/*		switch(rotinaSelected){
		case SINALVITAL: subrotina=SinaisVitais.values();
		break;
		case BEMESTAR: subrotina=BemEstar.values();
		break;
		case CONSULTAS: subrotina=Consultas.values();
		break;
		case DOR: subrotina=Dor.values();
		break;
		case ELIMINACOES: subrotina=Eliminacoes.values();
		break;
		case HIGIENE: subrotina=Higiene.values();
		break;
		case INTEROCORRENCIAS: subrotina=InterOcorrencias.values();
		break;
		case MEDICACAO: subrotina=Medicacao.values();
		break;
		case NUTRICAO: subrotina=Nutricao.values();
		break;
		case RESPIRATORIO: subrotina=Respiratorio.values();
		break;
		}*/
		
	}
	
	
	
	public void updateEventos(){
		try {
			eventos = eventosDAO.findEnabledByPaciente(paciente);
		} catch (DaoException e) {
			Util.addErrorMessage(e.getMessage());
		}
	}

	public void checkin(Paciente paciente) {
		if (check) {
			Util.addErrorMessage("Deve-se fazer o checkout primeiro.");
		}
		try {
			this.paciente = paciente;
			checkin = checkinDAO.checkIN(new Date(), getUser(), paciente);
			check = true;
		} catch (DaoException e) {
			Util.addErrorMessage(e.getMessage());
		}
	}
	
	
	
	public void addEvento() {
		evento.setIdcuidador(getUser());
		evento.setIdpaciente(getPaciente());
		evento.setGrupoevento(rotinaSelected.getDescricao());
		switch (rotinaSelected) {
		case SINALVITAL:
			evento.setSubgrupoevento(sinaisVitais.getDescricao());
			switch(sinaisVitais){
			case PRESSAOARTERIAL:
				evento.setRespeventos(getPressaoInitial()+" x "+getPressaoFinal()+" mmHg");
				break;
			case CONTROLEGLICEMIA:
				evento.setRespeventos(evento.getRespeventos()+" mmHg");
				break;
			case FREQUENCIACARDIACA:
				evento.setRespeventos(evento.getRespeventos()+ " bpm");
				break;
			case FREQUENCIARESPIRATORIA:
				evento.setRespeventos(evento.getRespeventos()+ " vezes/min");
				break;
			case SATURACAOOXIGENIO:
				evento.setRespeventos(evento.getRespeventos()+ " %");
				break;
			case TEMPERATURA:
				evento.setRespeventos(evento.getRespeventos()+ " ºC");
				break;
			};
			break;
		case ELIMINACOES:
			evento.setSubgrupoevento(eliminacoes.getDescricao());
			evento.setRespeventos("Quantidade: "+getQtidade()+" - Aspecto: "+getAspecto());
			break;
		case BEMESTAR:
			evento.setSubgrupoevento(bemEstar.getDescricao());
			break;
		case CONSULTAS:
			evento.setSubgrupoevento(consultas.getDescricao());
			break;
		case DOR:
			evento.setSubgrupoevento(dor.getDescricao());
			break;
		case HIGIENE:
			evento.setSubgrupoevento(higiene.getDescricao());
			break;
		case INTEROCORRENCIAS:
			evento.setSubgrupoevento(interOcorrencias.getDescricao());
			break;
		case MEDICACAO:
			evento.setSubgrupoevento(medicacao.getDescricao());
			break;
		case NUTRICAO:
			evento.setSubgrupoevento(nutricao.getDescricao());
			break;
		case RESPIRATORIO:
			evento.setSubgrupoevento(respiratorio.getDescricao());
			evento.setRespeventos("Tipo: "+getOpcao()+" - Quantidade: "+getQtidade()+" - Aspecto: "+getAspecto());
			break;
		default:
			break;
		}
		if (evento.getIdeventos() == null) {
			try {
				eventosDAO.create(evento);
			} catch (Exception e) {
				Util.addErrorMessage(e.getMessage());
			}
		} else {
			try {
				eventosDAO.edit(evento);
			} catch (Exception e) {
				Util.addErrorMessage(e.getMessage());
			}
		}
		updateEventos();
		zeroEventos();
		
	}

	public void excluirEvento(Eventos evento) {
		try {
			eventosDAO.delete(evento);
			updateEventos();
		} catch (Exception e) {
			Util.addErrorMessage(e.getMessage());
		}
		
	}


	public void aceitaConvite(Convitecuidador cc) {

		try {
			convitecuidadorDAO.aceitarConvite(getUser(), cc);
			initConvite();
			updateStatus();
		} catch (DaoException e) {
			Util.addErrorMessage(e.getMessage());
		}
	}

	public Paciente getPaciente() {
		return paciente;
	}

	public void setPaciente(Paciente paciente) {
		this.paciente = paciente;
	}

	public boolean isCheck() {
		return check;
	}

	public void setCheck(boolean check) {
		this.check = check;
	}

	public List<Paciente> getPacientes() {
		return pacientes;
	}

	public void setPacientes(List<Paciente> pacientes) {
		this.pacientes = pacientes;
	}

	public List<Convitecuidador> getConvites() {
		return convites;
	}

	public void setConvites(List<Convitecuidador> convites) {
		this.convites = convites;
	}

	public List<Eventos> getEventos() {
		return eventos;
	}

	public void setEventos(List<Eventos> eventos) {
		this.eventos = eventos;
	}

	public Eventos getEvento() {
		return evento;
	}

	public void setEvento(Eventos evento) {
		this.evento = evento;
	}

	public Rotinas[] getRotinas() {
		return rotinas;
	}

	public void setRotinas(Rotinas[] rotinas) {
		this.rotinas = rotinas;
	}

	public Rotinas getRotinaSelected() {
		return rotinaSelected;
	}

	public void setRotinaSelected(Rotinas rotinaSelected) {
		this.rotinaSelected = rotinaSelected;
	}

	public SinaisVitais getSinaisVitais() {
		return sinaisVitais;
	}

	public void setSinaisVitais(SinaisVitais sinaisVitais) {
		this.sinaisVitais = sinaisVitais;
	}

	public Respiratorio getRespiratorio() {
		return respiratorio;
	}

	public void setRespiratorio(Respiratorio respiratorio) {
		this.respiratorio = respiratorio;
	}

	public Nutricao getNutricao() {
		return nutricao;
	}

	public void setNutricao(Nutricao nutricao) {
		this.nutricao = nutricao;
	}

	public Medicacao getMedicacao() {
		return medicacao;
	}

	public void setMedicacao(Medicacao medicacao) {
		this.medicacao = medicacao;
	}

	public InterOcorrencias getInterOcorrencias() {
		return interOcorrencias;
	}

	public void setInterOcorrencias(InterOcorrencias interOcorrencias) {
		this.interOcorrencias = interOcorrencias;
	}

	public Higiene getHigiene() {
		return higiene;
	}

	public void setHigiene(Higiene higiene) {
		this.higiene = higiene;
	}

	public Eliminacoes getEliminacoes() {
		return eliminacoes;
	}

	public void setEliminacoes(Eliminacoes eliminacoes) {
		this.eliminacoes = eliminacoes;
	}

	public Dor getDor() {
		return dor;
	}

	public void setDor(Dor dor) {
		this.dor = dor;
	}

	public Consultas getConsultas() {
		return consultas;
	}

	public void setConsultas(Consultas consultas) {
		this.consultas = consultas;
	}

	public BemEstar getBemEstar() {
		return bemEstar;
	}

	public void setBemEstar(BemEstar bemEstar) {
		this.bemEstar = bemEstar;
	}

	public Integer getPressaoInitial() {
		return pressaoInitial;
	}

	public void setPressaoInitial(Integer pressaoInitial) {
		this.pressaoInitial = pressaoInitial;
	}

	public Integer getPressaoFinal() {
		return pressaoFinal;
	}

	public void setPressaoFinal(Integer pressaoFinal) {
		this.pressaoFinal = pressaoFinal;
	}

	public String getQtidade() {
		return qtidade;
	}

	public void setQtidade(String qtidade) {
		this.qtidade = qtidade;
	}

	public String getAspecto() {
		return aspecto;
	}

	public void setAspecto(String aspecto) {
		this.aspecto = aspecto;
	}

	public String getOpcao() {
		return opcao;
	}

	public void setOpcao(String opcao) {
		this.opcao = opcao;
	}





	
	
	

}
