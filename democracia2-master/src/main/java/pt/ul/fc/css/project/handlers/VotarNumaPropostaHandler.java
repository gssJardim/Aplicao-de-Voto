package pt.ul.fc.css.project.handlers;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.persistence.EntityNotFoundException;
import pt.ul.fc.css.project.entities.Cidadao;
import pt.ul.fc.css.project.entities.Votacao;
import pt.ul.fc.css.project.entities.VotoPrivado;
import pt.ul.fc.css.project.entities.enumerated.EstadoVotacao;
import pt.ul.fc.css.project.entities.enumerated.TipoDeVoto;
import pt.ul.fc.css.project.entities.facade.VotacaoDTO;
import pt.ul.fc.css.project.handlers.exceptions.DuplicatedActionException;
import pt.ul.fc.css.project.handlers.exceptions.EmptyFieldsException;
import pt.ul.fc.css.project.handlers.exceptions.InvalidActionException;
import pt.ul.fc.css.project.repositories.CidadaoRepository;
import pt.ul.fc.css.project.repositories.VotacaoRepository;
import pt.ul.fc.css.project.repositories.VotoPrivadoRepository;

@Component
public class VotarNumaPropostaHandler {
	@Autowired
	private CidadaoRepository cidadaoRepository;
	
	@Autowired
	private VotacaoRepository votacaoRepository;
	
	@Autowired
	private VotoPrivadoRepository votoPrivadoRepository;
	
	private static final Logger log = LoggerFactory.getLogger(VotarNumaPropostaHandler.class);

	
	public Optional<VotacaoDTO> votarComoCidadao(Long cidadaoId, Long votacaoId, TipoDeVoto tipoDeVoto) throws EmptyFieldsException, DuplicatedActionException, InvalidActionException{
		if(tipoDeVoto.equals(null)) {
			throw new EmptyFieldsException("Tipo de votacao is a required field");
		}
		Optional<Cidadao> c = cidadaoRepository.findById(cidadaoId);
		if(c.isPresent()) {
			Cidadao cidadao = c.get();
			log.info("id " + String.valueOf(votacaoId));
			Optional<Votacao> v = votacaoRepository.findById(votacaoId);
			if(v.isPresent()) {
				Votacao votacao = v.get();
				if(votacao.getCidadaosQueJaVotaram().contains(cidadao)) {
					throw new DuplicatedActionException("Cidadao já votou nesta votacao!");
				}
				if(!votacao.getEstadoVotacao().equals(EstadoVotacao.ABERTO)) {
					throw new InvalidActionException("Votacao já se encontra fechada!");
				}
				VotoPrivado votoCidadao = new VotoPrivado();
				votoCidadao.setTipoDeVoto(tipoDeVoto);
				votoPrivadoRepository.save(votoCidadao);
				
				votacao.addCidadao(cidadao);
				if(tipoDeVoto.equals(TipoDeVoto.FAVOR))
					votacao.incAFavor();
				else
					votacao.incContra();
				votacaoRepository.save(votacao);
				return v.map(votacaoAtualizado->votacaoAtualizado.toDTO());
			}else {
				throw new EntityNotFoundException("Votacao nao foi encontrado");
			}
		}else {
			throw new EntityNotFoundException("Cidadao não foi encontrado");
		}
	}

//	public void votarComoDelegado(Long delegadoId, Long votacaoId, TipoDeVoto tipoDeVoto){
//		if(tipoDeVoto.equals(null)) {
//			//throw new EmptyFieldsException("Tipo de votacao is a required field");
//		}
//		Optional<Votacao> possivelVotacao = votacaoRepository.findById(votacaoId);
//		if(possivelVotacao.isPresent()) {
//			Votacao votacao = possivelVotacao.get();
//			Optional<Delegado> possivelDelegado = delegadoRepository.findById(delegadoId);
//			
//			if(possivelDelegado.isPresent()) {
//				Delegado delegado = possivelDelegado.get();
//				
//				for (VotoPublico votoPublico : votacao.getVotosDeDelegados()) {
//					if(votoPublico.getDelegado().equals(delegado)) {
//						//throw new DuplicatedActionException("Delegado já votou nesta votacao!");
//					}
//				}
//				if(!votacao.getEstadoVotacao().equals(EstadoVotacao.ABERTO)) {
//					//throw new InvalidActionException("Votacao já se encontra fechada!");
//				}
//				VotoPublico votoDelegado = new VotoPublico();
//				votoDelegado.setTipoDeVoto(tipoDeVoto);
//				votoDelegado.setDelegado(delegado);
//				votoPublicoRepository.save(votoDelegado);
//				
//				votacao.addVotoPublico(votoDelegado);
//				if(tipoDeVoto.equals(TipoDeVoto.FAVOR))
//					votacao.incAFavor();
//				else
//					votacao.incContra();
//				votacaoRepository.save(votacao);
//			}
//		}
//		//throw new ErrorException("mensagem");
//	}
}
