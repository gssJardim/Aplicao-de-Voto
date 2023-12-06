package pt.ul.fc.css.project.entities.facade;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Embeddable;
import pt.ul.fc.css.project.entities.enumerated.EstadoProjetoLei;

@Component
@Embeddable
public class ProjetoLeiDTO {
	
	private Long id;
	private String titulo;
	private String texto;
	private String anexoPDF;
	private LocalDate dataValidade;
	private TemaDTO tema;
	private DelegadoDTO proponente;
	private int nApoiantes;
	private EstadoProjetoLei estado;
	private CidadaoDTO cidadao;
	private ProjetoLeiDTO projetoLei;
	
	public ProjetoLeiDTO() {}
	

	public Long getId() {
		return id;
	}

	
	public void setId(Long id) {
		this.id = id;
	}


	public String getTitulo() {
		return titulo;
	}


	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}


	public String getTexto() {
		return texto;
	}


	public void setTexto(String texto) {
		this.texto = texto;
	}


	public String getAnexoPDF() {
		return anexoPDF;
	}
	
	public CidadaoDTO getCidadao() {
        return cidadao;
    }

    public void setCidadao(CidadaoDTO cidadao) {
        this.cidadao = cidadao;
    } 
    
    public ProjetoLeiDTO getProjetoLei() {
        return projetoLei;
    }

    public void setProjetoLei(ProjetoLeiDTO projetoLei) {
        this.projetoLei = projetoLei;
    }


	public void setAnexoPDF(String anexoPDF) {
		this.anexoPDF = anexoPDF;
	}


	public LocalDate getDataValidade() {
		return dataValidade;
	}


	public void setDataValidade(LocalDate dataValidade) {
		this.dataValidade = dataValidade;
	}

	public TemaDTO getTema() {
		return tema;
	}

	public void setTema(TemaDTO tema) {
		this.tema = tema;
	}

	public DelegadoDTO getProponente() {
		return proponente;
	}

	public void setProponente(DelegadoDTO proponente) {
		this.proponente = proponente;
	}


	public int getnApoiantes() {
		return nApoiantes;
	}


	public void setnApoiantes(int nApoiantes) {
		this.nApoiantes = nApoiantes;
	}


	public EstadoProjetoLei getEstado() {
		return estado;
	}


	public void setEstado(EstadoProjetoLei estado) {
		this.estado = estado;
	}
	
	@JsonProperty("tema")
	private void unpackNested(TemaDTO tema) {
	    this.tema = tema;
	}
	
	@JsonProperty("tema")
	private void setTema(TemaDTO tema, Long temaId) {
		tema.setId(temaId);
	}
	
	@JsonProperty("proponente")
	private void unpackProponente(DelegadoDTO proponente) {
	    this.proponente = proponente;
	}
	
	@JsonProperty("proponente")
	private void setProponente(DelegadoDTO delegado, Long delegadoId) {
		delegado.setId(delegadoId);
	}

	@Override
	public String toString() {
		return "ProjetoLeiDTO [id=" + id + ", titulo=" + titulo + ", texto=" + texto + ", anexoPDF=" + anexoPDF
				+ ", dataValidade=" + dataValidade + ", tema=" + tema + ", proponente=" + proponente + ", nApoiantes="
				+ nApoiantes + ", estado=" + estado + "]";
	}
}
