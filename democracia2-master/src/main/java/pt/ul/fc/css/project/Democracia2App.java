package pt.ul.fc.css.project;

import java.time.LocalDate;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import pt.ul.fc.css.project.entities.Cidadao;
import pt.ul.fc.css.project.entities.Delegado;
import pt.ul.fc.css.project.entities.ProjetoLei;
import pt.ul.fc.css.project.entities.Tema;
import pt.ul.fc.css.project.entities.Votacao;
import pt.ul.fc.css.project.entities.VotoPublico;
import pt.ul.fc.css.project.repositories.CidadaoRepository;
import pt.ul.fc.css.project.repositories.DelegadoRepository;
import pt.ul.fc.css.project.repositories.ProjetoLeiRepository;
import pt.ul.fc.css.project.repositories.TemaRepository;
import pt.ul.fc.css.project.repositories.VotacaoRepository;
import pt.ul.fc.css.project.repositories.VotoPublicoRepository;

@SpringBootApplication
@EnableWebMvc
public class Democracia2App {

	private static final Logger log = LoggerFactory.getLogger(Democracia2App.class);

	public static void main(String[] args) {
		SpringApplication.run(Democracia2App.class, args); 
	}


	@Bean
	public CommandLineRunner createVotacoes(VotacaoRepository votRep, TemaRepository temaRep, 
			DelegadoRepository delRep, ProjetoLeiRepository projRep, VotoPublicoRepository votPubRep, 
			CidadaoRepository cidRepo) {
		return (args) -> {
			//save few Votacoes
			Tema tema = new Tema(); //tema geral
			temaRep.save(tema);
			Delegado delegado = new Delegado("pedro", "123456789");
			delRep.save(delegado);

			ProjetoLei projetoLei = new ProjetoLei("titulo1", "texto1", "pdf", LocalDate.of(2024, 12, 12), tema, delegado);
			projetoLei.setNApoiantes(10000);
			projRep.save(projetoLei);
			if(projetoLei.getNApoiantes() == 10000) {
				log.info("PROJETO LEI ATINGIU OS 10000! A CRIAR A VOTACAO...");
				VotoPublico votoPublico = new VotoPublico(delegado);
				log.info("delegado: " + delegado);
				votPubRep.save(votoPublico);
				Votacao votacao = new Votacao(projetoLei, votoPublico);
				votRep.save(votacao);
			} else {
				//does nothing
			}
			//fetch all votacoes
			log.info("Votacoes found with findAll():");
			//			log.info("-------------------------------");
			for(Votacao votacao : votRep.findAll()) {
				log.info(votacao.toString());
			}
			log.info("");	

			// fetch an individual votacao by ID
			votRep.findById(1L).ifPresent((Votacao vot) ->{
				log.info("Votacao found with findById(1L):");
				log.info("--------------------------------");
				log.info(vot.toString());
				log.info("");
			});
		};
	}

	@Bean
	public CommandLineRunner createDelegados(DelegadoRepository repository, CidadaoRepository cRepository) {
		return (args) -> {
			// save a few delegates
			repository.save(new Delegado("afonso", "123456789"));
			repository.save(new Delegado("luis", "123443211"));
			repository.save(new Delegado("leonardo", "124343211"));

			//fetch all delegates
			log.info("Delegates found with findAll():");
			log.info("-------------------------------");
			for(Delegado delegado : repository.findAll()) {
				log.info(delegado.toString());
			}
			log.info("");

			// fetch an individual delegate by ID
			repository.findById(1L).ifPresent((Delegado delegado) ->{
				log.info("Delegado found with findById(1L):");
				log.info("--------------------------------");
				log.info(delegado.toString());
				log.info("");
			});
		};

	}
	@Bean
	public CommandLineRunner createTemas(TemaRepository temaRepository) {
		return (args) -> {
			// save a few Themes
			Optional<Tema> tema = temaRepository.findById((long) 1);  //tema mais geral
			Tema geral = tema.get();

			Tema educacao = new Tema("Educacao", geral);

			Tema ensinoSuperior = new Tema("Ensino Superior", educacao);

			Tema professores = new Tema("Professores", educacao);
			temaRepository.save(geral);
			temaRepository.save(educacao);
			temaRepository.save(ensinoSuperior);
			temaRepository.save(professores);


			// fetch all citizens
			log.info("Temas found with findAll():");
			log.info("-------------------------------");
			for (Tema temas : temaRepository.findAll()) {
				log.info(temas.toString());
			}
			log.info("");

			// fetch an individual customer by ID
			temaRepository.findById(1L).ifPresent((Tema temas) -> {
				log.info("Tema found with findById(1L):");
				log.info("--------------------------------");
				log.info(temas.toString());
				log.info("");
			});
		};
	}

	@Bean
	public CommandLineRunner createCitizens(CidadaoRepository repository) {
		return (args) -> {
			// save a few citizens
			repository.save(new Cidadao("Jack", "111111111"));
			repository.save(new Cidadao("Chloe", "111111112"));
			repository.save(new Cidadao("Kim", "111111113"));
			repository.save(new Cidadao("David", "111111114"));
			repository.save(new Cidadao("Michelle", "111111115"));

			// fetch all citizens
			log.info("Citizens found with findAll():");
			log.info("-------------------------------");
			for (Cidadao citizen : repository.findAll()) {
				log.info(citizen.toString());
			}
			log.info("");

			// fetch an individual customer by ID
			repository.findById(1L).ifPresent((Cidadao citizen) -> {
				log.info("Citizen found with findById(1L):");
				log.info("--------------------------------");
				log.info(citizen.toString());
				log.info("");
			});
		};
	}


	@Bean
	public CommandLineRunner createProjetosLei(ProjetoLeiRepository pjRep, DelegadoRepository delRep, TemaRepository temRep) {
		return (args) -> {
			Optional<Tema> tema = temRep.findById((long) 1);
			Tema geral = tema.get();
			
			Delegado delegado1 = new Delegado("Afonso", "123456789");
			delRep.save(delegado1);
			Tema tema1 = new Tema("Agricultura", geral);
			temRep.save(tema1);
			// save a few citizens
			ProjetoLei projetoLeiComApoios = new ProjetoLei("titulo1", "texto1", "pdf", LocalDate.now(), tema1, delegado1);
			projetoLeiComApoios.setNApoiantes(9999);
			pjRep.save(projetoLeiComApoios);
			
			Delegado delegado2 = new Delegado("Pedro" , "123456789");
			delRep.save(delegado2);
			Tema tema2 = new Tema("Saude", geral);
			temRep.save(tema2);
			pjRep.save(new ProjetoLei("titulo1", "texto1", "pdf", LocalDate.now(), tema2, delegado2));
			
			Delegado delegado3 = new Delegado("Afonso" , "929223019");
			delRep.save(delegado3);
			Tema tema3 = new Tema("Financas", geral);
			temRep.save(tema3);
			pjRep.save(new ProjetoLei("titulo2", "texto2", "pdf", LocalDate.now(), tema3, delegado3));
			
			Delegado delegado4 = new Delegado("Gustavo" , "914006056");
			delRep.save(delegado4);
			Tema tema4 = new Tema("Infraestruturas", geral);
			temRep.save(tema4);
			pjRep.save(new ProjetoLei("titulo3", "texto3", "pdf", LocalDate.now(), tema4, delegado4));
			
			Delegado delegado5 = new Delegado("Luis" , "987654321");
			delRep.save(delegado5);
			Tema tema5 = new Tema("Educacao", geral);
			temRep.save(tema5);
			pjRep.save(new ProjetoLei("titulo4", "texto4", "pdf", LocalDate.now(), tema5, delegado5));
			
			Delegado delegado6 = new Delegado("Daniel" , "192837465");
			delRep.save(delegado6);
			Tema tema6 = new Tema("Ambiente", geral);
			temRep.save(tema6);
			pjRep.save(new ProjetoLei("titulo5", "texto5", "pdf", LocalDate.now(), tema6, delegado6));
			
			
			temRep.save(geral);
			// fetch all citizens
			log.info("ProjetoLei found with findAll():");
			log.info("-------------------------------");
			for (ProjetoLei projetoLei : pjRep.findAll()) {
				log.info(projetoLei.toString());
			}
			log.info("");

			// fetch an individual customer by ID
			pjRep.findById(1L).ifPresent((ProjetoLei projetoLei) -> {
				log.info("ProjetoLei found with findById(1L):");
				log.info("--------------------------------");
				log.info(projetoLei.toString());
				log.info("");
			});
		};
	}
}