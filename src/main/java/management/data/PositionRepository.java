package management.data;

import management.objectData.Position;
import management.objectData.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PositionRepository extends CrudRepository<Position, String> {
    Position findByUserAndName(User user, String name);

    void deleteByUserAndName(User user, String name);
}
