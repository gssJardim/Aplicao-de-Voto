package pt.ul.fc.css.project;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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
import pt.ul.fc.css.project.entities.Votacao;
import pt.ul.fc.css.project.entities.VotoPublico;
import pt.ul.fc.css.project.entities.enumerated.TipoDeVoto;
import pt.ul.fc.css.project.entities.facade.VotacaoDTO;
import pt.ul.fc.css.project.handlers.ListarVotacoesHandler;
import pt.ul.fc.css.project.repositories.CidadaoRepository;
import pt.ul.fc.css.project.repositories.DelegadoEscolhidoRepository;
import pt.ul.fc.css.project.repositories.DelegadoRepository;
import pt.ul.fc.css.project.repositories.ProjetoLeiRepository;
import pt.ul.fc.css.project.repositories.TemaRepository;
import pt.ul.fc.css.project.repositories.VotacaoRepository;
import pt.ul.fc.css.project.repositories.VotoPrivadoRepository;
import pt.ul.fc.css.project.repositories.VotoPublicoRepository;

@SpringBootTest
public class ListarVotacoesTests {
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
	public void TestarProjetoLei() {
		inicializarCatalogos();
		
		Delegado delegado1 = catalogoDelegados.createDelegado("afonso", "253150698");
		
		Tema tema1 = catalogoTemas.createTema("Saude");
		
		ProjetoLei projetoLei1 = catalogoProjetosLei.criarProjetoLei("titulo","texto","anexo",LocalDateTime.of(2024, 11, 20, 8, 55) ,tema1 ,delegado1);
		
		Optional<ProjetoLei> possivelProjetoLei = catalogoProjetosLei.getProjetoLeiById(projetoLei1.getId()); 
		ProjetoLei projetoLei2 = possivelProjetoLei.get();

		assertEquals(projetoLei1, projetoLei2);
	}
	
	@Test
	public void TestarVotacao() {
		inicializarCatalogos();
		Delegado delegado1 = new Delegado("Afonso","253150698");
		delegadoRepository.save(delegado1);

		
		Tema tema1 = new Tema("Saude");
		temaRepository.save(tema1);
		
		ProjetoLei projetoLei1 = new ProjetoLei("titulo","texto","anexo",LocalDateTime.of(2024, 11, 20, 8, 55) ,tema1 ,delegado1);
		projetoLeiRepository.save(projetoLei1);
		
		VotoPublico voto1 = new VotoPublico(delegado1, TipoDeVoto.FAVOR);
		votoPublicoRepository.save(voto1);
		
		Votacao votacao1 = new Votacao(projetoLei1, voto1);
		votacaoRepository.save(votacao1);
		
		Optional<Votacao> possivelVotacao = catalogoVotacoes.getVotacaoById(votacao1.getId());
		Votacao votacao2 = possivelVotacao.get();
		
		assertEquals(votacao1, votacao2);
		
	}
		
		
	@Test
	public void TestarListarVotacoesHandler() {
		inicializarCatalogos();
		Delegado delegado1 = new Delegado("Afonso","253150698");
		delegadoRepository.save(delegado1);

		
		Tema tema1 = new Tema("Saude");
		temaRepository.save(tema1);
		
		ProjetoLei projetoLei1 = new ProjetoLei("titulo","texto","anexo",LocalDateTime.of(2024, 11, 20, 8, 55) ,tema1 ,delegado1);
		projetoLeiRepository.save(projetoLei1);
		
		VotoPublico voto1 = new VotoPublico(delegado1, TipoDeVoto.FAVOR);
		votoPublicoRepository.save(voto1);
		
		Votacao votacao1 = new Votacao(projetoLei1, voto1);
		votacaoRepository.save(votacao1);
		
		List<VotacaoDTO> votacoesDTO = votacaoRepository.findAll().stream().map
				(v -> new VotacaoDTO(v.getId(), v.getProjetoLei(), v.getTema(),
						v.getDataFecho(), v.getProponente(), v.getVotosDeDelegados(),
						v.getaFavor(), v.getEcontra(), v.getEstadoVotacao())).collect(Collectors.toList());
		
		ListarVotacoesHandler handler = new ListarVotacoesHandler(catalogoVotacoes);
		List<VotacaoDTO> votacoesDTOHandler = handler.listarVotacoesEmCurso();
		
		
		assertEquals(votacoesDTO, votacoesDTOHandler);
	}
}
