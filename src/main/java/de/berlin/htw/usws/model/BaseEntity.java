package de.berlin.htw.usws.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity {

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created")
	private Date createdOn;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated")
	private Date lastModifiedOn;

    @PrePersist
    public void prePersist() {
        createdOn = new Date();
        lastModifiedOn = new Date();
    }
    @PreUpdate
    public void preUpdate()
    {
        lastModifiedOn = new Date();
    }
    

}
