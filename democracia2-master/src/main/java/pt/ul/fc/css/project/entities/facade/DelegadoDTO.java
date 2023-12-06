package pt.ul.fc.css.project.entities.facade;

import java.util.Set;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;

import pt.ul.fc.css.project.entities.ProjetoLei;

@Component
public class DelegadoDTO extends CidadaoDTO{

	private Long id;
//	private Set<ProjetoLeiDTO> projetosLei;
	
	
	public DelegadoDTO(){}
	//@JsonProperty("proponente") @JsonProperty("nome")
	public DelegadoDTO(Long id,  String nome){
		this.id = id;
		this.nome = nome;
	}
	
	
	public DelegadoDTO(Long proponenteId) {
		this.id =proponenteId;
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
	
	public String getnCartaoCidadao() {
		return nCartaoCidadao;
	}
	
	public void setnCartaoCidadao(String nCartaoCidadao) {
		this.nCartaoCidadao = nCartaoCidadao;
	}
}
