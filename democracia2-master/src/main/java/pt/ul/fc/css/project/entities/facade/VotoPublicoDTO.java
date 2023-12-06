package pt.ul.fc.css.project.entities.facade;

import org.springframework.stereotype.Component;

import pt.ul.fc.css.project.entities.enumerated.TipoDeVoto;

@Component
public class VotoPublicoDTO {
	private Long id;
	private DelegadoDTO delegado;
	private TipoDeVoto tipoDeVoto;
	private int valor;
	
	
	public VotoPublicoDTO() {}

	public VotoPublicoDTO(Long id, DelegadoDTO delegado, TipoDeVoto tipoDeVoto, int valor) {
		super();
		this.id = id;
		this.delegado = delegado;
		this.tipoDeVoto = tipoDeVoto;
		this.valor = valor;
	}


	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

	public DelegadoDTO getDelegado() {
		return delegado;
	}

	public void setDelegado(DelegadoDTO delegado) {
		this.delegado = delegado;
	}

	public TipoDeVoto getTipoDeVoto() {
		return tipoDeVoto;
	}
	
	public void setTipoDeVoto(TipoDeVoto tipoDeVoto) {
		this.tipoDeVoto = tipoDeVoto;
	}


	public int getValor() {
		return valor;
	}


	public void setValor(int valor) {
		this.valor = valor;
	}
}
