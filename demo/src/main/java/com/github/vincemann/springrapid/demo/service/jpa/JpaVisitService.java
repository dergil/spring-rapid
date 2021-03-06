package com.github.vincemann.springrapid.demo.service.jpa;

import com.github.vincemann.springrapid.demo.model.Visit;
import com.github.vincemann.springrapid.demo.repo.VisitRepository;
import com.github.vincemann.springrapid.demo.service.VisitService;
import com.github.vincemann.springrapid.core.slicing.components.ServiceComponent;
import com.github.vincemann.springrapid.core.service.JPACrudService;
import org.springframework.aop.TargetClassAware;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Primary
@Service
@ServiceComponent
public class JpaVisitService extends JPACrudService<Visit,Long, VisitRepository> implements VisitService, TargetClassAware {
    @Override
    public Class<?> getTargetClass() {
        return JpaVisitService.class;
    }
}
