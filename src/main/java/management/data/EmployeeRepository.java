package management.data;

import management.Employee;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.lang.annotation.Retention;

@Repository
public interface EmployeeRepository extends CrudRepository<Employee, Integer> {
}
