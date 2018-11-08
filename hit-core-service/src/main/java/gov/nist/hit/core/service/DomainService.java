package gov.nist.hit.core.service;

import java.util.List;

import org.springframework.security.core.Authentication;

import gov.nist.hit.core.domain.Domain;
import gov.nist.hit.core.domain.TestScope;
import gov.nist.hit.core.service.exception.DomainException;

public interface DomainService {

	public Domain findOneByKey(String key);

	public void save(Domain domain);

	// List<Domain> findShortAll(boolean disabled);

	List<Domain> findShortAllByScopeAndAuthorname(TestScope scope, String authorname);

	List<Domain> findShortAllByAuthorname(String authorname);

	Domain findOne(Long id);

	void delete(Domain domain);

	List<Domain> findShortAllWithGlobalOrAuthornameOrParticipantEmail(String authorname, String participantEmail);

	List<Domain> findShortAllWithAuthornameOrParticipantEmail(String authorname, String participantEmail);

	public List<Domain> findShortAllGlobalDomains();

	public void deletePreloaded();

	public void hasPermission(String domainKey, Authentication auth) throws Exception;

	void canPublish(String domainKey, Authentication auth) throws DomainException;

	void canDelete(String domainKey, Authentication auth) throws DomainException;

	List<Domain> findShortAll();

}
