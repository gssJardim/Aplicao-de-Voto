package pt.ul.fc.css.project.catalogos;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pt.ul.fc.css.project.entities.Cidadao;
import pt.ul.fc.css.project.entities.Delegado;
import pt.ul.fc.css.project.entities.ProjetoLei;
import pt.ul.fc.css.project.entities.Votacao;
import pt.ul.fc.css.project.entities.Voto;
import pt.ul.fc.css.project.entities.VotoPrivado;
import pt.ul.fc.css.project.entities.VotoPublico;
import pt.ul.fc.css.project.entities.enumerated.EstadoProjetoLei;
import pt.ul.fc.css.project.entities.enumerated.EstadoVotacao;
import pt.ul.fc.css.project.entities.enumerated.TipoDeVoto;
import pt.ul.fc.css.project.entities.facade.ProjetoLeiDTO;
import pt.ul.fc.css.project.entities.facade.VotacaoDTO;
import pt.ul.fc.css.project.entities.facade.VotoPublicoDTO;
import pt.ul.fc.css.project.handlers.FecharUmaVotacaoHandler;
import pt.ul.fc.css.project.handlers.VotarNumaPropostaHandler;
import pt.ul.fc.css.project.handlers.exceptions.DuplicatedActionException;
import pt.ul.fc.css.project.handlers.exceptions.EmptyFieldsException;
import pt.ul.fc.css.project.handlers.exceptions.InvalidActionException;
import pt.ul.fc.css.project.repositories.VotacaoRepository;

@Component
public class CatalogoVotacoes {
	@Autowired
	private VotacaoRepository votacaoRepository;
	
	@Autowired
	private FecharUmaVotacaoHandler fecharUmaVotacaoHandler;
	
	@Autowired
	private VotarNumaPropostaHandler votarNumaPropostaHandler;
	
	
	public Optional<VotacaoDTO> getVotacaoById(long id) {
		return votacaoRepository.findById(id).map(v -> v.toDTO());
	}
	
	public ArrayList<VotacaoDTO> getListaVotacoes() {
		ArrayList<VotacaoDTO> votacoesEmCurso = new ArrayList<>();
		for (Votacao votacao : votacaoRepository.findAll()) {
			VotacaoDTO votacaoDTO = votacao.toDTO();
			
			if(votacaoDTO.getEstadoVotacao().equals(EstadoVotacao.ABERTO)){
				votacoesEmCurso.add(votacaoDTO);
			}
		}
		return votacoesEmCurso;
	}
	
	
	public Optional<VotacaoDTO> votarNumaProposta(Long cidadaoId, Long votacaoId, TipoDeVoto tipoDeVoto) throws EmptyFieldsException, DuplicatedActionException, InvalidActionException{
		return votarNumaPropostaHandler.votarComoCidadao(cidadaoId, votacaoId, tipoDeVoto);
	}
	
	public void fecharVotacoes() throws EmptyFieldsException {
		fecharUmaVotacaoHandler.fecharVotacoes();
		
	}

}
