package pt.ul.fc.css.project.entities.facade;

public class DelegadoEscolhidoDTO {
	
	private Long id;
	private TemaDTO tema;
	private DelegadoDTO delegado;

	public DelegadoEscolhidoDTO() {}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TemaDTO getTema() {
		return tema;
	}

	public void setTema(TemaDTO tema) {
		this.tema = tema;
	}

	public DelegadoDTO getDelegado() {
		return delegado;
	}

	public void setDelegado(DelegadoDTO delegado) {
		this.delegado = delegado;
	}
}
