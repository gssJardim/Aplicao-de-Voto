package pt.ul.fc.css.project.entities;


import jakarta.annotation.Nonnull;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import pt.ul.fc.css.project.entities.facade.CidadaoDTO;
import pt.ul.fc.css.project.entities.facade.DelegadoEscolhidoDTO;
@Entity
public class DelegadoEscolhido {
	@Id
	@GeneratedValue
	private Long id;
	@ManyToOne
	private Tema tema;
	@ManyToOne
	private Delegado delegado;
	
	public DelegadoEscolhido() {}
	
	public DelegadoEscolhido(@Nonnull Delegado delegado, @Nonnull Tema tema) {
		this.delegado = delegado;
		this.tema = tema;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Tema getTema() {
		return tema;
	}

	public void setTema(Tema tema) {
		this.tema = tema;
	}

	public Delegado getDelegado() {
		return delegado;
	}

	public void setDelegado(Delegado delegado) {
		this.delegado = delegado;
	}
	
	public DelegadoEscolhidoDTO toDTO() {
		DelegadoEscolhidoDTO dto = new DelegadoEscolhidoDTO();
		dto.setId(id);
		dto.setDelegado(delegado.toDTO());
		dto.setTema(tema.toDTO());
		return dto;
	}

	@Override
	public String toString() {
		return "DelegadoEscolhido [id=" + id + ", tema=" + tema + ", delegado=" + delegado + "]";
	}
}