package pt.ul.fc.css.project.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import pt.ul.fc.css.project.entities.enumerated.EstadoVotacao;
import pt.ul.fc.css.project.entities.enumerated.TipoDeVoto;
import pt.ul.fc.css.project.entities.facade.VotacaoDTO;


@Entity
public class Votacao {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne //onetoone ou extends?
	private ProjetoLei projetoLei;
//	@OneToOne
//	private VotoPublico votoProponente;
//	@OneToOne
//	private Tema tema;
	@Column(name = "local_date_time" , columnDefinition = "TIMESTAMP")
	private LocalDate dataFecho;
//	@ManyToOne
//	private Delegado proponente;
	@OneToMany(fetch = FetchType.EAGER)
	private Set<Cidadao> cidadaosQueJaVotaram;
	@OneToMany(fetch = FetchType.EAGER)
	private Set<VotoPublico> votosDeDelegados;
	private int aFavor;
	private int econtra;
	@Enumerated
	private EstadoVotacao estadoVotacao;

	public Votacao() {}

	public Votacao(ProjetoLei projetoLei, VotoPublico votoProponente) {
		this.projetoLei = projetoLei;
		//this.tema = projetoLei.getTema();
		this.dataFecho = projetoLei.getDataValidade();
		//this.proponente = projetoLei.getProponente();
		this.cidadaosQueJaVotaram = new HashSet<>();
		this.votosDeDelegados = new HashSet<>();
		this.votosDeDelegados.add(votoProponente);
		this.aFavor = 1;
		this.econtra = 0;
		this.estadoVotacao = EstadoVotacao.ABERTO;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public ProjetoLei getProjetoLei() {
		return projetoLei;
	}

	
//	public VotoPublico getVotoPublico() {
//		return votoProponente;
//	}


	public void setProjetoLei(ProjetoLei projetoLei) {
		this.projetoLei = projetoLei;
	}


//	public Tema getTema() {
//		return this.projetoLei.getTema();
//	}


//	public void setTema(Tema tema) {
//		this.tema = tema;
//	}


	public LocalDate getDataFecho() {
		return dataFecho;
	}


	public void setDataFecho(LocalDate dataFecho) {
		this.dataFecho = dataFecho;
	}


//	public Delegado getProponente() {
//		return this.projetoLei.getProponente();
//	}


//	public void setProponente(Delegado proponente) {
//		this.proponente = proponente;
//	}


	public Set<Cidadao> getCidadaosQueJaVotaram() {
		return cidadaosQueJaVotaram;
	}


	public void setCidadaosQueJaVotaram(Set<Cidadao> cidadaosQueJaVotaram) {
		this.cidadaosQueJaVotaram = cidadaosQueJaVotaram;
	}


	public Set<VotoPublico> getVotosDeDelegados() {
		return votosDeDelegados;
	}


	public void setVotosDeDelegados(Set<VotoPublico> votosDeDelegados) {
		this.votosDeDelegados = votosDeDelegados;
	}


	public int getaFavor() {
		return aFavor;
	}


	public void setaFavor(int aFavor) {
		this.aFavor = aFavor;
	}


	public int getEcontra() {
		return econtra;
	}


	public void setEcontra(int econtra) {
		this.econtra = econtra;
	}


	public EstadoVotacao getEstadoVotacao() {
		return estadoVotacao;
	}


	public void setEstadoVotacao(EstadoVotacao estadoVotacao) {
		this.estadoVotacao = estadoVotacao;
	}

	public void addVotoPublico(VotoPublico votoDelegado) {
		this.votosDeDelegados.add(votoDelegado);
		
	}
	
	public void addCidadao(Cidadao cidadaoCorrente) {
		this.cidadaosQueJaVotaram.add(cidadaoCorrente);
		
	}

	public void incAFavor() {
		this.aFavor ++;
	}


	public void incContra() {
		this.econtra ++;
	}
	
	public void contarVotosPublicos(VotoPublico votosPublico) {
		if(votosPublico.getTipoDeVoto().equals(TipoDeVoto.FAVOR))
			aFavor += votosPublico.getValor();
		else
			econtra += votosPublico.getValor();
	}

	
	public VotacaoDTO toDTO() {
		VotacaoDTO dto = new VotacaoDTO();
		dto.setId(id);
		dto.setProjetoLeiDTO(projetoLei.toDTO());
		dto.setDataFecho(dataFecho);
		dto.setaFavor(aFavor);
		dto.setEcontra(econtra);
		dto.setEstadoVotacao(estadoVotacao);
		dto.setVotosDeDelegado(votosDeDelegados.stream().map(v -> v.toDTO()).collect(Collectors.toSet()));
		return dto;
	}


	@Override
	public int hashCode() {
		return Objects.hash(aFavor, cidadaosQueJaVotaram, dataFecho, econtra, estadoVotacao, id, projetoLei,
				votosDeDelegados);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Votacao other = (Votacao) obj;
		return aFavor == other.aFavor && Objects.equals(cidadaosQueJaVotaram, other.cidadaosQueJaVotaram)
				&& Objects.equals(dataFecho, other.dataFecho) && econtra == other.econtra
				&& estadoVotacao == other.estadoVotacao && id == other.id
				&& Objects.equals(projetoLei, other.projetoLei)
				&& Objects.equals(votosDeDelegados, other.votosDeDelegados);
	}

	@Override
	public String toString() {
		return "Votacao [projetoLei=" + projetoLei + ", votosDeDelegados=" + votosDeDelegados + ", aFavor=" + aFavor
				+ ", econtra=" + econtra + ", estadoVotacao=" + estadoVotacao + "]";
	}
}

