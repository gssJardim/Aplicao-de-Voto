package pt.ul.fc.css.project.entities;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import pt.ul.fc.css.project.entities.enumerated.EstadoProjetoLei;
import pt.ul.fc.css.project.entities.facade.ProjetoLeiDTO;

@Entity
public class ProjetoLei {
	@Id
	@GeneratedValue
	private Long id;
	@Nonnull
	private String titulo;
	@Nonnull
	private String texto;
	@Lob
	private String anexoPDF;
	
	@Column(name = "local_date", columnDefinition = "DATE")
	private LocalDate dataValidade;
	
	@ManyToOne(fetch = FetchType.EAGER)
//	@JoinColumn(name = "temaId")
//	@JsonBackReference("tema")
	private Tema tema;
	
	@ManyToOne(fetch = FetchType.EAGER)
//	@JoinColumn(name = "delegadoId")
//	@JsonBackReference("proponente")
	private Delegado proponente;
	
	@ManyToMany(fetch = FetchType.EAGER)
	private Set<Cidadao> apoiantes;
	
	private int nApoiantes;
	
	@Enumerated
	private EstadoProjetoLei estado;
	
	
	public ProjetoLei() {
		this.apoiantes = new HashSet<>();
		this.nApoiantes = 1;
		this.estado = EstadoProjetoLei.ABERTO;
	}
	
	public ProjetoLei(String titulo, String texto, String anexoPDF, LocalDate dataValidade, Tema tema, Delegado proponente) {
		this.titulo = titulo;
		this.texto = texto;
		this.anexoPDF = anexoPDF;
		this.dataValidade = dataValidade;
		this.tema = tema;
		this.proponente = proponente;
		this.apoiantes = new HashSet<>();
		this.nApoiantes = 1;
		this.estado = EstadoProjetoLei.ABERTO;
	}
	
	
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

	public void setAnexoPDF(String anexoPDF) {
		this.anexoPDF = anexoPDF;
	}

	public LocalDate getDataValidade() {
		return dataValidade;
	}

	public void setDataValidade(LocalDate dataValidade) {
		this.dataValidade = dataValidade;
	}

	public Tema getTema() {
		return tema;
	}

	public void setTema(Tema tema) {
		this.tema = tema;
	}

	public Delegado getProponente() {
		return proponente;
	}
	
	public void setProponente(Delegado proponente) {
		this.proponente = proponente;
	}

	public int getNApoiantes() {
		return this.nApoiantes;
	}
	
	public void setNApoiantes(int nApoiantes) {
		this.nApoiantes = nApoiantes;
	}
	
	public Set<Cidadao> getApoiantes() {
		return apoiantes;
	}
	
	public void setApoiantes(Set<Cidadao> apoiantes) {
		this.apoiantes = apoiantes;
	}

	public EstadoProjetoLei getEstado() {
		return estado;
	}

	public void setEstado(EstadoProjetoLei estado) {
		this.estado = estado;
	}
	

	public void adicionarCidadao(Cidadao cidadao) {
		if(!cidadaoJaApoiou(cidadao)) {
			apoiantes.add(cidadao);
			incApoio();
		}
			
	}

	private boolean cidadaoJaApoiou(Cidadao cidadao) {
		for (Cidadao cidadaoapoiante : apoiantes) {
			if(cidadaoapoiante.equals(cidadao))
				return true;
		}
		return false;
	}
	
	public void incApoio() {
		this.nApoiantes ++;
		
	}
	
	public ProjetoLeiDTO toDTO() {
		ProjetoLeiDTO dto = new ProjetoLeiDTO();
		dto.setId(id);
		dto.setTitulo(titulo);
		dto.setTexto(texto);
		dto.setAnexoPDF(anexoPDF);
		dto.setDataValidade(dataValidade);
		dto.setEstado(estado);
		dto.setTema(tema.toDTO());
		dto.setnApoiantes(nApoiantes);
		dto.setProponente(proponente.toDTO());
		return dto;
	}

	@Override
	public int hashCode() {
		return Objects.hash(anexoPDF, nApoiantes, dataValidade, estado, id, proponente, tema, texto, titulo);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProjetoLei other = (ProjetoLei) obj;
		return Objects.equals(anexoPDF, other.anexoPDF)
				&& Objects.equals(dataValidade, other.dataValidade) && nApoiantes == other.nApoiantes && estado == other.estado && id == other.id
				&& Objects.equals(proponente, other.proponente) && Objects.equals(tema, other.tema)
				&& Objects.equals(texto, other.texto) && Objects.equals(titulo, other.titulo);
	}

	@Override
	public String toString() {
		return "ProjetoLei [titulo=" + titulo + ", texto=" + texto + ", anexoPDF=" + anexoPDF + ", dataValidade="
				+ dataValidade + ", tema=" + tema + ", proponente=" + proponente + ", estado=" + estado + "]";
	}
}
