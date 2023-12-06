package pt.ul.fc.css.project.controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pt.ul.fc.css.project.catalogos.CatalogoProjetoLei;
import pt.ul.fc.css.project.entities.ProjetoLei;
import pt.ul.fc.css.project.entities.facade.ProjetoLeiDTO;
import pt.ul.fc.css.project.handlers.EscolherDelegadoHandler;
import pt.ul.fc.css.project.handlers.FecharProjetoLeiExpiradoHandler;
import pt.ul.fc.css.project.handlers.exceptions.DuplicatedActionException;
import pt.ul.fc.css.project.handlers.exceptions.EmptyFieldsException;
import pt.ul.fc.css.project.handlers.exceptions.EmptyRepositoryException;
import pt.ul.fc.css.project.handlers.exceptions.InvalidActionException;
import pt.ul.fc.css.project.handlers.exceptions.NewEntityException;
import pt.ul.fc.css.project.repositories.ProjetoLeiRepository;

@RestController
@RequestMapping("/api")
public class ProjetoLeiController {
	
	private static final Logger log = LoggerFactory.getLogger(ProjetoLeiController.class);

	@Autowired
	private CatalogoProjetoLei catProjLei;
	
	@Autowired
	private FecharProjetoLeiExpiradoHandler fechProLeiExp;

	@Autowired
	private ProjetoLeiRepository projetoLeiRepository;
	
	@Scheduled(fixedRate = 1000)
	public void scheduleFixedRateTask() throws EmptyRepositoryException {
		System.out.println("Checking for expired projects...");
		fechProLeiExp.fecharProjetosLei();
	}
	 
	@GetMapping("/projeto")
	public Optional<ProjetoLeiDTO> getProjetoLei(@NonNull @RequestBody ProjetoLeiDTO projetoLei) {
		return catProjLei.getProjetoLeiById(projetoLei.getId());
	}
	
	@GetMapping("/projetos")
	public List<ProjetoLeiDTO> getProjetosLei() {
		return catProjLei.getProjetosLei();
	}
	
	@PostMapping("/apresentar-projeto")
	public ProjetoLeiDTO create(@NonNull @RequestBody ProjetoLeiDTO projetoLei) throws NewEntityException, EmptyFieldsException, InvalidActionException {
		log.info("nº de projetos: " + projetoLeiRepository.count());
		ProjetoLeiDTO p = catProjLei.apresentarProjetoLei(projetoLei.getTitulo(), projetoLei.getTexto(), 
				projetoLei.getAnexoPDF(), projetoLei.getDataValidade(), projetoLei.getTema().getId(), 
				projetoLei.getProponente().getId());
		//log.info("apos: " + p);
		return p;
	}
	
	@PatchMapping("/projeto/apoiar")
	ResponseEntity<?> apoiarProjetoLei(@NonNull @RequestParam Long cidadaoId, @NonNull @RequestParam Long projetoLeiId) throws EmptyFieldsException, DuplicatedActionException, InvalidActionException {
		Optional<ProjetoLeiDTO> projetoLei = catProjLei.apoiarProjetoLei(cidadaoId, projetoLeiId);
		if(projetoLei.isPresent()) {
			log.info(projetoLei.get().toString());
			return ResponseEntity.ok().body(projetoLei.get());
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/projetosnaoexpirados")
	public List<ProjetoLeiDTO> getProjetosLeiNaoExpirados() throws EmptyRepositoryException {
		return catProjLei.getProjetosLeiNaoExpirados();
	}
}
