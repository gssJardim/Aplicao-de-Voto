package pt.ul.fc.css.project;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import pt.ul.fc.css.project.catalogos.CatalogoCidadaos;
import pt.ul.fc.css.project.catalogos.CatalogoDelegados;
import pt.ul.fc.css.project.catalogos.CatalogoDelegadosEscolhidos;
import pt.ul.fc.css.project.catalogos.CatalogoProjetoLei;
import pt.ul.fc.css.project.catalogos.CatalogoTemas;
import pt.ul.fc.css.project.catalogos.CatalogoVotacoes;
import pt.ul.fc.css.project.catalogos.CatalogoVotos;
import pt.ul.fc.css.project.entities.Delegado;
import pt.ul.fc.css.project.entities.ProjetoLei;
import pt.ul.fc.css.project.entities.Tema;
import pt.ul.fc.css.project.entities.facade.ProjetoLeiDTO;
import pt.ul.fc.css.project.handlers.ConsultarProjetoLeiHandler;
import pt.ul.fc.css.project.repositories.CidadaoRepository;
import pt.ul.fc.css.project.repositories.DelegadoEscolhidoRepository;
import pt.ul.fc.css.project.repositories.DelegadoRepository;
import pt.ul.fc.css.project.repositories.ProjetoLeiRepository;
import pt.ul.fc.css.project.repositories.TemaRepository;
import pt.ul.fc.css.project.repositories.VotacaoRepository;
import pt.ul.fc.css.project.repositories.VotoPrivadoRepository;
import pt.ul.fc.css.project.repositories.VotoPublicoRepository;

@SpringBootTest
public class ConsultarProjetoLeiTests {
	@Autowired
	private CidadaoRepository cidadaoRepository;
	@Autowired
	private DelegadoRepository delegadoRepository;
	@Autowired
	private DelegadoEscolhidoRepository delegadoEscolhidoRepository;
	@Autowired
	private TemaRepository temaRepository;
	@Autowired
	private ProjetoLeiRepository projetoLeiRepository;
	@Autowired
	private VotacaoRepository votacaoRepository;
	@Autowired
	private VotoPublicoRepository votoPublicoRepository;
	@Autowired
	private VotoPrivadoRepository votoPrivadoRepository;

	private CatalogoCidadaos catalogoCidadaos;
	private CatalogoDelegados catalogoDelegados;
	private CatalogoDelegadosEscolhidos catalogoDelegadosEscolhidos;
	private CatalogoTemas catalogoTemas;
	private CatalogoProjetoLei catalogoProjetosLei;
	private CatalogoVotacoes catalogoVotacoes;
	private CatalogoVotos catalogoVotos;
	

	private void inicializarCatalogos() {
		this.catalogoCidadaos = new CatalogoCidadaos(cidadaoRepository);
		this.catalogoDelegados = new CatalogoDelegados(delegadoRepository);
		this.catalogoTemas = new CatalogoTemas(temaRepository);
		this.catalogoProjetosLei = new CatalogoProjetoLei(projetoLeiRepository);
		this.catalogoVotacoes = new CatalogoVotacoes(votacaoRepository);
		this.catalogoVotos = new CatalogoVotos(votoPublicoRepository, votoPrivadoRepository);
		this.catalogoDelegadosEscolhidos = new CatalogoDelegadosEscolhidos(delegadoEscolhidoRepository);
	}

	@Test
	public void TestarConsulta1ProjetoLei() {
		inicializarCatalogos();

		Delegado delegado1 = catalogoDelegados.createDelegado("Afonso","253150698");

		Tema tema1 = catalogoTemas.createTema("saude");


		Optional<Delegado> possivelDelegado = catalogoDelegados.getDelegado(delegado1.getId());
		Delegado delegado = possivelDelegado.get();
		Optional<Tema> possivelTema = catalogoTemas.getTemaById(tema1.getId());
		Tema tema = possivelTema.get();
		catalogoProjetosLei.criarProjetoLei("titulo","texto","anexo",LocalDateTime.of(2026, 11, 20, 8, 55) ,tema ,delegado); 


		List<ProjetoLeiDTO> projetoLeiDTO = projetoLeiRepository.findAll().stream().map
				(v -> new ProjetoLeiDTO( v.getTitulo(), v.getTexto(), v.getAnexoPDF(),
						v.getDataValidade(), v.getTema(), v.getProponente(),
						v.getNApoiantes(), v.getEstado() )).collect(Collectors.toList());

		ConsultarProjetoLeiHandler handler = new ConsultarProjetoLeiHandler(catalogoProjetosLei);
		List<ProjetoLeiDTO> projetoLeiDTOHandler = handler.ConsultarProjetosLeiNaoExpirados();

		assertEquals(projetoLeiDTO.get(0), projetoLeiDTOHandler.get(0));

	}

	@Test
	public void TestarConsultar2ProjetosLei() {
		inicializarCatalogos();
		projetoLeiRepository.deleteAll();
		Delegado delegado1 = catalogoDelegados.createDelegado("Afonso","253150698");

		Tema tema1 = catalogoTemas.createTema("saude");


		Optional<Delegado> possivelDelegado = catalogoDelegados.getDelegado(delegado1.getId());
		Delegado delegado = possivelDelegado.get();
		Optional<Tema> possivelTema = catalogoTemas.getTemaById(tema1.getId());
		Tema tema = possivelTema.get();
		catalogoProjetosLei.criarProjetoLei("titulo","texto","anexo",LocalDateTime.of(2026, 11, 20, 8, 55) ,tema ,delegado); 

		Optional<Delegado> possivelDelegado2 = catalogoDelegados.getDelegado(delegado1.getId());
		Delegado delegado2 = possivelDelegado2.get();
		Optional<Tema> possivelTema2 = catalogoTemas.getTemaById(tema1.getId());
		Tema tema2 = possivelTema2.get();
		catalogoProjetosLei.criarProjetoLei("titulo","texto","anexo",LocalDateTime.of(2028, 11, 20, 8, 55) ,tema2 ,delegado2); 

		ConsultarProjetoLeiHandler handler = new ConsultarProjetoLeiHandler(catalogoProjetosLei);
		List<ProjetoLeiDTO> projetoLeiDTOHandler = handler.ConsultarProjetosLeiNaoExpirados();

		assertEquals(2, projetoLeiDTOHandler.size());

	}
}
