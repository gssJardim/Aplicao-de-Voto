package pt.ul.fc.css.project.handlers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pt.ul.fc.css.project.catalogos.CatalogoProjetoLei;
import pt.ul.fc.css.project.entities.ProjetoLei;
import pt.ul.fc.css.project.entities.enumerated.EstadoProjetoLei;
import pt.ul.fc.css.project.entities.facade.ProjetoLeiDTO;
import pt.ul.fc.css.project.repositories.ProjetoLeiRepository;
@Component
public class FecharProjetoLeiExpiradoHandler {
	@Autowired
	private ProjetoLeiRepository projetoLeiRepository;

	public void fecharProjetosLei() {
		for (ProjetoLei projetoLei : projetoLeiRepository.findAll()) {
			if(projetoLei.getDataValidade().isBefore(LocalDate.now())){
				projetoLei.setEstado(EstadoProjetoLei.FECHADO);
			}
		}
	}
}
