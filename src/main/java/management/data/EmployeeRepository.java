package management.data;

import management.objectData.Employee;
import management.objectData.Position;
import management.objectData.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends CrudRepository<Employee, Integer> {

    List<Employee> findAllByUserAndPosition(User user, Position position);

    void deleteByUserAndId(User user, int id);

    @Modifying
    @Query("update Employee set firstName = :firstName, middleName = :middleName, lastName = :lastName, date = :date, position = :position where id = :id")
    void updateEmployee(@Param("firstName") String firstName, @Param("middleName") String middleName,
                        @Param("lastName") String lastName, @Param("date") String date, @Param("position") Position position, @Param("id") int id);

}
