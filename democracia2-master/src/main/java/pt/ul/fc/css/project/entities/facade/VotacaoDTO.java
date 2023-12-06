package pt.ul.fc.css.project.entities.facade;

import java.time.LocalDate;
import java.util.Set;

import org.springframework.stereotype.Component;

import pt.ul.fc.css.project.entities.enumerated.EstadoVotacao;

@Component
public class VotacaoDTO { 
	
	private Long id;
	private ProjetoLeiDTO projetoLeiDTO;
	private LocalDate dataFecho;
	private Set<VotoPublicoDTO> votosDeDelegados;
	private int aFavor;
	private int eContra;
	private EstadoVotacao estadoVotacao;

	public VotacaoDTO() {
		this.projetoLeiDTO = new ProjetoLeiDTO();
	}
	
	
	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

	public ProjetoLeiDTO getProjetoLeiDTO() {
		return projetoLeiDTO;
	}
	
	public void setProjetoLeiDTO(ProjetoLeiDTO projetoLeiDTO) {
		this.projetoLeiDTO = projetoLeiDTO;
	}

	public LocalDate getDataFecho() {
		return dataFecho;
	}

	public void setDataFecho(LocalDate dataFecho) {
		this.dataFecho = dataFecho;
	}

	public Set<VotoPublicoDTO> getVotosDeDelegados() {
		return this.votosDeDelegados;
	}
	
	public void setVotosDeDelegado(Set<VotoPublicoDTO> votosDeDelegados) {
		this.votosDeDelegados = votosDeDelegados;
	}

	public int getaFavor() {
		return aFavor;
	}
	
	public void setaFavor(int aFavor) {
		this.aFavor = aFavor;
	}

	public int getEcontra() {
		return eContra;
	}

	public void setEcontra(int eContra) {
		this.eContra = eContra;
	}
	
	public EstadoVotacao getEstadoVotacao() {
		return estadoVotacao;
	}
	
	public void setEstadoVotacao(EstadoVotacao estadoVotacao) {
		this.estadoVotacao = estadoVotacao;
	}
}
