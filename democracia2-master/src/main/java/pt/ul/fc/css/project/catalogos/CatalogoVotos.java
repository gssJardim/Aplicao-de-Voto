//package pt.ul.fc.css.project.catalogos;
//
//import java.util.List;
//import java.util.Optional;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import pt.ul.fc.css.project.entities.Cidadao;
//import pt.ul.fc.css.project.entities.Delegado;
//import pt.ul.fc.css.project.entities.Votacao;
//import pt.ul.fc.css.project.entities.Voto;
//import pt.ul.fc.css.project.entities.VotoPrivado;
//import pt.ul.fc.css.project.entities.VotoPublico;
//import pt.ul.fc.css.project.entities.enumerated.TipoDeVoto;
//import pt.ul.fc.css.project.repositories.VotacaoRepository;
//import pt.ul.fc.css.project.repositories.VotoPrivadoRepository;
//import pt.ul.fc.css.project.repositories.VotoPublicoRepository;
//
//@Component
//public class CatalogoVotos {
//	@Autowired
//	private VotoPublicoRepository votoPublicoRepository;
//	@Autowired
//	private VotoPrivadoRepository votoPrivadoRepository;
//	
//	
//	public VotoPrivado criarVotoPrivado(TipoDeVoto tipoDeVoto) {
//		return votoPrivadoRepository.save(new VotoPrivado(tipoDeVoto));
//	}
//	
//	public Optional<VotoPrivado> getVotoPrivadoById(long id) {
//		return votoPrivadoRepository.findById(id);
//	}
//	
//	
//	
//	public List<VotoPublico> getVotosPublicos() {
//		return votoPublicoRepository.findAll();
//	}
//	
//	public VotoPublico criarVotoProponente(Delegado proponente) {
//		return votoPublicoRepository.save(new VotoPublico(proponente));
//	}
//	
//	public Optional<VotoPublico> getVotoPublicoById(long id) {
//		return votoPublicoRepository.findById(id);
//	}
//	
//	public void incVotoPublico(VotoPublico votoPublico) {
//		votoPublico.inc();
//		votoPublicoRepository.save(votoPublico);
//	}
////
////	public VotoPrivado aceitarVotoDelegado(TipoDeVoto votoDoDelegado) {
////		VotoPrivado novoVotoPrivado = new VotoPrivado(votoDoDelegado);
////		//votoRepository.save(novoVotoPrivado);
////		return novoVotoPrivado;
////	}
////
////	public VotoPublico criarVotoProponente(Delegado proponente) {
////		return votoPublicoRepository.save(new VotoPublico(proponente));
////	}
////
////	public VotoPublico criarVotoPublico(Delegado proponente, TipoDeVoto tipoDeVoto) {
////		VotoPublico votoP = new VotoPublico(proponente, tipoDeVoto);
////		votoPublicoRepository.save(votoP);
////		return votoP;
////	}
////
////
////	public List<VotoPrivado> getVotosPublicos() {
////		return votoPublicoRepository.findAll();
////	}
////
////	public Optional<VotoPublico> getVotoPublicoById(long votoDoDelegadoID) {
////		return votoPublicoRepository.findById(votoDoDelegadoID);
////	}
////	
//
//
//
//	
//}
