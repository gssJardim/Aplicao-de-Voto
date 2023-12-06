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
import pt.ul.fc.css.project.entities.Cidadao;
import pt.ul.fc.css.project.entities.Delegado;
import pt.ul.fc.css.project.entities.ProjetoLei;
import pt.ul.fc.css.project.entities.Tema;
import pt.ul.fc.css.project.entities.facade.ProjetoLeiDTO;
import pt.ul.fc.css.project.entities.facade.VotacaoDTO;
import pt.ul.fc.css.project.repositories.CidadaoRepository;
import pt.ul.fc.css.project.repositories.DelegadoEscolhidoRepository;
import pt.ul.fc.css.project.repositories.DelegadoRepository;
import pt.ul.fc.css.project.repositories.ProjetoLeiRepository;
import pt.ul.fc.css.project.repositories.TemaRepository;
import pt.ul.fc.css.project.repositories.VotacaoRepository;
import pt.ul.fc.css.project.repositories.VotoPrivadoRepository;
import pt.ul.fc.css.project.repositories.VotoPublicoRepository;
import pt.ul.fc.css.project.handlers.ApresentarProjetoLeiHandler;

@SpringBootTest
public class ApresentarProjetoLeiTest {
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
	public void TestarDelegado() {
		inicializarCatalogos();
		Delegado delegado1 = catalogoDelegados.createDelegado("afonso", "253150698");
		
		
		Optional<Delegado> delegadoDoRepositorio = catalogoDelegados.getDelegado(delegado1.getId());
		Delegado delegado2 = delegadoDoRepositorio.get();
		
		assertEquals(delegado1, delegado2);
	}
	
	@Test
	public void TesteApresentarProjetoLei() {
		inicializarCatalogos();
		
		Delegado delegado1 = catalogoDelegados.createDelegado("Pedro", "123456789");
		
		Tema tema1 = catalogoTemas.createTema("Saude");
		
		ApresentarProjetoLeiHandler handler = new ApresentarProjetoLeiHandler(catalogoProjetosLei, catalogoTemas, delegado1);
		handler.InserirTitulo("titulo1");
		handler.InserirTextoDescritivo("texto descritivo1");
		handler.InserirAnexoPDF("anexo");
		handler.InserirDataValidade("2024", "11", "23", "22", "22");
		handler.IndicarTema(handler.PedirListaTemas().get(0).getId().toString());
		handler.confirmar();
		
		List<ProjetoLei> projetosLei = projetoLeiRepository.findAll();
		
		assertEquals(1, projetosLei.size());
	}
	
	@Test
	public void TestarDelegadoEscolhido() {
		inicializarCatalogos();
		
		Delegado delegado1 = catalogoDelegados.createDelegado("Pedro", "123456789");
		
		Tema tema1 = catalogoTemas.createTema("Saude");
		
		Cidadao cidadao1 = catalogoCidadaos.createCidadao("cdsvs", "441414");
		
		catalogoDelegadosEscolhidos.createDelegadoEscolhido(cidadao1, delegado1, tema1);
	}
}
