package pt.ul.fc.css.project.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.ManyToOne;
import pt.ul.fc.css.project.entities.enumerated.TipoDeVoto;


@Inheritance
public interface Voto {

	public abstract TipoDeVoto getTipoDeVoto();

	public abstract void setTipoDeVoto(TipoDeVoto tipoDeVoto);
}
