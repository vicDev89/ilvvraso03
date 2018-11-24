package de.berlin.htw.usws.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Setter(value = AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date createdOn;

    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date lastModifiedOn;

    @PrePersist
    public void prePersist() {
        createdOn = new Date();
        lastModifiedOn = new Date();
    }

    @PreUpdate
    public void preUpdate() {
        lastModifiedOn = new Date();
    }


}
