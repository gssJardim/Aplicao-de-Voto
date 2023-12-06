package pt.ul.fc.css.project;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

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
import pt.ul.fc.css.project.entities.DelegadoEscolhido;
import pt.ul.fc.css.project.entities.Tema;
import pt.ul.fc.css.project.entities.facade.TemaDTO;
import pt.ul.fc.css.project.entities.facade.DelegadoDTO;
import pt.ul.fc.css.project.handlers.EscolherDelegadoHandler;
import pt.ul.fc.css.project.repositories.CidadaoRepository;
import pt.ul.fc.css.project.repositories.DelegadoEscolhidoRepository;
import pt.ul.fc.css.project.repositories.DelegadoRepository;
import pt.ul.fc.css.project.repositories.ProjetoLeiRepository;
import pt.ul.fc.css.project.repositories.TemaRepository;
import pt.ul.fc.css.project.repositories.VotacaoRepository;
import pt.ul.fc.css.project.repositories.VotoPrivadoRepository;
import pt.ul.fc.css.project.repositories.VotoPublicoRepository;

@SpringBootTest
public class EscolherDelegadoTests {
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
	public void verDelegados() {
		inicializarCatalogos();
		Cidadao cidadaoCorrente = catalogoCidadaos.createCidadao("cidadao1","12345678");

		catalogoDelegados.createDelegado("delegado1", "256423419");
		catalogoDelegados.createDelegado("delegado2", "252467419");
		catalogoDelegados.createDelegado("delegado3", "245794579");

		List<DelegadoDTO> delegadosDTO = catalogoDelegados.getDelegados().stream()
				.map(d -> new DelegadoDTO(d.getNome(), d.getId())).collect(Collectors.toList());

		EscolherDelegadoHandler handler = new EscolherDelegadoHandler(cidadaoCorrente,
				catalogoCidadaos, catalogoDelegados, catalogoDelegadosEscolhidos, catalogoTemas);
		List<DelegadoDTO> delegadosDTOHandler = handler.verDelegados();
		assertEquals(delegadosDTO, delegadosDTOHandler);
	}

	@Test
	public void verTemas() {
		inicializarCatalogos();
		Cidadao cidadaoCorrente = catalogoCidadaos.createCidadao("cidadao2","12345678");

		catalogoTemas.createTema("saude");
		catalogoTemas.createTema("desporto");
		catalogoTemas.createTema("ambiente");

		List<TemaDTO> temasDTO = catalogoTemas.getTemas().stream()
				.map(t -> new TemaDTO(t.getId(), t.getNome())).collect(Collectors.toList());

		EscolherDelegadoHandler handler = new EscolherDelegadoHandler(cidadaoCorrente,
				catalogoCidadaos, catalogoDelegados, catalogoDelegadosEscolhidos, catalogoTemas);
		List<TemaDTO> temasDTOHandler = handler.verTemas();

		assertEquals(temasDTO, temasDTOHandler);
	}

	@Test
	public void EscolherDelegado() {
		inicializarCatalogos();
		Cidadao cidadaoCorrente = catalogoCidadaos.createCidadao("cidadao3","12345678");

		Delegado delegado1 = catalogoDelegados.createDelegado("delegado4", "256423419");

		Tema tema1 = catalogoTemas.createTema("saude");

		EscolherDelegadoHandler handler = new EscolherDelegadoHandler(cidadaoCorrente,
				catalogoCidadaos, catalogoDelegados, catalogoDelegadosEscolhidos, catalogoTemas);

		handler.escolherDelegado(String.valueOf(delegado1.getId()), String.valueOf(tema1.getId()));

		Optional<Cidadao> possivelCidadao = catalogoCidadaos.getCidadaoById(cidadaoCorrente.getId());
		Cidadao cidadao = possivelCidadao.get();

		Delegado delegadoDoHandler = catalogoCidadaos.getDelegadoByTema(cidadao, tema1);

		assertEquals(delegado1, delegadoDoHandler);
	}

	@Test
	public void DelegadoEscolhidoComDelegadoAssociado() {
		inicializarCatalogos();
		Cidadao cidadaoCorrente = catalogoCidadaos.createCidadao("cidadao3","12345678");

		Delegado delegado1 = catalogoDelegados.createDelegado("delegado4", "256423419");

		Tema tema1 = catalogoTemas.createTema("saude");

		EscolherDelegadoHandler handler = new EscolherDelegadoHandler(cidadaoCorrente,
				catalogoCidadaos, catalogoDelegados, catalogoDelegadosEscolhidos, catalogoTemas);

		handler.escolherDelegado(String.valueOf(delegado1.getId()), String.valueOf(tema1.getId()));

		Optional<Cidadao> possivelCidadao = catalogoCidadaos.getCidadaoById(cidadaoCorrente.getId());
		Cidadao cidadao = possivelCidadao.get();

		DelegadoEscolhido delegadoEscolhidoDoHandler = catalogoCidadaos.getDelegadoEscolhidoByTema(cidadao, tema1);
		Delegado delegado = delegadoEscolhidoDoHandler.getDelegado();
		
		assertEquals(delegado1, delegado);
	}

	@Test
	public void Escolher2Delegados() {
		inicializarCatalogos();
		Cidadao cidadaoCorrente = catalogoCidadaos.createCidadao("cidadao4","12345678");

		Delegado delegado1 = catalogoDelegados.createDelegado("delegado5", "256423419");
		Delegado delegado2 = catalogoDelegados.createDelegado("delegado6", "252467419");

		Tema tema1 = catalogoTemas.createTema("saude");
		Tema tema2 = catalogoTemas.createTema("desporto");

		EscolherDelegadoHandler handler = new EscolherDelegadoHandler(cidadaoCorrente,
				catalogoCidadaos, catalogoDelegados, catalogoDelegadosEscolhidos, catalogoTemas);
		handler.verDelegados();
		handler.verTemas();
		handler.escolherDelegado(String.valueOf(delegado1.getId()), String.valueOf(tema1.getId()));
		handler.escolherDelegado(String.valueOf(delegado2.getId()), String.valueOf(tema2.getId()));

		Optional<Cidadao> possivelCidadao = catalogoCidadaos.getCidadaoById(cidadaoCorrente.getId());
		Cidadao cidadao = possivelCidadao.get();
		Set<DelegadoEscolhido> delegadoEscolhido = cidadao.getDelegadosEscolhidos();

		assertEquals(2, delegadoEscolhido.size());
	}

	@Test
	public void Escolher2DelegadosComOMesmoTema() {
		inicializarCatalogos();
		Cidadao cidadaoCorrente = catalogoCidadaos.createCidadao("cidadao5","12345678");

		Delegado delegado1 = catalogoDelegados.createDelegado("delegado7", "256423419");
		Delegado delegado2 = catalogoDelegados.createDelegado("delegado8", "252467419");

		Tema tema1 = catalogoTemas.createTema("saude");

		EscolherDelegadoHandler handler = new EscolherDelegadoHandler(cidadaoCorrente,
				catalogoCidadaos, catalogoDelegados, catalogoDelegadosEscolhidos, catalogoTemas);
		handler.verDelegados();
		handler.verTemas();
		handler.escolherDelegado(String.valueOf(delegado1.getId()), String.valueOf(tema1.getId()));
		handler.escolherDelegado(String.valueOf(delegado2.getId()), String.valueOf(tema1.getId())); //Nao faz nada

		Optional<Cidadao> possivelCidadao = catalogoCidadaos.getCidadaoById(cidadaoCorrente.getId());
		Cidadao cidadao = possivelCidadao.get();
		Set<DelegadoEscolhido> delegadoEscolhido = cidadao.getDelegadosEscolhidos();

		assertEquals(1, delegadoEscolhido.size());
	}

	@Test
	public void EscolherOMesmoDelegado2Vezes() {
		inicializarCatalogos();
		Cidadao cidadaoCorrente = catalogoCidadaos.createCidadao("cidadao6","12345678");

		Delegado delegado1 = catalogoDelegados.createDelegado("delegado9", "256423419");

		Tema tema1 = catalogoTemas.createTema("saude");
		Tema tema2 = catalogoTemas.createTema("desporto");

		EscolherDelegadoHandler handler = new EscolherDelegadoHandler(cidadaoCorrente,
				catalogoCidadaos, catalogoDelegados, catalogoDelegadosEscolhidos, catalogoTemas);
		handler.verDelegados();
		handler.verTemas();
		handler.escolherDelegado(String.valueOf(delegado1.getId()), String.valueOf(tema1.getId()));
		handler.escolherDelegado(String.valueOf(delegado1.getId()), String.valueOf(tema2.getId())); //Nao faz nada

		Optional<Cidadao> possivelCidadao = catalogoCidadaos.getCidadaoById(cidadaoCorrente.getId());
		Cidadao cidadao = possivelCidadao.get();
		Set<DelegadoEscolhido> delegadoEscolhido = cidadao.getDelegadosEscolhidos();

		assertEquals(1, delegadoEscolhido.size());
	}
}
