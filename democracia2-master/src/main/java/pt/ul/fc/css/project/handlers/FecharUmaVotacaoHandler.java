package pt.ul.fc.css.project.handlers;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pt.ul.fc.css.project.entities.Cidadao;
import pt.ul.fc.css.project.entities.Delegado;
import pt.ul.fc.css.project.entities.DelegadoEscolhido;
import pt.ul.fc.css.project.entities.Tema;
import pt.ul.fc.css.project.entities.Votacao;
import pt.ul.fc.css.project.entities.VotoPublico;
import pt.ul.fc.css.project.entities.enumerated.EstadoProjetoLei;
import pt.ul.fc.css.project.entities.enumerated.EstadoVotacao;
import pt.ul.fc.css.project.handlers.exceptions.EmptyFieldsException;
import pt.ul.fc.css.project.repositories.CidadaoRepository;
import pt.ul.fc.css.project.repositories.VotacaoRepository;
import pt.ul.fc.css.project.repositories.VotoPublicoRepository;
@Component
public class FecharUmaVotacaoHandler {
	@Autowired
	private VotacaoRepository votacaoRepository;
	@Autowired
	private CidadaoRepository cidadaoRepository;
	@Autowired
	private VotoPublicoRepository votoPublicoRepository;

	private static final Logger log = LoggerFactory.getLogger(FecharUmaVotacaoHandler.class);


	public void fecharVotacoes() throws EmptyFieldsException {
		for (Votacao votacao : votacaoRepository.findAll()) {
			if(votacao.getDataFecho().isBefore(LocalDate.now()))
				votacao.getProjetoLei().setEstado(EstadoProjetoLei.FECHADO);

			for (Cidadao cidadao : cidadaoRepository.findAll()) {   //atribuir o voto do delegado aos cidadaos que não votaram
				log.info(cidadao.toString());
				if(!votacao.getCidadaosQueJaVotaram().contains(cidadao)) {
					incVotoDoDelegadoDoCidadao(cidadao, votacao.getProjetoLei().getTema(), votacao);
				}
			}
			contarVotosDosDelegados(votacao);
			if(votacao.getaFavor() > votacao.getEcontra())
				votacao.setEstadoVotacao(EstadoVotacao.APROVADO);
			else
				votacao.setEstadoVotacao(EstadoVotacao.REJEITADO);
			votacaoRepository.save(votacao);
		}
	}


		private boolean incVotoDoDelegadoDoCidadao(Cidadao cidadao, Tema tema, Votacao votacao) {
			log.info("incVotoDoDelegadoDoCidadao: " + cidadao.toString());
			log.info("");
			log.info("delegados escolhidos: " + cidadao.getDelegadosEscolhidos());
			if(tema.getNome().equals("Geral")) {
				for (DelegadoEscolhido delegadoEscolhido : cidadao.getDelegadosEscolhidos()) {
					if(delegadoEscolhido.getTema().equals(tema)) {
						if(incVoto(votacao, delegadoEscolhido.getDelegado()))
							return true;
						else {
							return false;
						}
					}else {
						return false;
					}
				}
			}else {
				for (DelegadoEscolhido delegadoEscolhido : cidadao.getDelegadosEscolhidos()) {
					if(delegadoEscolhido.getTema().equals(tema)) {
						if(incVoto(votacao, delegadoEscolhido.getDelegado()))
							return true;
						else {
							return incVotoDoDelegadoDoCidadao(cidadao, tema.getSupTema(), votacao);
						}
					}else {
						return incVotoDoDelegadoDoCidadao(cidadao, tema.getSupTema(), votacao);
					}
				}
			}
			return false;
		}


		private boolean incVoto(Votacao votacao, Delegado delegado) {
			for (VotoPublico voto : votacao.getVotosDeDelegados()) {
				if(voto.getDelegado().equals(delegado)) {
					voto.inc();
					votoPublicoRepository.save(voto);
					return true;
				}
			}
			return false;
		}

		private void contarVotosDosDelegados(Votacao votacao) {
			for (VotoPublico votosPublicos : votacao.getVotosDeDelegados()) {
				votacao.contarVotosPublicos(votosPublicos);
			}

		}

}
