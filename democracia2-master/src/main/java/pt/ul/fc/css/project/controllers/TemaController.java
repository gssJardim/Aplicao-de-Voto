package pt.ul.fc.css.project.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pt.ul.fc.css.project.catalogos.CatalogoTemas;
import pt.ul.fc.css.project.entities.Tema;
import pt.ul.fc.css.project.entities.facade.TemaDTO;
import pt.ul.fc.css.project.repositories.TemaRepository;

@RestController
@RequestMapping("/api")
public class TemaController {
	@Autowired
	private CatalogoTemas catTema;
	
	@GetMapping("/tema")
	public Optional<TemaDTO> getTema(@NonNull @RequestBody TemaDTO tema) {
		return catTema.getTemaById(tema.getId());
	}
	
	@GetMapping("/temas")
    public List<TemaDTO> getTemas() {
        return catTema.getListaTemas();
    }
	
//	@PostMapping("/createtema")
//	public TemaDTO createTema(@NonNull @RequestBody TemaDTO tema) {
//		return catTema.createTema(tema.getNome());
//	}
}
