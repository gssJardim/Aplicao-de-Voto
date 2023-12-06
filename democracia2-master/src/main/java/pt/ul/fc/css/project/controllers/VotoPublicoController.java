//package pt.ul.fc.css.project.controllers;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.lang.NonNull;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.server.ResponseStatusException;
//
//import pt.ul.fc.css.project.entities.Tema;
//import pt.ul.fc.css.project.entities.VotoPublico;
//import pt.ul.fc.css.project.repositories.DelegadoRepository;
//import pt.ul.fc.css.project.repositories.TemaRepository;
//import pt.ul.fc.css.project.repositories.VotoPublicoRepository;
//import pt.ul.fc.css.project.entities.enumerated.*;
//
//@RestController
//@RequestMapping("/api")
//public class VotoPublicoController {
//
////	private final VotoPublicoRepository votPubRep;
////	private final DelegadoRepository delRep;
////	
////	public VotoPublicoController(DelegadoRepository delRep, VotoPublicoRepository votPubRep) {
////		this.votPubRep = votPubRep;
////		this.delRep = delRep;
////	}
////	
////	@PostMapping("/votopublico")
////	public VotoPublico postTema(@NonNull @RequestBody VotoPublico votPub) {
////		Long delegadoId = votPub.getDelegado().getId();
////		boolean delegadoExists = delRep.existsById(delegadoId);
////		if(!delegadoExists) {
////			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Delegado not found");
////		}
////		//Continuar
////		VotoPublico votoPublico = null;
////		return votoPublico;
////		
////	}
//}
