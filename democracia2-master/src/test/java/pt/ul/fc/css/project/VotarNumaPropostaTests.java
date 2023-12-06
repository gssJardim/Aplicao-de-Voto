package pt.ul.fc.css.project;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.repository.CrudRepository;

import pt.ul.fc.css.project.catalogos.*;
import pt.ul.fc.css.project.entities.Cidadao;
import pt.ul.fc.css.project.entities.Delegado;
import pt.ul.fc.css.project.entities.ProjetoLei;
import pt.ul.fc.css.project.entities.Tema;
import pt.ul.fc.css.project.entities.Votacao;
import pt.ul.fc.css.project.entities.Voto;
import pt.ul.fc.css.project.entities.VotoPrivado;
import pt.ul.fc.css.project.entities.VotoPublico;
import pt.ul.fc.css.project.entities.enumerated.TipoDeVoto;
import pt.ul.fc.css.project.entities.facade.ProjetoLeiDTO;
import pt.ul.fc.css.project.entities.facade.VotacaoDTO;
import pt.ul.fc.css.project.handlers.*;
import pt.ul.fc.css.project.repositories.*;


@SpringBootTest
public class VotarNumaPropostaTests {

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
	public void TestarVotosPrivados() {
		inicializarCatalogos();
		VotoPrivado votoPrivado1 = catalogoVotos.criarVotoPrivado(TipoDeVoto.FAVOR);	
		
		
		Optional<VotoPrivado> possivelVotoPrivado = catalogoVotos.getVotoPrivadoById(votoPrivado1.getId());
		VotoPrivado votoPrivado2 = possivelVotoPrivado.get();
		assertEquals(votoPrivado1, votoPrivado2);
	}
	
	@Test
	public void TestarVotosPublicos() {
		inicializarCatalogos();
		VotoPublico votoPublico1 = catalogoVotos.criarVotoProponente(catalogoDelegados.createDelegado("pedro", "56369"));
		
		Optional<VotoPublico> possivelVotoPublico = catalogoVotos.getVotoPublicoById(votoPublico1.getId());
		VotoPublico votoPublico2 = possivelVotoPublico.get();
		assertEquals(votoPublico1, votoPublico2);
	}
	
	@Test
	public void TestarEscolherVotacao() {
		inicializarCatalogos();

		Delegado delegado1 = catalogoDelegados.createDelegado("Afonso","253150698");

		Cidadao cidadao1 = catalogoCidadaos.createCidadao("marcelo", "56359");

		Tema tema1 = catalogoTemas.createTema("saude");

		Optional<Delegado> possivelDelegado = catalogoDelegados.getDelegado(delegado1.getId());
		Delegado delegado = possivelDelegado.get();
		
		Optional<Tema> possivelTema = catalogoTemas.getTemaById(tema1.getId());
		Tema tema = possivelTema.get();
		
		ProjetoLei projetoLei1 = catalogoProjetosLei.criarProjetoLei("titulo", "texto", "aneo", LocalDateTime.of(2024, 11, 11, 21, 11), tema, delegado);
		Optional<ProjetoLei> possivelProjetoLei = projetoLeiRepository.findById(projetoLei1.getId());
		ProjetoLei projetoLei = possivelProjetoLei.get();

		
		//PARA VER O VOTO DO DELEGADO, EH PRECISO ADICIONAR AO SET DO CIDADAO
		EscolherDelegadoHandler delegadoHandler = new EscolherDelegadoHandler(cidadao1, catalogoCidadaos, catalogoDelegados, catalogoDelegadosEscolhidos, catalogoTemas);
		delegadoHandler.escolherDelegado(String.valueOf(delegado1.getId()), String.valueOf(tema1.getId()));
			
		Optional<Cidadao> possivelCidadao = catalogoCidadaos.getCidadaoById(cidadao1.getId());
		Cidadao cidadao = possivelCidadao.get();
		
		
		VotoPublico votoProponente1 = catalogoVotos.criarVotoProponente(delegado);
		Optional<VotoPublico> possivelVotoProponente = catalogoVotos.getVotoPublicoById(votoProponente1.getId());
		VotoPublico votoProponente = possivelVotoProponente.get();
		
		Votacao votacao1 = catalogoVotacoes.criarVotacao(projetoLei, votoProponente);
		
		VotarNumaPropostaHandler handler = new VotarNumaPropostaHandler(
				cidadao, catalogoCidadaos, catalogoVotacoes, catalogoVotos);
		VotoPublico voto = handler.escolherVotacao(String.valueOf(votacao1.getId()));
		
		assertEquals(TipoDeVoto.FAVOR, voto.getTipoDeVoto());
	}

	@Test
	public void TestarAceitarVotoDelegado() {
		inicializarCatalogos();

		Delegado delegado1 = catalogoDelegados.createDelegado("Afonsdado","253152120698");

		Cidadao cidadao1 = catalogoCidadaos.createCidadao("marcedwdlo", "5632159");

		Tema tema1 = catalogoTemas.createTema("saudwde");

		Optional<Delegado> possivelDelegado = catalogoDelegados.getDelegado(delegado1.getId());
		Delegado delegado = possivelDelegado.get();
		
		Optional<Tema> possivelTema = catalogoTemas.getTemaById(tema1.getId());
		Tema tema = possivelTema.get();
		
		ProjetoLei projetoLei1 = catalogoProjetosLei.criarProjetoLei("tituewlo", "texdto", "anewedo", LocalDateTime.of(2024, 11, 11, 21, 11), tema, delegado);
		Optional<ProjetoLei> possivelProjetoLei = projetoLeiRepository.findById(projetoLei1.getId());
		ProjetoLei projetoLei = possivelProjetoLei.get();

		
		//PARA VER O VOTO DO DELEGADO, EH PRECISO ADICIONAR AO SET DO CIDADAO
		EscolherDelegadoHandler delegadoHandler = new EscolherDelegadoHandler(cidadao1, catalogoCidadaos, catalogoDelegados, catalogoDelegadosEscolhidos, catalogoTemas);
		delegadoHandler.escolherDelegado(String.valueOf(delegado1.getId()), String.valueOf(tema1.getId()));
			
		Optional<Cidadao> possivelCidadao = catalogoCidadaos.getCidadaoById(cidadao1.getId());
		Cidadao cidadao = possivelCidadao.get();
		
		
		VotoPublico votoProponente1 = catalogoVotos.criarVotoProponente(delegado);
		Optional<VotoPublico> possivelVotoProponente = catalogoVotos.getVotoPublicoById(votoProponente1.getId());
		VotoPublico votoProponente = possivelVotoProponente.get();
		
		Votacao votacao1 = catalogoVotacoes.criarVotacao(projetoLei, votoProponente);
		
		VotarNumaPropostaHandler handler = new VotarNumaPropostaHandler(
				cidadao, catalogoCidadaos, catalogoVotacoes, catalogoVotos);
		VotoPublico voto = handler.escolherVotacao(String.valueOf(votacao1.getId()));
		handler.aceitarVotoDelegado();
		
		assertEquals(2, voto.getValor());
	}

	@Test
	public void TestarNVotosAFavor() {
		inicializarCatalogos();

		Delegado delegado1 = catalogoDelegados.createDelegado("Afonsdado","253152120698");

		Cidadao cidadao1 = catalogoCidadaos.createCidadao("marcedwdlo", "5632159");

		Tema tema1 = catalogoTemas.createTema("saudwde");

		Optional<Delegado> possivelDelegado = catalogoDelegados.getDelegado(delegado1.getId());
		Delegado delegado = possivelDelegado.get();
		
		Optional<Tema> possivelTema = catalogoTemas.getTemaById(tema1.getId());
		Tema tema = possivelTema.get();
		
		ProjetoLei projetoLei1 = catalogoProjetosLei.criarProjetoLei("tituewlo", "texdto", "anewedo", LocalDateTime.of(2024, 11, 11, 21, 11), tema, delegado);
		Optional<ProjetoLei> possivelProjetoLei = projetoLeiRepository.findById(projetoLei1.getId());
		ProjetoLei projetoLei = possivelProjetoLei.get();

		
		//PARA VER O VOTO DO DELEGADO, EH PRECISO ADICIONAR AO SET DO CIDADAO
		EscolherDelegadoHandler delegadoHandler = new EscolherDelegadoHandler(cidadao1, catalogoCidadaos, catalogoDelegados, catalogoDelegadosEscolhidos, catalogoTemas);
		delegadoHandler.escolherDelegado(String.valueOf(delegado1.getId()), String.valueOf(tema1.getId()));
			
		Optional<Cidadao> possivelCidadao = catalogoCidadaos.getCidadaoById(cidadao1.getId());
		Cidadao cidadao = possivelCidadao.get();
		
		
		VotoPublico votoProponente1 = catalogoVotos.criarVotoProponente(delegado);
		Optional<VotoPublico> possivelVotoProponente = catalogoVotos.getVotoPublicoById(votoProponente1.getId());
		VotoPublico votoProponente = possivelVotoProponente.get();
		
		Votacao votacao1 = catalogoVotacoes.criarVotacao(projetoLei, votoProponente);
		
		VotarNumaPropostaHandler handler = new VotarNumaPropostaHandler(
				cidadao, catalogoCidadaos, catalogoVotacoes, catalogoVotos);
		handler.escolherVotacao(String.valueOf(votacao1.getId()));
		handler.aceitarVotoDelegado();
		
		Optional<Votacao> possivelVotacao = catalogoVotacoes.getVotacaoById(votacao1.getId());
		Votacao votacao2 = possivelVotacao.get();
		
		assertEquals(2, votacao2.getaFavor());
	}
	
	@Test
	public void TestarLancarProprioVoto() {
		inicializarCatalogos();

		Delegado delegado1 = catalogoDelegados.createDelegado("Afonsdado","253152120698");

		Cidadao cidadao1 = catalogoCidadaos.createCidadao("marcedwdlo", "5632159");

		Tema tema1 = catalogoTemas.createTema("saudwde");

		Optional<Delegado> possivelDelegado = catalogoDelegados.getDelegado(delegado1.getId());
		Delegado delegado = possivelDelegado.get();
		
		Optional<Tema> possivelTema = catalogoTemas.getTemaById(tema1.getId());
		Tema tema = possivelTema.get();
		
		ProjetoLei projetoLei1 = catalogoProjetosLei.criarProjetoLei("tituewlo", "texdto", "anewedo", LocalDateTime.of(2024, 11, 11, 21, 11), tema, delegado);
		Optional<ProjetoLei> possivelProjetoLei = projetoLeiRepository.findById(projetoLei1.getId());
		ProjetoLei projetoLei = possivelProjetoLei.get();

		
		//PARA VER O VOTO DO DELEGADO, EH PRECISO ADICIONAR AO SET DO CIDADAO
		EscolherDelegadoHandler delegadoHandler = new EscolherDelegadoHandler(cidadao1, catalogoCidadaos, catalogoDelegados, catalogoDelegadosEscolhidos, catalogoTemas);
		delegadoHandler.escolherDelegado(String.valueOf(delegado1.getId()), String.valueOf(tema1.getId()));
			
		Optional<Cidadao> possivelCidadao = catalogoCidadaos.getCidadaoById(cidadao1.getId());
		Cidadao cidadao = possivelCidadao.get();
		
		
		VotoPublico votoProponente1 = catalogoVotos.criarVotoProponente(delegado);
		Optional<VotoPublico> possivelVotoProponente = catalogoVotos.getVotoPublicoById(votoProponente1.getId());
		VotoPublico votoProponente = possivelVotoProponente.get();
		
		Votacao votacao1 = catalogoVotacoes.criarVotacao(projetoLei, votoProponente);
		
		VotarNumaPropostaHandler handler = new VotarNumaPropostaHandler(
				cidadao, catalogoCidadaos, catalogoVotacoes, catalogoVotos);
		handler.escolherVotacao(String.valueOf(votacao1.getId()));
		handler.lancarProprioVoto(TipoDeVoto.FAVOR);
		
		Optional<Votacao> possivelVotacao = catalogoVotacoes.getVotacaoById(votacao1.getId());
		Votacao votacao2 = possivelVotacao.get();
		
		assertEquals(2, votacao2.getaFavor());
	}
	
	
}