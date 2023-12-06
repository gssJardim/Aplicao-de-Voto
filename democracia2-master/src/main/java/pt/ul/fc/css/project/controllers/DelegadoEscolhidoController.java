package pt.ul.fc.css.project.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import pt.ul.fc.css.project.catalogos.CatalogoDelegados;
import pt.ul.fc.css.project.catalogos.CatalogoDelegadosEscolhidos;
import pt.ul.fc.css.project.catalogos.CatalogoTemas;
import pt.ul.fc.css.project.entities.Delegado;
import pt.ul.fc.css.project.entities.DelegadoEscolhido;
import pt.ul.fc.css.project.entities.ProjetoLei;
import pt.ul.fc.css.project.entities.facade.DelegadoEscolhidoDTO;
import pt.ul.fc.css.project.repositories.DelegadoEscolhidoRepository;
import pt.ul.fc.css.project.repositories.DelegadoRepository;
import pt.ul.fc.css.project.repositories.TemaRepository;

@RestController
@RequestMapping("/api")
public class DelegadoEscolhidoController {
	@Autowired
	private CatalogoDelegadosEscolhidos catalogoDelegadosEscolhidos;
	@Autowired
	private CatalogoDelegados catalogoDelegados;
	@Autowired
	private CatalogoTemas catalogoTemas;
	
	
	@GetMapping("/delegadosEscolhidos")
	public List<DelegadoEscolhidoDTO> postDelegadoEscolhido() {
		return catalogoDelegadosEscolhidos.getDelegadosEscolhidos();
	}
	
	@PostMapping("/delegadoEscolhido")
	public Optional<DelegadoEscolhidoDTO> getDelegadosEscolhidos(@NonNull @RequestBody DelegadoEscolhidoDTO delDTO) {
		return catalogoDelegadosEscolhidos.getDelegadoEscolhidoById(delDTO.getId());
	}
}
