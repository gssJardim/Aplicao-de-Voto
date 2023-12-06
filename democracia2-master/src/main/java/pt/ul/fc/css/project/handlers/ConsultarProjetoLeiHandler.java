package pt.ul.fc.css.project.handlers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pt.ul.fc.css.project.entities.ProjetoLei;
import pt.ul.fc.css.project.entities.enumerated.EstadoProjetoLei;
import pt.ul.fc.css.project.entities.facade.ProjetoLeiDTO;
import pt.ul.fc.css.project.handlers.exceptions.EmptyRepositoryException;
import pt.ul.fc.css.project.repositories.ProjetoLeiRepository;

@Component
public class ConsultarProjetoLeiHandler {
	@Autowired
	private ProjetoLeiRepository projetoLeiRepository;
	
	
	public List<ProjetoLeiDTO> consultarProjetosLeiNaoExpirados() throws EmptyRepositoryException{
		ArrayList<ProjetoLeiDTO> projetosLeiNaoExpirados = new ArrayList<>();
		
		if(projetoLeiRepository.count() == 0)
			throw new EmptyRepositoryException("Não há projetos lei!");
		for (ProjetoLei projetoLei : projetoLeiRepository.findAll()) {
			if(!projetoLei.getEstado().equals(EstadoProjetoLei.FECHADO)) {
				ProjetoLeiDTO projetoLeiDTO = projetoLei.toDTO();
				projetosLeiNaoExpirados.add(projetoLeiDTO);
			}
		}
		return projetosLeiNaoExpirados;
	}
}
