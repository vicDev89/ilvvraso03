package de.berlin.htw.usws.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonIgnore
    @Setter(value = AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date createdOn;

    @JsonIgnore
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
