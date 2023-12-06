package pt.ul.fc.css.project.catalogos;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pt.ul.fc.css.project.entities.ProjetoLei;
import pt.ul.fc.css.project.entities.Tema;
import pt.ul.fc.css.project.entities.facade.DelegadoDTO;
import pt.ul.fc.css.project.entities.facade.ProjetoLeiDTO;
import pt.ul.fc.css.project.entities.facade.TemaDTO;
import pt.ul.fc.css.project.repositories.TemaRepository;

@Component
public class CatalogoTemas {
	@Autowired
	private TemaRepository temaRepository;
	
	@Autowired
	
	public List<TemaDTO> getListaTemas() {
		return temaRepository.findAll().stream().map(d -> new TemaDTO(d.getId(), d.getNome())).collect(Collectors.toList());

	}

	public Optional<TemaDTO> getTemaById(long id) {
		return temaRepository.findById(id).map(t -> t.toDTO());
	}
	
	public Optional<Tema> getTemaDTOById(String id) {
		return temaRepository.findById(Long.parseLong(id));
	}

	public TemaDTO createTema() {
		Tema tema = new Tema();
		temaRepository.save(tema);
		return tema.toDTO();

	}

	public List<Tema> getTemas() { //USADO PARA TESTES
		return temaRepository.findAll();
	}	
}
