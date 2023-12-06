package pt.ul.fc.css.project.catalogos;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pt.ul.fc.css.project.entities.Cidadao;
import pt.ul.fc.css.project.entities.Delegado;
import pt.ul.fc.css.project.entities.ProjetoLei;
import pt.ul.fc.css.project.entities.Tema;
import pt.ul.fc.css.project.entities.Votacao;
import pt.ul.fc.css.project.entities.enumerated.EstadoProjetoLei;
import pt.ul.fc.css.project.entities.facade.CidadaoDTO;
import pt.ul.fc.css.project.entities.facade.DelegadoDTO;
import pt.ul.fc.css.project.entities.facade.ProjetoLeiDTO;
import pt.ul.fc.css.project.entities.facade.TemaDTO;
import pt.ul.fc.css.project.handlers.ApoiarProjetoLeiHandler;
import pt.ul.fc.css.project.handlers.ApresentarProjetoLeiHandler;
import pt.ul.fc.css.project.handlers.ConsultarProjetoLeiHandler;
import pt.ul.fc.css.project.handlers.FecharProjetoLeiExpiradoHandler;
import pt.ul.fc.css.project.handlers.exceptions.DuplicatedActionException;
import pt.ul.fc.css.project.handlers.exceptions.EmptyFieldsException;
import pt.ul.fc.css.project.handlers.exceptions.EmptyRepositoryException;
import pt.ul.fc.css.project.handlers.exceptions.InvalidActionException;
import pt.ul.fc.css.project.handlers.exceptions.NewEntityException;
import pt.ul.fc.css.project.repositories.ProjetoLeiRepository;
@Component
public class CatalogoProjetoLei {
	@Autowired
	private ProjetoLeiRepository projetoLeiRepository;
	
	@Autowired
	private FecharProjetoLeiExpiradoHandler fecharProjetoLeiExpiradoHandler;
	
	@Autowired
	private ApoiarProjetoLeiHandler apoiarProjetoLeiHandler;
	
	@Autowired
	private ApresentarProjetoLeiHandler apresentarProjetoLeiHandler;
	
	@Autowired
	private ConsultarProjetoLeiHandler consultarProjetoLeiHandler;
	
	Logger logger = LoggerFactory.getLogger(CatalogoProjetoLei.class);



	public Optional<ProjetoLeiDTO> getProjetoLeiById(long id) {
		return projetoLeiRepository.findById(id).map(p -> p.toDTO());
	}
	
	public List<ProjetoLeiDTO> getProjetosLei(){
		ArrayList<ProjetoLeiDTO> projetosLeiNaoExpirados = new ArrayList<>();
		for (ProjetoLei projetoLei : projetoLeiRepository.findAll()) {
			ProjetoLeiDTO projetoLeiDTO = projetoLei.toDTO();
			projetosLeiNaoExpirados.add(projetoLeiDTO);
		}
		return projetosLeiNaoExpirados;
	}
	
	public Optional<ProjetoLeiDTO> apoiarProjetoLei(Long cidadaoId, Long projetoLeiId) throws InvalidActionException, DuplicatedActionException {
		return apoiarProjetoLeiHandler.apoiarProjetoLei(cidadaoId, projetoLeiId);
	}
	
	public ProjetoLeiDTO apresentarProjetoLei(String titulo, String texto, String anexoPDF, 
			LocalDate dataValidade, Long tema, Long proponente) throws NewEntityException, EmptyFieldsException, InvalidActionException {
		logger.debug("estamos cá");
		ProjetoLei novoProjetoLei = apresentarProjetoLeiHandler.criarProjetoLei(titulo, texto, anexoPDF, dataValidade, tema, proponente);
		return novoProjetoLei.toDTO();
	}
	
	public List<ProjetoLeiDTO> getProjetosLeiNaoExpirados() throws EmptyRepositoryException{
		return consultarProjetoLeiHandler.consultarProjetosLeiNaoExpirados();
	}

}
