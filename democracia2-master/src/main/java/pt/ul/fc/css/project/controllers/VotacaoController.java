package pt.ul.fc.css.project.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pt.ul.fc.css.project.catalogos.CatalogoVotacoes;
import pt.ul.fc.css.project.entities.enumerated.TipoDeVoto;
import pt.ul.fc.css.project.entities.facade.VotacaoDTO;
import pt.ul.fc.css.project.handlers.FecharUmaVotacaoHandler;
import pt.ul.fc.css.project.handlers.exceptions.DuplicatedActionException;
import pt.ul.fc.css.project.handlers.exceptions.EmptyFieldsException;
import pt.ul.fc.css.project.handlers.exceptions.EmptyRepositoryException;
import pt.ul.fc.css.project.handlers.exceptions.InvalidActionException;

@RestController
@RequestMapping("/api")
public class VotacaoController {
	@Autowired
	private CatalogoVotacoes catalogoVotacoes;
	
	@Autowired
	private FecharUmaVotacaoHandler fechVot;

//	@Scheduled(fixedRate = 1000)
//	public void scheduleFixedRateTask() throws EmptyRepositoryException {
//		System.out.println("Checking for expired projects...");
//		fechVot.fecharVotacao();
//	}
//	@PostMapping("/votacao")
//	public Votacao postVotacao(@NonNull @RequestBody Votacao votacao) {
//		Long projetoLeiID = votacao.getProjetoLei().getId();
//		boolean projetoLeiExists = projetoLeiRepository.existsById(projetoLeiID);
//		if(!projetoLeiExists) {
//			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Projeto Lei not found");
//		}
//		Long votoPublicoID = votacao.getVotoPublico().getId();
//		boolean votoPublicoExists = votoPublicoRepository.existsById(votoPublicoID);
//		if(!votoPublicoExists) {
//			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Voto Publico not found");
//		}
//		Votacao saved = votacaoRepository.save(votacao);
//		return saved;
//	}
	
	@GetMapping("/votacoes")
	public List<VotacaoDTO> getVotacoes(){
		return catalogoVotacoes.getListaVotacoes();
	}
	
	
	@PatchMapping("/votacao/votar")
	ResponseEntity<?> votar(@NonNull @RequestParam Long cidadaoId, @NonNull @RequestParam Long votacaoId, @RequestParam TipoDeVoto tipoDeVoto) throws EmptyFieldsException, DuplicatedActionException, InvalidActionException {
		Optional<VotacaoDTO> votacao = catalogoVotacoes.votarNumaProposta(cidadaoId, votacaoId, tipoDeVoto);
		if(votacao.isPresent()) {
			return ResponseEntity.ok().body(votacao.get());
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}
