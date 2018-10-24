package de.berlin.htw.usws.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.Date;

@Getter
@Setter
@Entity
public class Opinion {

    private String author;

    private Date date;

    private String rate;

    @ManyToOne
    private Recipe recipe;
}
