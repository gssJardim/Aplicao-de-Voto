package pt.ul.fc.css.project.entities;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.ArrayList;

import org.springframework.lang.NonNull;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.OneToMany;
import pt.ul.fc.css.project.entities.facade.CidadaoDTO;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Cidadao {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@NonNull
	protected String nome;
	@NonNull
	protected String nCartaoCidadao;
	//@NonNull
	//private String tokenAutenticacao;
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<DelegadoEscolhido> delegadosEscolhidos;
    
	
	
	public Cidadao() {}
	
	public Cidadao(@NonNull String nome, @NonNull String nCartaoCidadao) {
		this.nome = nome;
		this.nCartaoCidadao = nCartaoCidadao;
	}

	public Long getId() {
		return this.id;
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

	public String getnCartaoCidadao() {
		return nCartaoCidadao;
	}

	public void setnCartaoCidadao(String nCartaoCidadao) {
		this.nCartaoCidadao = nCartaoCidadao;
	}
	
	public Set<DelegadoEscolhido> getDelegadosEscolhidos() {
		return delegadosEscolhidos;
	}

	public void setDelegadosEscolhidos(Set<DelegadoEscolhido> delegadosEscolhidos) {
		this.delegadosEscolhidos = delegadosEscolhidos;
	}
	
	public void addDelegadoEscolhido(DelegadoEscolhido delegado) {
		if(!jaEscolheuEsteDelegado(delegado))
			delegadosEscolhidos.add(delegado);
	}	
	
	private boolean jaEscolheuEsteDelegado(DelegadoEscolhido delegado) {
		for (DelegadoEscolhido delegadoEscolhido : delegadosEscolhidos) {
			if(delegadoEscolhido.equals(delegado))
				return true;
		}
		return false;
	}
	
	public CidadaoDTO toDTO() {
		CidadaoDTO dto = new CidadaoDTO();
		dto.setId(this.id);
		dto.setNome(this.nome);
		dto.setnCartaoCidadao(this.nCartaoCidadao);
		return dto;
	}
	
	@Override
	public String toString() {
		return "Citizen[" +
				"id=" + id + ", " +
				"name=" + nome + ", " +
				"nif=" + nCartaoCidadao + ']';
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, nCartaoCidadao, nome);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cidadao other = (Cidadao) obj;
		return id == other.id
				&& Objects.equals(nCartaoCidadao, other.nCartaoCidadao) && Objects.equals(nome, other.nome);
	}
}