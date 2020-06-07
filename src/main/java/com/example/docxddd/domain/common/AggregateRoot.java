package com.example.docxddd.domain.common;

import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

@MappedSuperclass
public abstract class AggregateRoot extends BaseEntity {

//    @Getter
//    @Transient
//    private List<DomainEvent> domainEvents = new ArrayList<>();

    @Version
    private Integer version;

//    public void registerEvent(DomainEvent domainEvent) {
//        domainEvents.add(domainEvent);
//    }
}
