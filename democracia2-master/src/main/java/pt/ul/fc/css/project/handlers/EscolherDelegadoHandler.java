package pt.ul.fc.css.project.handlers;

import java.util.List;
import java.util.Optional;

import javax.naming.directory.InvalidAttributesException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import pt.ul.fc.css.project.Democracia2App;
import pt.ul.fc.css.project.catalogos.CatalogoCidadaos;
import pt.ul.fc.css.project.catalogos.CatalogoDelegados;
import pt.ul.fc.css.project.catalogos.CatalogoTemas;
import pt.ul.fc.css.project.controllers.ProjetoLeiController;
import pt.ul.fc.css.project.entities.Cidadao;
import pt.ul.fc.css.project.entities.Delegado;
import pt.ul.fc.css.project.entities.DelegadoEscolhido;
import pt.ul.fc.css.project.entities.ProjetoLei;
import pt.ul.fc.css.project.entities.Tema;
import pt.ul.fc.css.project.entities.facade.DelegadoDTO;
import pt.ul.fc.css.project.entities.facade.DelegadoEscolhidoDTO;
import pt.ul.fc.css.project.entities.facade.ProjetoLeiDTO;
import pt.ul.fc.css.project.entities.facade.TemaDTO;
import pt.ul.fc.css.project.handlers.exceptions.DuplicatedActionException;
import pt.ul.fc.css.project.repositories.CidadaoRepository;
import pt.ul.fc.css.project.repositories.DelegadoEscolhidoRepository;
import pt.ul.fc.css.project.repositories.DelegadoRepository;
import pt.ul.fc.css.project.repositories.TemaRepository;

@Component
public class EscolherDelegadoHandler {

	@Autowired
	private CidadaoRepository cidadaoRepository;
	@Autowired
	private DelegadoRepository delegadoRepository;
	@Autowired
	private DelegadoEscolhidoRepository delegadoEscolhidoRepository;
	@Autowired
	private TemaRepository temaRepository;

	private static final Logger log = LoggerFactory.getLogger(EscolherDelegadoHandler.class);


	public DelegadoEscolhidoDTO escolherDelegado(Long cidadaoId, Long delegadoId, Long temaId) throws Exception {
		Optional<Cidadao> possivelCidadao = cidadaoRepository.findById(cidadaoId); //get cidadao
		if(possivelCidadao.isPresent()) {
			Cidadao cidadao = possivelCidadao.get();
			Optional<Delegado> possivelDelegado = delegadoRepository.findById(delegadoId); //get delegado

			if(possivelDelegado.isPresent()) {
				Delegado delegado = possivelDelegado.get();
				Optional<Tema> possivelTema = temaRepository.findById(temaId); //get tema

				if(possivelTema.isPresent()) {
					Tema tema = possivelTema.get();
					verificarSeCidadaoJaEscolheuEsteDelegadoOuTema(cidadao, delegado, tema);
					
					DelegadoEscolhido novoDelegadoEscolhido = new DelegadoEscolhido(); //criar delegado escolhido
					novoDelegadoEscolhido.setDelegado(delegado);
					novoDelegadoEscolhido.setTema(tema);
					delegadoEscolhidoRepository.save(novoDelegadoEscolhido);
					
					cidadao.addDelegadoEscolhido(novoDelegadoEscolhido);
					cidadaoRepository.save(cidadao);
					return novoDelegadoEscolhido.toDTO();
				}else {
					throw new EntityNotFoundException("Tema não foi encontrado");
				}
			}else {
				throw new EntityNotFoundException("Delegado não foi encontrado");
			}
		}else {
			throw new EntityNotFoundException("Cidadao não foi encontrado");
		}
	}


	private void verificarSeCidadaoJaEscolheuEsteDelegadoOuTema(Cidadao cidadao, Delegado delegado, Tema tema) throws Exception {
		for (DelegadoEscolhido delegadoEscolhido : cidadao.getDelegadosEscolhidos()) {
			if(delegadoEscolhido.getDelegado().equals(delegado)) {
				throw new DuplicatedActionException("Cidadao já adicionou este delegado");
			}else if(delegadoEscolhido.getTema().equals(tema)){
				throw new DuplicatedActionException("Cidadao já escolheu um delegado para este tema");
			}
		}

	}
}
