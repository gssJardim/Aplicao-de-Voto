package pt.ul.fc.css.project.entities;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.springframework.lang.NonNull;

import java.util.HashSet;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import pt.ul.fc.css.project.entities.enumerated.TipoDeVoto;
import pt.ul.fc.css.project.entities.facade.CidadaoDTO;
import pt.ul.fc.css.project.entities.facade.DelegadoDTO;
@Entity
public class Delegado extends Cidadao{
	@Id
	@GeneratedValue
	private Long id;
//	@OneToMany(fetch = FetchType.EAGER)
//	private Set<ProjetoLei> projetosLei;
	
	
	public Delegado(){
		super();
	}

	
	public Delegado(String nome, String numCartaoCidadao) {
		super(nome, numCartaoCidadao);
	}
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

//	public Set<ProjetoLei> getProjetosLei() {
//		return projetosLei;
//	}
//
//	public void setProjetosLei(Set<ProjetoLei> projetosLei) {
//		this.projetosLei = projetosLei;
//	}
//
//	public void addProjetoLei(ProjetoLei novoProjetoLei) {
//		projetosLei.add(novoProjetoLei);
//	}
	
	
	public DelegadoDTO toDTO() {
		DelegadoDTO dto = new DelegadoDTO();
		dto.setId(id);
		dto.setNome(nome);
		dto.setnCartaoCidadao(nCartaoCidadao);
		return dto;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Delegado other = (Delegado) obj;
		return id == other.id;
	}

	@Override
	public String toString() {
		return "Delegado [nome=" + nome + "]";
	}
}
