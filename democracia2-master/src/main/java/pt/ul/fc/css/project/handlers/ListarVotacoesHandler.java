package pt.ul.fc.css.project.handlers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pt.ul.fc.css.project.entities.Votacao;
import pt.ul.fc.css.project.entities.enumerated.EstadoVotacao;
import pt.ul.fc.css.project.entities.facade.VotacaoDTO;
import pt.ul.fc.css.project.handlers.exceptions.EmptyRepositoryException;
import pt.ul.fc.css.project.repositories.VotacaoRepository;
@Component
public class ListarVotacoesHandler {
	@Autowired
	private VotacaoRepository votacaoRepository;
	
	
	public List<VotacaoDTO> listarVotacoesEmCurso() throws EmptyRepositoryException{
		ArrayList<VotacaoDTO> votacoesEmCurso = new ArrayList<>();
		
		if(votacaoRepository.count() == 0)
			throw new EmptyRepositoryException("Nao há votações!");
		for(Votacao votacao : votacaoRepository.findAll()) {
			if(votacao.getEstadoVotacao().equals(EstadoVotacao.ABERTO)) {
				VotacaoDTO votacaoDTO = votacao.toDTO();
				votacoesEmCurso.add(votacaoDTO);
			}
		}
		return votacoesEmCurso;
	}
}
