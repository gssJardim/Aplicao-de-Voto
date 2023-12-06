package pt.ul.fc.css.project.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pt.ul.fc.css.project.entities.Votacao;

public interface VotacaoRepository extends JpaRepository<Votacao, Long>{
	@Modifying
	@Query("UPDATE Votacao SET aFavor = aFavor + 1 WHERE id = :id")
	void supportVoteFavorById(@Param("id") Long id);

	@Modifying
	@Query("UPDATE Votacao T SET T.econtra = T.econtra + 1 WHERE T.id = :id")
	void supportVoteContraById(@Param("id") Long id);
}
