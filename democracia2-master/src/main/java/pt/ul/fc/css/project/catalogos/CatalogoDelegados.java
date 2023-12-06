package pt.ul.fc.css.project.catalogos;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import pt.ul.fc.css.project.entities.Cidadao;
import pt.ul.fc.css.project.entities.Delegado;
import pt.ul.fc.css.project.entities.DelegadoEscolhido;
import pt.ul.fc.css.project.entities.ProjetoLei;
import pt.ul.fc.css.project.entities.enumerated.TipoDeVoto;
import pt.ul.fc.css.project.entities.facade.CidadaoDTO;
import pt.ul.fc.css.project.entities.facade.DelegadoDTO;
import pt.ul.fc.css.project.entities.facade.DelegadoEscolhidoDTO;
import pt.ul.fc.css.project.entities.facade.ProjetoLeiDTO;
import pt.ul.fc.css.project.entities.facade.TemaDTO;
import pt.ul.fc.css.project.handlers.ApresentarProjetoLeiHandler;
import pt.ul.fc.css.project.handlers.EscolherDelegadoHandler;
import pt.ul.fc.css.project.handlers.VotarNumaPropostaHandler;
import pt.ul.fc.css.project.repositories.CidadaoRepository;
import pt.ul.fc.css.project.repositories.DelegadoRepository;
@Component
public class CatalogoDelegados {
	@Autowired
	private DelegadoRepository delegadoRepository;
	@Autowired
	private CidadaoRepository cidadaoRepository;
	@Autowired
	private ApresentarProjetoLeiHandler apresentarProjetoLeiHandler;
	
	@Autowired
	private EscolherDelegadoHandler escolherDelegadoHandler;
	
	@Autowired
	private VotarNumaPropostaHandler votarNumaPropostaHandler;
	

	public Optional<DelegadoDTO> getDelegado(Long id) {
		return delegadoRepository.findById(id).map(d -> d.toDTO());
	}
	
	public List<DelegadoDTO> getDelegados(){
		ArrayList<DelegadoDTO> delegadosDTO = new ArrayList<>();
		for (Delegado delegado : delegadoRepository.findAll()) {
			DelegadoDTO delegadoDTO = delegado.toDTO();
			delegadosDTO.add(delegadoDTO);
		}
		return delegadosDTO;
	}
	

	public DelegadoDTO createDelegado(String nome, String ncc) {
		Delegado novoDelegado = new Delegado();
		novoDelegado.setNome(nome);
		novoDelegado.setnCartaoCidadao(ncc);
		delegadoRepository.save(novoDelegado);
		return novoDelegado.toDTO();
	}
}
