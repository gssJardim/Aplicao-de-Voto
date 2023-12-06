package pt.ul.fc.css.project.catalogos;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pt.ul.fc.css.project.entities.Cidadao;
import pt.ul.fc.css.project.entities.Delegado;
import pt.ul.fc.css.project.entities.DelegadoEscolhido;
import pt.ul.fc.css.project.entities.Tema;
import pt.ul.fc.css.project.entities.facade.DelegadoEscolhidoDTO;
import pt.ul.fc.css.project.handlers.ApresentarProjetoLeiHandler;
import pt.ul.fc.css.project.handlers.EscolherDelegadoHandler;
import pt.ul.fc.css.project.repositories.DelegadoEscolhidoRepository;
@Component
public class CatalogoDelegadosEscolhidos {
	@Autowired
	private DelegadoEscolhidoRepository delegadoEscolhidoRepository;
	
	@Autowired
	private EscolherDelegadoHandler escolherDelegadoHandler;
	

	public List<DelegadoEscolhidoDTO> getDelegadosEscolhidos() {
		return delegadoEscolhidoRepository.findAll().stream().map(de -> de.toDTO()).collect(Collectors.toList());
	}

	public Optional<DelegadoEscolhidoDTO> getDelegadoEscolhidoById(long id) {
		return this.delegadoEscolhidoRepository.findById(id).map(de -> de.toDTO());
	}
	
	public DelegadoEscolhidoDTO getEscolherDelegados(Long cidadaoId, Long delegadoId, Long temaId) throws Exception{
		return escolherDelegadoHandler.escolherDelegado(cidadaoId, delegadoId, temaId);
	}
}
