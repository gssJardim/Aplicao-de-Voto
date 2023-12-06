package pt.ul.fc.css.project.controllers;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mvcexample.business.services.CustomerDTO;
import pt.ul.fc.css.project.catalogos.CatalogoCidadaos;
import pt.ul.fc.css.project.entities.Cidadao;
import pt.ul.fc.css.project.entities.enumerated.TipoDeVoto;
import pt.ul.fc.css.project.entities.facade.CidadaoDTO;
import pt.ul.fc.css.project.entities.facade.DelegadoEscolhidoDTO;
import pt.ul.fc.css.project.entities.facade.VotacaoDTO;
import pt.ul.fc.css.project.handlers.ApoiarProjetoLeiHandler;
import pt.ul.fc.css.project.handlers.ConsultarProjetoLeiHandler;
import pt.ul.fc.css.project.handlers.EscolherDelegadoHandler;
import pt.ul.fc.css.project.handlers.ListarVotacoesHandler;
import pt.ul.fc.css.project.handlers.exceptions.DuplicatedActionException;
import pt.ul.fc.css.project.handlers.exceptions.EmptyFieldsException;
import pt.ul.fc.css.project.handlers.exceptions.InvalidActionException;
import pt.ul.fc.css.project.repositories.CidadaoRepository;

@RestController
@RequestMapping("/api")
public class CidadaoController {

	@Autowired
	private CatalogoCidadaos catalogoCidadaos;
	
	@Autowired
	private CidadaoRepository cidadaoRepository;

	private static final Logger log = LoggerFactory.getLogger(CidadaoController.class);


	@GetMapping("/cidadaos")
	public List<CidadaoDTO> getCidadaos(){
		return catalogoCidadaos.getCidadaos();
	}


	@GetMapping("/cidadao/{id}")
	public Optional<CidadaoDTO> getCidadao(@NonNull @PathVariable Long cidadao) {
		return catalogoCidadaos.getCidadao(cidadao);
	}


	@PostMapping("/cidadao-criar")
	public CidadaoDTO createCidadao(@NonNull @RequestParam String nome, @NonNull @RequestParam String nCartaoCidadao) {
		return catalogoCidadaos.createCidadao(nome, nCartaoCidadao);
	}


	@PostMapping("/cidadao/escolher-delegado/")
	public DelegadoEscolhidoDTO escolherDelegado(@NonNull @RequestParam Long id, @NonNull @RequestBody DelegadoEscolhidoDTO delEsc) throws Exception {
		return catalogoCidadaos.escolherDelegado(id, delEsc.getDelegado().getId(), delEsc.getTema().getId());
	}
}
