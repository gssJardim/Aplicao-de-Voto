package pt.ul.fc.css.project.entities.facade;

import java.util.Objects;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Embeddable;

@Component
public class TemaDTO {
	
	private Long id;
	
	private String nome;
	
	public TemaDTO() {}
	//@JsonProperty("tema") @JsonProperty("nome")
	public TemaDTO(Long id, String nome) {
		this.id = id;
		this.nome = nome;
	}

	public TemaDTO(Long id) {
		this.id=id;
	}
	
	public Long getId() {
        return id;
    }

    public void setId(Long id) {
    	if (id != null) {
            this.id = id;
        }
    }
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
}
