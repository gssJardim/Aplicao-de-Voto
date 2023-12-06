package pt.ul.fc.css.project;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.repository.CrudRepository;

import pt.ul.fc.css.project.catalogos.CatalogoCidadaos;
import pt.ul.fc.css.project.catalogos.CatalogoVotos;
import pt.ul.fc.css.project.catalogos.CatalogoDelegados;
import pt.ul.fc.css.project.catalogos.CatalogoDelegadosEscolhidos;
import pt.ul.fc.css.project.catalogos.CatalogoProjetoLei;
import pt.ul.fc.css.project.catalogos.CatalogoTemas;
import pt.ul.fc.css.project.catalogos.CatalogoVotacoes;
import pt.ul.fc.css.project.entities.Delegado;
import pt.ul.fc.css.project.entities.Cidadao;
import pt.ul.fc.css.project.entities.ProjetoLei;
import pt.ul.fc.css.project.entities.Tema;
import pt.ul.fc.css.project.entities.Votacao;
import pt.ul.fc.css.project.entities.VotoPrivado;
import pt.ul.fc.css.project.entities.VotoPublico;
import pt.ul.fc.css.project.entities.enumerated.EstadoProjetoLei;
import pt.ul.fc.css.project.entities.enumerated.TipoDeVoto;
import pt.ul.fc.css.project.entities.facade.ProjetoLeiDTO;
import pt.ul.fc.css.project.entities.facade.VotacaoDTO;
import pt.ul.fc.css.project.repositories.*;
import pt.ul.fc.css.project.handlers.*;

@SpringBootTest
public class ApoiarProjetoLeiTest {

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
		this.catalogoCidadaos = new CatalogoCidadaos();
		this.catalogoDelegados = new CatalogoDelegados();
		this.catalogoTemas = new CatalogoTemas();
		this.catalogoProjetosLei = new CatalogoProjetoLei();
		this.catalogoVotacoes = new CatalogoVotacoes();
		this.catalogoVotos = new CatalogoVotos(votoPublicoRepository, votoPrivadoRepository);
		this.catalogoDelegadosEscolhidos = new CatalogoDelegadosEscolhidos();
	}

	@Test
	public void TestarApoiarProjetoLei() {
		inicializarCatalogos();

		Delegado delegado1 = new Delegado("Afonso","253150698");
		delegadoRepository.save(delegado1);

		Cidadao cidadao1 = new Cidadao();
		cidadaoRepository.save(cidadao1);

		Tema tema1 = new Tema("Saude");
		temaRepository.save(tema1);

		ProjetoLei projetoLei1 = new ProjetoLei("titulo","texto","anexo",LocalDateTime.of(2024, 11, 20, 8, 55) ,tema1 ,delegado1);
		projetoLeiRepository.save(projetoLei1);


		ApoiarProjetoLeiHandler handler = new ApoiarProjetoLeiHandler(
				cidadao1 ,catalogoProjetosLei, catalogoVotacoes, catalogoVotos, projetoLei1 );

		handler.apoiarProjetoLei();

		Optional<ProjetoLei> possivelProjetoLei = projetoLeiRepository.findById(projetoLei1.getId());
		ProjetoLei projetoLeiAtualizado = possivelProjetoLei.get();

		int nApoiantes = projetoLeiAtualizado.getNApoiantes();

		assertEquals(nApoiantes, 2);

	}

	//Testa se depois de 10000 apoios, eh criada a votacao
	@Test
	public void ApoiarProjetoLei10000Vezes() {
		inicializarCatalogos();
		ApoiarProjetoLeiHandler handler;

		Delegado delegado1 = new Delegado("Afonso","253150698");
		delegadoRepository.save(delegado1);

		Cidadao cidadao1;

		Tema tema1 = new Tema("Saude");
		temaRepository.save(tema1);

		ProjetoLei projetoLei1 = new ProjetoLei("titulo","texto","anexo",LocalDateTime.of(2024, 11, 20, 8, 55) ,tema1 ,delegado1);
		projetoLei1.setNApoiantes(9999);
		projetoLeiRepository.save(projetoLei1);

		
		cidadao1 = catalogoCidadaos.createCidadao("nome1", "ncc1");
		cidadaoRepository.save(cidadao1);
		
		Optional<ProjetoLei> possivelPossivel = projetoLeiRepository.findById(projetoLei1.getId());
		ProjetoLei projetoLei = possivelPossivel.get();
		handler = new ApoiarProjetoLeiHandler(
				cidadao1 ,catalogoProjetosLei, catalogoVotacoes, catalogoVotos, projetoLei );
		handler.apoiarProjetoLei();

		assertEquals(10000, projetoLei.getNApoiantes());
	}

	@Test
	public void TestarEstadoProjetoLeiDepoisDe10000Apoios() {
		inicializarCatalogos();
		ApoiarProjetoLeiHandler handler;

		Delegado delegado1 = new Delegado("Afonso","253150698");
		delegadoRepository.save(delegado1);

		Cidadao cidadao1;

		Tema tema1 = new Tema("Saude");
		temaRepository.save(tema1);

		ProjetoLei projetoLei1 = new ProjetoLei("titulo","texto","anexo",LocalDateTime.of(2024, 11, 20, 8, 55) ,tema1 ,delegado1);
		projetoLei1.setNApoiantes(9999);
		projetoLeiRepository.save(projetoLei1);

		
		cidadao1 = catalogoCidadaos.createCidadao("nome1", "ncc1");
		cidadaoRepository.save(cidadao1);
		
		Optional<ProjetoLei> possivelPossivel = projetoLeiRepository.findById(projetoLei1.getId());
		ProjetoLei projetoLei = possivelPossivel.get();
		handler = new ApoiarProjetoLeiHandler(
				cidadao1 ,catalogoProjetosLei, catalogoVotacoes, catalogoVotos, projetoLei );
		handler.apoiarProjetoLei();

		assertEquals(EstadoProjetoLei.EM_VOTACAO, projetoLei.getEstado());
	}

	@Test
	public void TestarApoioDeUmCidadaoQueJaApoiou() {
		inicializarCatalogos();
		ApoiarProjetoLeiHandler handler;

		Delegado delegado1 = new Delegado("Afonso","253150698");
		delegadoRepository.save(delegado1);

		Cidadao cidadao1 = new Cidadao();
		cidadaoRepository.save(cidadao1);

		Tema tema1 = new Tema("Saude");
		temaRepository.save(tema1);

		ProjetoLei projetoLei1 = new ProjetoLei("titulo","texto","anexo",LocalDateTime.of(2024, 11, 20, 8, 55) ,tema1 ,delegado1);
		projetoLeiRepository.save(projetoLei1);
		
		for (int i = 0; i < 2; i++) {
			Optional<ProjetoLei> possivelPossivel = projetoLeiRepository.findById(projetoLei1.getId());
			ProjetoLei projetoLei = possivelPossivel.get();
			handler = new ApoiarProjetoLeiHandler(
					cidadao1 ,catalogoProjetosLei, catalogoVotacoes, catalogoVotos, projetoLei);
			handler.apoiarProjetoLei();
		}
		
		Optional<ProjetoLei> possivelPossivel = projetoLeiRepository.findById(projetoLei1.getId());
		ProjetoLei projetoLei = possivelPossivel.get();

		assertEquals(2, projetoLei.getNApoiantes());
	}
	
	@Test
	public void verificarSeCriouVotacao() {
		inicializarCatalogos();
		votacaoRepository.deleteAll(); //apaga as duas votacoes criadas pelos outros testes
		ApoiarProjetoLeiHandler handler;

		Delegado delegado1 = new Delegado("Afonso","253150698");
		delegadoRepository.save(delegado1);

		Cidadao cidadao1;

		Tema tema1 = new Tema("Saude");
		temaRepository.save(tema1);

		ProjetoLei projetoLei1 = new ProjetoLei("titulo","texto","anexo",LocalDateTime.of(2024, 11, 20, 8, 55) ,tema1 ,delegado1);
		projetoLei1.setNApoiantes(9999);
		projetoLeiRepository.save(projetoLei1);

		
		cidadao1 = catalogoCidadaos.createCidadao("nome1", "ncc1");
		cidadaoRepository.save(cidadao1);
		
		Optional<ProjetoLei> possivelPossivel = projetoLeiRepository.findById(projetoLei1.getId());
		ProjetoLei projetoLei = possivelPossivel.get();
		handler = new ApoiarProjetoLeiHandler(
				cidadao1 ,catalogoProjetosLei, catalogoVotacoes, catalogoVotos, projetoLei );
		handler.apoiarProjetoLei();
		
		List<Votacao> votacoes = votacaoRepository.findAll();
		

		assertEquals(1, votacoes.size());
	}
}
