package pt.ul.fc.css.project.handlers;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.persistence.EntityNotFoundException;
import pt.ul.fc.css.project.entities.Cidadao;
import pt.ul.fc.css.project.entities.Delegado;
import pt.ul.fc.css.project.entities.ProjetoLei;
import pt.ul.fc.css.project.entities.Votacao;
import pt.ul.fc.css.project.entities.VotoPublico;
import pt.ul.fc.css.project.entities.enumerated.EstadoProjetoLei;
import pt.ul.fc.css.project.entities.facade.ProjetoLeiDTO;
import pt.ul.fc.css.project.handlers.exceptions.DuplicatedActionException;
import pt.ul.fc.css.project.handlers.exceptions.InvalidActionException;
import pt.ul.fc.css.project.repositories.CidadaoRepository;
import pt.ul.fc.css.project.repositories.ProjetoLeiRepository;
import pt.ul.fc.css.project.repositories.VotacaoRepository;
import pt.ul.fc.css.project.repositories.VotoPublicoRepository;


@Component
public class ApoiarProjetoLeiHandler {
	@Autowired
	private ProjetoLeiRepository projetoLeiRepository;
	
	@Autowired
	private CidadaoRepository cidadaoRepository;
	
	@Autowired
	private VotacaoRepository votacaoRepository;
	
	@Autowired
	private VotoPublicoRepository votoPublicoRepository;
	
	private static final Logger log = LoggerFactory.getLogger(ApoiarProjetoLeiHandler.class);

	public Optional<ProjetoLeiDTO> apoiarProjetoLei(Long cidadaoId, Long projetoLeiId) throws InvalidActionException, DuplicatedActionException {
		Optional<Cidadao> cidadao = cidadaoRepository.findById(cidadaoId);
		if(cidadao.isPresent()) {
			Cidadao c = cidadao.get();
			Optional<ProjetoLei> projetoLei = projetoLeiRepository.findById(projetoLeiId);
			if(projetoLei.isPresent()){
				ProjetoLei p = projetoLei.get();
				if(p.getApoiantes().contains(c)) {
					throw new DuplicatedActionException("Cidadao já apoiou este projeto");
				}else {
					p.adicionarCidadao(c);
					if(p.getNApoiantes() >= 10000)
						log.info("projeto lei atingiu 10000 apoiantes, a criar votacao...");
						criarVotacao(p, p.getProponente());
					projetoLeiRepository.save(p);
					return projetoLei.map(projetoLeiAtualizado -> projetoLeiAtualizado.toDTO());
				}
			}else {
				throw new EntityNotFoundException("Projeto Lei não encontrado");
			}
		}else {
			throw new EntityNotFoundException("Cidadao não encontrado");
		}
	}

	private void criarVotacao(ProjetoLei p, Delegado proponente) {
		VotoPublico votoProponente = new VotoPublico(proponente);
		votoPublicoRepository.save(votoProponente);
		
		Votacao novaVotacao = new Votacao(p, votoProponente);
		votacaoRepository.save(novaVotacao);
		
		p.setEstado(EstadoProjetoLei.EM_VOTACAO);
	}
}
