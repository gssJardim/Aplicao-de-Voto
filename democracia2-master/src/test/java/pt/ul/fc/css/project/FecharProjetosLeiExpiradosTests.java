package pt.ul.fc.css.project;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

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
import pt.ul.fc.css.project.entities.enumerated.EstadoProjetoLei;
import pt.ul.fc.css.project.handlers.FecharProjetoLeiExpiradoHandler;
import pt.ul.fc.css.project.repositories.CidadaoRepository;
import pt.ul.fc.css.project.repositories.DelegadoEscolhidoRepository;
import pt.ul.fc.css.project.repositories.DelegadoRepository;
import pt.ul.fc.css.project.repositories.ProjetoLeiRepository;
import pt.ul.fc.css.project.repositories.TemaRepository;
import pt.ul.fc.css.project.repositories.VotacaoRepository;
import pt.ul.fc.css.project.repositories.VotoPrivadoRepository;
import pt.ul.fc.css.project.repositories.VotoPublicoRepository;


@SpringBootTest
public class FecharProjetosLeiExpiradosTests {
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
	public void Test1() {
		inicializarCatalogos();

		Delegado delegado1 = catalogoDelegados.createDelegado("asfonso", "253150698");
		
		Tema tema1 = catalogoTemas.createTema("Saude");
		
		ProjetoLei projetoLei1 = catalogoProjetosLei.criarProjetoLei("titulo","texto","anexo",LocalDateTime.of(2022, 11, 20, 8, 55) ,tema1 ,delegado1);
		
		FecharProjetoLeiExpiradoHandler handler = new FecharProjetoLeiExpiradoHandler(catalogoProjetosLei);
		handler.fecharProjetoLei();
		
		Optional<ProjetoLei> possivelProjetoLei = catalogoProjetosLei.getProjetoLeiById(projetoLei1.getId());
		ProjetoLei projetoLeiFechado = possivelProjetoLei.get();
		
		assertEquals(projetoLeiFechado.getEstado(), EstadoProjetoLei.FECHADO);
	}
	
	@Test
	public void Test2() {
		inicializarCatalogos();
		Delegado delegado1 = catalogoDelegados.createDelegado("asfonso", "253150698");
		
		Tema tema1 = catalogoTemas.createTema("Saude");
		
		ProjetoLei projetoLei1 = catalogoProjetosLei.criarProjetoLei("titulo","texto","anexo",LocalDateTime.of(2024, 11, 20, 8, 55) ,tema1 ,delegado1);
		
		FecharProjetoLeiExpiradoHandler handler = new FecharProjetoLeiExpiradoHandler(catalogoProjetosLei);
		handler.fecharProjetoLei(); //Não fez nada, porque não expirou
		
		Optional<ProjetoLei> possivelProjetoLei = catalogoProjetosLei.getProjetoLeiById(projetoLei1.getId());
		ProjetoLei projetoLeiAberto = possivelProjetoLei.get();
		
		assertEquals(projetoLeiAberto.getEstado(), EstadoProjetoLei.ABERTO);
	}
}
