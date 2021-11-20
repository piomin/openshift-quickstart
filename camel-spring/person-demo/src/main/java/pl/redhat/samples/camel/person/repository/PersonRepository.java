package pl.redhat.samples.camel.person.repository;

import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import pl.redhat.samples.camel.person.domain.Person;

public interface PersonRepository extends CrudRepository<Person, Integer> {

    @Procedure(procedureName = "COUNT_OLDER_THAN_4", outputParameterName = "COUNT_OUT")
    int countOlderThen(@Param("age_in") int ageIn);
}
