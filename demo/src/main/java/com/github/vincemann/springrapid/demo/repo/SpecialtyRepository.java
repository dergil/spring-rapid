package com.github.vincemann.springrapid.demo.repo;

import com.github.vincemann.springrapid.core.slicing.components.ServiceComponent;
import com.github.vincemann.springrapid.demo.model.Specialty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@ServiceComponent
public interface SpecialtyRepository extends JpaRepository<Specialty,Long> {
}
