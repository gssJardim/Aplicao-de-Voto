package pt.ul.fc.css.project.handlers;

import java.time.LocalDate;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.persistence.EntityNotFoundException;
import pt.ul.fc.css.project.entities.Delegado;
import pt.ul.fc.css.project.entities.ProjetoLei;
import pt.ul.fc.css.project.entities.Tema;
import pt.ul.fc.css.project.handlers.exceptions.EmptyFieldsException;
import pt.ul.fc.css.project.handlers.exceptions.InvalidActionException;
import pt.ul.fc.css.project.handlers.exceptions.NewEntityException;
import pt.ul.fc.css.project.repositories.DelegadoRepository;
import pt.ul.fc.css.project.repositories.ProjetoLeiRepository;
import pt.ul.fc.css.project.repositories.TemaRepository;
@Component
public class ApresentarProjetoLeiHandler {
	
	Logger logger = LoggerFactory.getLogger(ApresentarProjetoLeiHandler.class);
	@Autowired
	private ProjetoLeiRepository projetoLeiRepository;
	
	@Autowired
	private DelegadoRepository delegadoRepository;
	
	@Autowired
	private TemaRepository temaRepository;

	public ProjetoLei criarProjetoLei(String titulo, String texto, String anexoPDF, 
			LocalDate dataValidade, Long tema, Long proponente) throws NewEntityException, EmptyFieldsException, InvalidActionException{
		logger.info("criado");
		if(titulo == null || titulo.isEmpty()) {
			throw new EmptyFieldsException("Titulo is a required field");
		}
		if(texto == null) {
			throw new EmptyFieldsException("Texto descritivo is a required field");
		}
		if(anexoPDF.equals(null)) {
			throw new EmptyFieldsException("Anexo is required");
		}
		if(dataValidade.isBefore(LocalDate.now())) {
			throw new InvalidActionException("Data de Validade is invalid");
		}
		Optional<Tema> temaOPT = temaRepository.findById(tema);
		if(temaOPT.isPresent()) {
			Tema t = temaOPT.get();
			Optional<Delegado> proponenteOPT = delegadoRepository.findById(proponente);
			Delegado d = proponenteOPT.get();
			if(proponenteOPT.isPresent()) {
				ProjetoLei p = new ProjetoLei();
				p.setTitulo(titulo);
				p.setTexto(texto);
				p.setAnexoPDF(anexoPDF);
				p.setDataValidade(dataValidade);
				p.setTema(t);
				p.setProponente(d);
				
				return projetoLeiRepository.save(p);
			}else {
				throw new EntityNotFoundException("Não existe esse proponente");
			}
		}else{
			throw new EntityNotFoundException("Não existe esse tema");
		}
	}
}