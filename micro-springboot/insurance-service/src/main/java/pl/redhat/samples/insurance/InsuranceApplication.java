package pl.redhat.samples.insurance;

import com.blazebit.persistence.Criteria;
import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.integration.view.spring.EnableEntityViews;
import com.blazebit.persistence.spi.CriteriaBuilderConfiguration;
import com.blazebit.persistence.spring.data.repository.config.EnableBlazeRepositories;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.spi.EntityViewConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

@SpringBootApplication
@EnableBlazeRepositories
@EnableEntityViews
public class InsuranceApplication {

    public static void main(String[] args) {
        SpringApplication.run(InsuranceApplication.class, args);
    }

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplateBuilder().build();
    }

    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    @Bean
    public CriteriaBuilderFactory createCriteriaBuilderFactory() {
        CriteriaBuilderConfiguration config = Criteria.getDefault();
        return config.createCriteriaBuilderFactory(entityManagerFactory);
    }

    @Bean
    public EntityViewManager createEntityViewManager(
            CriteriaBuilderFactory criteriaBuilderFactory, EntityViewConfiguration entityViewConfiguration) {
        return entityViewConfiguration.createEntityViewManager(criteriaBuilderFactory);
    }
}
