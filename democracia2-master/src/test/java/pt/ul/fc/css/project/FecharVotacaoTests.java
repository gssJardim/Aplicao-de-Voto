package pt.ul.fc.css.project;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.Optional;

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
import pt.ul.fc.css.project.entities.Votacao;
import pt.ul.fc.css.project.entities.VotoPublico;
import pt.ul.fc.css.project.entities.enumerated.EstadoProjetoLei;
import pt.ul.fc.css.project.entities.enumerated.EstadoVotacao;
import pt.ul.fc.css.project.entities.enumerated.TipoDeVoto;
import pt.ul.fc.css.project.repositories.CidadaoRepository;
import pt.ul.fc.css.project.repositories.DelegadoEscolhidoRepository;
import pt.ul.fc.css.project.repositories.DelegadoRepository;
import pt.ul.fc.css.project.repositories.ProjetoLeiRepository;
import pt.ul.fc.css.project.repositories.TemaRepository;
import pt.ul.fc.css.project.repositories.VotacaoRepository;
import pt.ul.fc.css.project.repositories.VotoPrivadoRepository;
import pt.ul.fc.css.project.repositories.VotoPublicoRepository;
import pt.ul.fc.css.project.handlers.*;

@SpringBootTest
public class FecharVotacaoTests {

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
	public void TesteFecharVotacaoComVotoApenasDoProponente() {
		inicializarCatalogos();

		Delegado delegado1 = catalogoDelegados.createDelegado("asfonso", "253150698");

		Tema tema1 = catalogoTemas.createTema("Saude");

		ProjetoLei projetoLei1 = catalogoProjetosLei.criarProjetoLei("titulo","texto","anexo",LocalDateTime.of(2022, 11, 20, 8, 55) ,tema1 ,delegado1);

		VotoPublico voto1 = catalogoVotos.criarVotoProponente(delegado1);

		Votacao votacao = catalogoVotacoes.criarVotacao(projetoLei1, voto1);;

		FecharUmaVotacaoHandler handler = new FecharUmaVotacaoHandler(catalogoVotacoes, votacao);
		handler.fecharVotacao();

		Optional<Votacao> possivelVotacao = catalogoVotacoes.getVotacaoById(votacao.getId());
		Votacao votacaoFechada = possivelVotacao.get();

		assertEquals(EstadoVotacao.APROVADO, votacaoFechada.getEstadoVotacao());

	}

	@Test
	public void TesteFecharVotacaoCom3VotosContra() {
		inicializarCatalogos();

		Delegado delegado1 = catalogoDelegados.createDelegado("asfonso", "253150698");
		Tema tema1 = catalogoTemas.createTema("Saude");
		ProjetoLei projetoLei1 = catalogoProjetosLei.criarProjetoLei("titulo","texto","anexo",LocalDateTime.of(2022, 11, 20, 8, 55) ,tema1 ,delegado1);
		VotoPublico voto1 = catalogoVotos.criarVotoProponente(delegado1);

		Votacao votacao = catalogoVotacoes.criarVotacao(projetoLei1, voto1);


		catalogoVotacoes.addVotoPrivado(votacao, catalogoCidadaos.createCidadao("luis", "56341987"), catalogoVotos.criarVotoPrivado(TipoDeVoto.CONTRA));
		catalogoVotacoes.addVotoPrivado(votacao, catalogoCidadaos.createCidadao("daniel", "56379987"), catalogoVotos.criarVotoPrivado(TipoDeVoto.CONTRA));
		catalogoVotacoes.addVotoPrivado(votacao, catalogoCidadaos.createCidadao("andre", "56349987"), catalogoVotos.criarVotoPrivado(TipoDeVoto.CONTRA));

		FecharUmaVotacaoHandler handler = new FecharUmaVotacaoHandler(catalogoVotacoes, votacao);
		handler.fecharVotacao();

		Optional<Votacao> possivelVotacao = catalogoVotacoes.getVotacaoById(votacao.getId());
		Votacao votacaoFechada = possivelVotacao.get();

		assertEquals(EstadoVotacao.REJEITADO, votacaoFechada.getEstadoVotacao());

	}

	@Test
	public void TestarFecharUmaVotacaoComOMesmoNumeroDeVotos() { //A favor e contra
		inicializarCatalogos();

		Delegado delegado1 = catalogoDelegados.createDelegado("asfonso", "253150698");
		Tema tema1 = catalogoTemas.createTema("Saude");
		ProjetoLei projetoLei1 = catalogoProjetosLei.criarProjetoLei("titulo","texto","anexo",LocalDateTime.of(2022, 11, 20, 8, 55) ,tema1 ,delegado1);
		VotoPublico voto1 = catalogoVotos.criarVotoProponente(delegado1);

		Votacao votacao = catalogoVotacoes.criarVotacao(projetoLei1, voto1);


		catalogoVotacoes.addVotoPrivado(votacao, catalogoCidadaos.createCidadao("luis", "56341987"), catalogoVotos.criarVotoPrivado(TipoDeVoto.CONTRA));
		catalogoVotacoes.addVotoPrivado(votacao, catalogoCidadaos.createCidadao("daniel", "56379987"), catalogoVotos.criarVotoPrivado(TipoDeVoto.CONTRA));
		catalogoVotacoes.addVotoPrivado(votacao, catalogoCidadaos.createCidadao("andre", "56349987"), catalogoVotos.criarVotoPrivado(TipoDeVoto.FAVOR));

		FecharUmaVotacaoHandler handler = new FecharUmaVotacaoHandler(catalogoVotacoes, votacao);
		handler.fecharVotacao();

		Optional<Votacao> possivelVotacao = catalogoVotacoes.getVotacaoById(votacao.getId());
		Votacao votacaoFechada = possivelVotacao.get();

		assertEquals(EstadoVotacao.REJEITADO, votacaoFechada.getEstadoVotacao());
	}

}
