package management.data;

import jakarta.transaction.Transactional;
import management.objectData.Position;
import management.objectData.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PositionRepository extends CrudRepository<Position, String> {
    Position findByName(String name);
    @Modifying
    @Query("update Position set name = :newName where id = :id")
    void updatePosition(@Param("newName") String rename, @Param("id") int id);
    void deleteByName(String name);
}
