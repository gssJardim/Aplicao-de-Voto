package pt.ul.fc.css.project.entities;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import pt.ul.fc.css.project.entities.enumerated.TipoDeVoto;
import pt.ul.fc.css.project.entities.facade.VotoPublicoDTO;
@Entity
public class VotoPublico implements Voto{
	@Id
	@GeneratedValue
	private Long id;
	@ManyToOne
	private Delegado delegado;
	private int valor;
	@Enumerated 
	private TipoDeVoto tipoDeVoto;
	
	public VotoPublico() {
		this.valor = 1;
	}
	
	public VotoPublico(Delegado delegado, TipoDeVoto tipoDeVoto) {
		this.tipoDeVoto = tipoDeVoto;
		this.delegado = delegado;
		this.valor = 0;
	}
	
	public VotoPublico(Delegado proponente) { //CONSTRUTOR QUANDO UM PROJETOLEI CHEGA AOS 10000 APOIOS,
		this.tipoDeVoto = TipoDeVoto.FAVOR;           //EH CRIADA UMA VOTACAO COM O PARAMETRO VOTODOPROPONENTE
		this.delegado = proponente;
		this.valor = 0;
	}
	
	
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public Delegado getDelegado() {
		return delegado;
	}


	public void setDelegado(Delegado delegado) {
		this.delegado = delegado;
	}
	
	
	public int getValor() {
		return valor;
	}

	public void setValor(int valor) {
		this.valor = valor;
	}
	
	public void inc() {
		this.valor ++;
		
	}
	
	public VotoPublicoDTO toDTO() {
		VotoPublicoDTO dto = new VotoPublicoDTO();
		dto.setId(id);
		dto.setTipoDeVoto(tipoDeVoto);
		dto.setValor(valor);
		dto.setDelegado(delegado.toDTO());
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
		VotoPublico other = (VotoPublico) obj;
		return id == other.id;
	}

	@Override
	public String toString() {
		return "VotoPublico [delegado=" + delegado + ", valor=" + valor + ", tipoDeVoto=" + tipoDeVoto + "]";
	}

	@Override
	public TipoDeVoto getTipoDeVoto() {
		return this.tipoDeVoto;
	}

	@Override
	public void setTipoDeVoto(TipoDeVoto tipoDeVoto) {
		this.tipoDeVoto = tipoDeVoto;
	}
	
	
}
