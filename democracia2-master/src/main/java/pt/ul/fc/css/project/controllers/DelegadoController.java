package pt.ul.fc.css.project.controllers;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import pt.ul.fc.css.project.catalogos.CatalogoDelegados;
//import pt.ul.fc.css.project.catalogos.CatalogoProjetoLei;
import pt.ul.fc.css.project.entities.Delegado;
import pt.ul.fc.css.project.entities.facade.CidadaoDTO;
import pt.ul.fc.css.project.entities.facade.DelegadoDTO;
import pt.ul.fc.css.project.entities.facade.DelegadoEscolhidoDTO;
import pt.ul.fc.css.project.entities.facade.ProjetoLeiDTO;
import pt.ul.fc.css.project.entities.facade.TemaDTO;
import pt.ul.fc.css.project.repositories.DelegadoRepository;

@RestController
@RequestMapping("/api")
public class DelegadoController {
	
	Logger logger = LoggerFactory.getLogger(DelegadoController.class);
	
	@Autowired
	private CatalogoDelegados catalogoDelegados;
	
//	@Autowired
//	private CatalogoProjetoLei catalogoProjetoLei;
	
	
	@GetMapping("/delegado")
	public Optional<DelegadoDTO> getDelegado(@NonNull @RequestBody DelegadoDTO delegado) {
		return catalogoDelegados.getDelegado(delegado.getId());
	}
	
	@GetMapping("/delegados")
	public List<DelegadoDTO> getDelegation() {
		return catalogoDelegados.getDelegados();
	}
	
	@PostMapping("/delegado-create")
	public DelegadoDTO criarDelegado (@NonNull @RequestBody DelegadoDTO delegado){
		return catalogoDelegados.createDelegado(delegado.getNome(), delegado.getnCartaoCidadao());
		
	}
}
