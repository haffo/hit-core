package gov.nist.hit.core.repo;

import gov.nist.hit.core.domain.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
