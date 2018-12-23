package de.berlin.htw.usws.repositories;

import de.berlin.htw.usws.model.Protokoll;
import org.apache.deltaspike.data.api.AbstractFullEntityRepository;
import org.apache.deltaspike.data.api.Repository;

@Repository(forEntity = Protokoll.class)
public abstract class ProtokollRepository  extends AbstractFullEntityRepository<Protokoll, Long> {
}
