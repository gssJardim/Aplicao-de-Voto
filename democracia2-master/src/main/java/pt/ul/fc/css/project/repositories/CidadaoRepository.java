package pt.ul.fc.css.project.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pt.ul.fc.css.project.entities.Cidadao;

public interface CidadaoRepository extends JpaRepository<Cidadao, Long>{
//	@Query("SELECT a FROM Cidadao a WHERE a.nCartaoCidadao = :nCartaoCidadao")
//    List<Cidadao> findByNif(@Param("nCartaoCidadao") String nCartaoCidadao);
//	
//	@Query("SELECT a FROM Cidadao a WHERE a.delegate = true")
//	List<Cidadao> findAllDelegates();
}