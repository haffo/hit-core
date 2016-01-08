package gov.nist.hit.core.repo;


import gov.nist.hit.core.domain.TransportMessage;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TransportMessageRepository extends JpaRepository<TransportMessage, Long> {

}
