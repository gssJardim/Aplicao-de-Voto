package pt.ul.fc.css.project;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import mvcexample.business.services.ApplicationException;
import pt.ul.fc.css.project.catalogos.CatalogoCidadaos;
import pt.ul.fc.css.project.catalogos.CatalogoDelegados;
import pt.ul.fc.css.project.catalogos.CatalogoDelegadosEscolhidos;
import pt.ul.fc.css.project.catalogos.CatalogoProjetoLei;
import pt.ul.fc.css.project.catalogos.CatalogoTemas;
import pt.ul.fc.css.project.catalogos.CatalogoVotacoes;
import pt.ul.fc.css.project.entities.Cidadao;
import pt.ul.fc.css.project.entities.Delegado;
import pt.ul.fc.css.project.entities.ProjetoLei;
import pt.ul.fc.css.project.entities.Tema;
import pt.ul.fc.css.project.entities.enumerated.TipoDeVoto;
import pt.ul.fc.css.project.entities.facade.*;
import pt.ul.fc.css.project.handlers.FecharProjetoLeiExpiradoHandler;
import pt.ul.fc.css.project.handlers.exceptions.DuplicatedActionException;
import pt.ul.fc.css.project.handlers.exceptions.EmptyFieldsException;
import pt.ul.fc.css.project.handlers.exceptions.EmptyRepositoryException;
import pt.ul.fc.css.project.handlers.exceptions.InvalidActionException;
import pt.ul.fc.css.project.handlers.exceptions.NewEntityException;
import pt.ul.fc.css.project.repositories.CidadaoRepository;
import pt.ul.fc.css.project.repositories.DelegadoRepository;
import pt.ul.fc.css.project.repositories.DelegadoEscolhidoRepository;
import pt.ul.fc.css.project.repositories.ProjetoLeiRepository;
import pt.ul.fc.css.project.repositories.TemaRepository;
import pt.ul.fc.css.project.repositories.VotacaoRepository;
import pt.ul.fc.css.project.repositories.VotoPublicoRepository;
import pt.ul.fc.css.project.repositories.VotoPrivadoRepository;




@Controller
public class Web2Controller {
	
	@Autowired
	private CatalogoCidadaos cidadaoCat;
	
	@Autowired
	private CatalogoProjetoLei projLeiCat;
	
	@Autowired
	private CatalogoTemas temaCat;
	@Autowired
	private CatalogoDelegados delegadoCat;
	
	@Autowired
	private CatalogoDelegadosEscolhidos delegadoEscCat;
	
	@Autowired
	private CatalogoVotacoes votacaoEscCat;
	

	@GetMapping("/index") 
    public String getIndex(Model model) {
        return "index";
    }

	@GetMapping("/projetos-lei/criar")
	public String getProjetoLeiCriar(Model model) {
		model.addAttribute("projetolei", new ProjetoLeiDTO());
		List<TemaDTO> themes = temaCat.getListaTemas();
		model.addAttribute("themes", themes);
		List<DelegadoDTO> delegates = delegadoCat.getDelegados();
		model.addAttribute("delegates", delegates);
		return "/projetos-lei/criar";
	}
	
	@PostMapping("/projetos-lei/criar")
	public String projetoleiAction(Model model, @ModelAttribute ProjetoLeiDTO p) throws NewEntityException, EmptyFieldsException, InvalidActionException {
	    String proponenteId = p.getProponente().getNome();
	    if (proponenteId != null && !proponenteId.isEmpty()) {
	        long delegadoId = Long.parseLong(proponenteId);
	        Optional<DelegadoDTO> proponente = delegadoCat.getDelegado(delegadoId);
	        if (proponente.isPresent()) {
	            p.setProponente(proponente.get());
	        } else {
	            throw new NewEntityException("Delegado não encontrado.");
	        }
	    }

	    String temaId = p.getTema().getNome();
	    if (temaId != null && !temaId.isEmpty()) {
	        long temaIdLong = Long.parseLong(temaId);
	        Optional<TemaDTO> tema = temaCat.getTemaById(temaIdLong);
	        if (tema.isPresent()) {
	            p.setTema(tema.get());
	        } else {
	            throw new NewEntityException("Tema não encontrado.");
	        }
	    }
	    return "/projetos-lei/criar";
	}
	
	@GetMapping("/delegado/escolher")
	public String escolherDelegado(Model model) {
		List<CidadaoDTO> cidadaos = cidadaoCat.getCidadaos();
		model.addAttribute("cidadaos", cidadaos);
		List<DelegadoDTO> delegados = delegadoCat.getDelegados();
		model.addAttribute("delegados", delegados);
		List<TemaDTO> temas = temaCat.getListaTemas();
		model.addAttribute("temas", temas);
		return "delegado/escolher";
	}
	
	@PostMapping("/delegado/escolher")
	public String escolherDelegado(Model model, @ModelAttribute DelegadoEscolhidoDTO d, @NonNull @PathVariable Long cidadaoId) throws Exception {
		DelegadoEscolhidoDTO d2;
		try {
			d2 = delegadoEscCat.getEscolherDelegados(cidadaoId, d.getDelegado().getId(), d.getTema().getId());
			return "/index/" + d2.getId();
		} catch(NewEntityException e){
			d2 = new DelegadoEscolhidoDTO();
			model.addAttribute("delegado-escolhido", d2);
			model.addAttribute("error", e.getMessage());
			return "delegado/escolher";
		}
	}
	
	@GetMapping("/projetos-lei/apoiar")
	public String getApoiarProjLei(Model model) {
		model.addAttribute("projetolei", new ProjetoLeiDTO());
		List<CidadaoDTO> cidadaos = cidadaoCat.getCidadaos();
		model.addAttribute("cidadaos", cidadaos);
		List<ProjetoLeiDTO> projetosLei = projLeiCat.getProjetosLei();
		model.addAttribute("projetosLei", projetosLei);
		return "projetos-lei/apoiar";
	}
	
	@PostMapping("/projetos-lei/apoiar")
	public String postApoiarProjLei(Model model, @Validated ProjetoLeiDTO projetoLeiDTO, BindingResult bindingResult, @NonNull @PathVariable Long cidadaoId) throws DuplicatedActionException {
	    if (bindingResult.hasErrors()) {
	        model.addAttribute("projetolei", projetoLeiDTO);
	        model.addAttribute("cidadaos", cidadaoCat.getCidadaos());
	        model.addAttribute("projetosLei", projLeiCat.getProjetosLei());
	        return "projetos-lei/apoiar";
	    }

	    try {
	        Optional<ProjetoLeiDTO> p2 = projLeiCat.apoiarProjetoLei(cidadaoId, projetoLeiDTO.getId());
	        return "/index/" + p2.get().getId();
	    } catch (InvalidActionException e) {
	        model.addAttribute("projetolei", new ProjetoLeiDTO());
	        model.addAttribute("cidadaos", cidadaoCat.getCidadaos());
	        model.addAttribute("projetosLei", projLeiCat.getProjetosLei());
	        model.addAttribute("error", e.getMessage());
	        return "projetos-lei/apoiar";
	    }
	}
	
	@GetMapping("/votacao/votar")
	public String getProjetosLeiVotar(Model model) {
		model.addAttribute("votacao", new VotacaoDTO());
	    model.addAttribute("cidadaos", cidadaoCat.getCidadaos());
	    model.addAttribute("projetosLei", projLeiCat.getProjetosLei());
	    model.addAttribute("tipoDeVoto", Arrays.asList(TipoDeVoto.values()));
	    model.addAttribute("projetoLeiDTO", new ProjetoLeiDTO()); // Add this line

	    return "/votacao/votar";
	}
	
	//Long cidadaoId, Long votacaoId, TipoDeVoto tipoDeVoto
	@PostMapping("/votacao/votar")
	public String postProjetosLeiVotar(Model model, VotacaoDTO v, @RequestParam("tipoDeVoto") TipoDeVoto t, @NonNull @RequestParam Long cidadaoId) throws EmptyFieldsException, DuplicatedActionException{
		Optional<VotacaoDTO> v2;
	    try {
	        v2 = votacaoEscCat.votarNumaProposta(cidadaoId, v.getProjetoLeiDTO().getId(), t);
	        return "redirect:/index/" + v2.get().getId();
	    } catch (InvalidActionException e) {
	        model.addAttribute("votacao", v);
	        model.addAttribute("cidadaos", cidadaoCat.getCidadaos());
	        model.addAttribute("projetosLei", projLeiCat.getProjetosLei());
	        model.addAttribute("tipoDeVoto", Arrays.asList(TipoDeVoto.values()));
	        model.addAttribute("error", e.getMessage());
	        return "/votacao/votar";
	    }
	}
	
	
	@GetMapping("/projetos-lei/{id}") 
	public String getProjetoLeiVer(Model model, @PathVariable Long id) {
		Optional<ProjetoLeiDTO> resultProject = projLeiCat.getProjetoLeiById(id);
		ProjetoLeiDTO projetoLei = null;
		if (resultProject.isPresent()) {
			projetoLei = resultProject.get();
			Optional<TemaDTO> resultTheme = temaCat.getTemaById(projetoLei.getTema().getId());
			TemaDTO theme = resultTheme.isPresent() ? resultTheme.get() : null;
			Optional<DelegadoDTO> resultDelegate = delegadoCat.getDelegado(projetoLei.getProponente().getId());
			DelegadoDTO delegate = resultDelegate.isPresent() ? resultDelegate.get() : null;
			model.addAttribute("theme", theme);
			model.addAttribute("delegate", delegate);
		}
		model.addAttribute("id", id);
		model.addAttribute("projetoLei", projetoLei);
		return "/projetos-lei/id"; 
	}
	
	@GetMapping("/projetos-lei/ver-nao-expirados")
	public String projetosLeiVer(Model model) {
		model.addAttribute("projetolei", new ProjetoLeiDTO());
		List<ProjetoLeiDTO> projetosLei = projLeiCat.getProjetosLei();
		model.addAttribute("projetosLei", projetosLei);
		return "projetos-lei/ver-nao-expirados"; 
	}
//	
	@GetMapping("/votacoes")
    public String verVotacao(Model model) {
        model.addAttribute("votacoes", votacaoEscCat.getListaVotacoes());
        return "votacao/ver-em-votacao";
    }
	
	 @GetMapping("/votacoes/{id}")
	 public String detalheDaVotacao(final Model model, @PathVariable Long id) {
	     Optional<VotacaoDTO> v = votacaoEscCat.getVotacaoById(id);
	     if (v.isPresent()) {
//	         logger.error("CID:", c.get().getId());
	         model.addAttribute("customer", v.get());
	         return "detalhes-da-votacao";
	     } else {
	         return "error/404";
	     }
	 }
//	
//	

}
