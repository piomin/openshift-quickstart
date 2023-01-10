package pl.redhat.samples.person.view;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import com.blazebit.persistence.view.Mapping;
import com.blazebit.persistence.view.UpdatableEntityView;
import pl.redhat.samples.person.domain.Person;

@EntityView(Person.class)
public interface PersonView {

    @IdMapping
    Integer getId();
    void setId(Integer id);

    @Mapping("CONCAT(firstName,' ',lastName)")
    String getName();
    void setName(String name);
}
