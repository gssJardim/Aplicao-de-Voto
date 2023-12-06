package pt.ul.fc.css.project.entities.facade;

import java.util.Set;

import org.springframework.stereotype.Component;


import pt.ul.fc.css.project.entities.DelegadoEscolhido;

@Component
public class CidadaoDTO {
	private Long id;
	protected String nome;
	protected String nCartaoCidadao;
	//@NonNull
	//private String tokenAutenticacao;
    
	
	public CidadaoDTO() {}

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
}
