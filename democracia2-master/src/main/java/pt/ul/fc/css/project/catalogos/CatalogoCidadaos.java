package pt.ul.fc.css.project.catalogos;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pt.ul.fc.css.project.entities.Cidadao;
import pt.ul.fc.css.project.entities.facade.CidadaoDTO;
import pt.ul.fc.css.project.entities.facade.DelegadoEscolhidoDTO;
import pt.ul.fc.css.project.handlers.EscolherDelegadoHandler;
import pt.ul.fc.css.project.handlers.VotarNumaPropostaHandler;
import pt.ul.fc.css.project.repositories.CidadaoRepository;
import pt.ul.fc.css.project.repositories.DelegadoEscolhidoRepository;
import pt.ul.fc.css.project.repositories.DelegadoRepository;
import pt.ul.fc.css.project.repositories.TemaRepository;
@Component
public class CatalogoCidadaos {
	@Autowired
	private CidadaoRepository cidadaoRepository;
	@Autowired
	private DelegadoRepository delegadoRepository;
	@Autowired
	private DelegadoEscolhidoRepository delegadoEscolhidoRepository;
	@Autowired
	private TemaRepository temaRepository;
	@Autowired
	private VotarNumaPropostaHandler votarNumaPropostaHandler;
	@Autowired
	private EscolherDelegadoHandler escolherDelegadoHandler;
	

	public CidadaoDTO createCidadao(String nome, String ncc) {
		if (nome.isEmpty()) {
            //throw new EmptyFieldsException("Name is a required field");
        }
        if (ncc.isEmpty()) {
            //throw new EmptyFieldsException("Vat is a required field");
        }

        try {
            Integer.parseInt(ncc);
        } catch (NumberFormatException e) {
            //throw new InvalidNCCException();
        }

        if (ncc.length() != 9) {
            //throw new InvalidNCCException();
        }

        Cidadao cidadao = new Cidadao();
        cidadao.setNome(nome);
        cidadao.setnCartaoCidadao(ncc);
		cidadaoRepository.save(cidadao);
		return cidadao.toDTO();
	}

	public void deleteCidadao(Long id) {
		cidadaoRepository.findById(id).ifPresent(cidadaoRepository::delete);
	}
	
	public Optional<CidadaoDTO> getCidadao(Long id) {
		return cidadaoRepository.findById(id).map(c -> c.toDTO());
	}
	
	public List<CidadaoDTO> getCidadaos(){
		ArrayList<CidadaoDTO> cidadaosDTO = new ArrayList<>();
		for (Cidadao cidadao : cidadaoRepository.findAll()) {
			CidadaoDTO cidadaoDTO = cidadao.toDTO();
			cidadaosDTO.add(cidadaoDTO);
		}
		return cidadaosDTO;
	}
	
	public DelegadoEscolhidoDTO escolherDelegado(Long cidadaoId, Long delegadoId, Long temaId) throws Exception{
		return escolherDelegadoHandler.escolherDelegado(cidadaoId, delegadoId, temaId);
	}
}
