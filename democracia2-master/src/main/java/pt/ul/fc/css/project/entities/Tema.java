package pt.ul.fc.css.project.entities;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeId;

import jakarta.annotation.Nonnull;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import pt.ul.fc.css.project.entities.facade.TemaDTO;

@Entity
public class Tema {

	@Id
    @GeneratedValue
    @JsonProperty("tema")
    private Long id;

	@Nonnull
	private String nome;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private Tema supTema;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Set<Tema> subTemas;
	
	
	public Tema() {
		this.nome = "Geral";
		this.subTemas = new HashSet<>();
	}
	
	public Tema(String nome, Tema supTema) {
		this.nome = nome;
		this.supTema = supTema;
		this.supTema.addSubTema(this);
		this.subTemas = new HashSet<>();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Tema getSupTema() {
		return supTema;
	}

	public void setSupTema(Tema supTema) {
		this.supTema = supTema;
		this.supTema.addSubTema(this);
	}

	public Set<Tema> getSubTemas() {
		return subTemas;
	}

	public void setSubTemas(Set<Tema> subTemas) {
		this.subTemas = subTemas;
	}
	
	public void addSubTema(Tema subTema) {
		this.subTemas.add(subTema);
	}
	
	public boolean isTemaMaisGeral() {
		return this.supTema.equals(null);
	}
	
	public TemaDTO toDTO() {
		TemaDTO dto = new TemaDTO();
		dto.setId(id);
		dto.setNome(nome);
		return dto;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, nome);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tema other = (Tema) obj;
		return Objects.equals(id, other.id) && Objects.equals(nome, other.nome)
				&& Objects.equals(subTemas, other.subTemas);
	}

	@Override
	public String toString() {
		return "Tema [id=" + id + ", nome=" + nome + ", supTema=" + supTema + "]";
	}
}

