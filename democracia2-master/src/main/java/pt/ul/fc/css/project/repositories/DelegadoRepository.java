package pt.ul.fc.css.project.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pt.ul.fc.css.project.entities.Cidadao;
import pt.ul.fc.css.project.entities.Delegado;

public interface DelegadoRepository extends JpaRepository<Delegado, Long> {
//	@Query("SELECT a FROM Delegado a WHERE a.nCartaoCidadao = :nCartaoCidadao")
//    List<Delegado> findByNif(@Param("nCartaoCidadao") String nCartaoCidadao);
}
