package pt.ul.fc.css.project.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pt.ul.fc.css.project.entities.ProjetoLei;

public interface ProjetoLeiRepository extends JpaRepository<ProjetoLei, Long>{
	@Query("SELECT T FROM ProjetoLei T WHERE T.dataValidade > CURRENT_DATE")
    List<ProjetoLei> findNonExpired();

	@Query("SELECT T FROM ProjetoLei T WHERE T.dataValidade < CURRENT_DATE AND nApoiantes < 10000")
	List<ProjetoLei> findNonExpiredInSupport();

	@Query("SELECT new pt.ul.fc.css.project.entities.ProjetoLei(T.id, T.titulo) FROM ProjetoLei T WHERE T.dataValidade < CURRENT_DATE AND nApoiantes >= 10000")
	List<ProjetoLei> findNonExpiredInVoting();

	@Modifying
	@Query("UPDATE ProjetoLei T SET T.nApoiantes = T.nApoiantes + 1 WHERE T.id = :id")
	void supportProjectById(@Param("id") Long id);

}
