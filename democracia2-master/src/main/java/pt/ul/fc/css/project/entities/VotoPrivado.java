package pt.ul.fc.css.project.entities;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import pt.ul.fc.css.project.entities.enumerated.TipoDeVoto;
import pt.ul.fc.css.project.entities.facade.VotoPublicoDTO;

@Entity
public class VotoPrivado implements Voto{
	@Id
	@GeneratedValue
	private Long id;
	@Enumerated 
	private TipoDeVoto tipoDeVoto;
	
	public VotoPrivado() {}
	
	public VotoPrivado(TipoDeVoto tipoDeVoto) {
		this.tipoDeVoto = tipoDeVoto;
	}

	public Long getId() {
		return this.id;
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
		VotoPrivado other = (VotoPrivado) obj;
		return id == other.id;
	}

	@Override
	public String toString() {
		return "VotoPrivado [id=" + id + ", tipoDeVoto=" + tipoDeVoto + "]";
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
