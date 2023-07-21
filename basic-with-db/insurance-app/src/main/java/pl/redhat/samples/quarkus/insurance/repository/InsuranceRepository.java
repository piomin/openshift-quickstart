package pl.redhat.samples.quarkus.insurance.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import pl.redhat.samples.quarkus.insurance.model.Insurance;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class InsuranceRepository implements PanacheRepository<Insurance> {
}
