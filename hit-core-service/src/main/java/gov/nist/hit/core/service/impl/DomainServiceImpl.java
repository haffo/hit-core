package gov.nist.hit.core.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import gov.nist.auth.hit.core.domain.Account;
import gov.nist.hit.core.domain.Domain;
import gov.nist.hit.core.domain.TestScope;
import gov.nist.hit.core.repo.AppInfoRepository;
import gov.nist.hit.core.repo.CFTestPlanRepository;
import gov.nist.hit.core.repo.CFTestStepGroupRepository;
import gov.nist.hit.core.repo.CFTestStepRepository;
import gov.nist.hit.core.repo.ConstraintsRepository;
import gov.nist.hit.core.repo.DocumentRepository;
import gov.nist.hit.core.repo.DomainRepository;
import gov.nist.hit.core.repo.IntegrationProfileRepository;
import gov.nist.hit.core.repo.MessageRepository;
import gov.nist.hit.core.repo.TestCaseDocumentationRepository;
import gov.nist.hit.core.repo.TestPlanRepository;
import gov.nist.hit.core.repo.TestStepRepository;
import gov.nist.hit.core.repo.TransportFormsRepository;
import gov.nist.hit.core.repo.VocabularyLibraryRepository;
import gov.nist.hit.core.service.AccountService;
import gov.nist.hit.core.service.DomainService;
import gov.nist.hit.core.service.UserService;
import gov.nist.hit.core.service.exception.DomainException;
import gov.nist.hit.core.service.exception.NoUserFoundException;

@Service
public class DomainServiceImpl implements DomainService {

	@Autowired
	protected DomainRepository domainRepo;

	@Autowired
	private UserService userService;

	@Autowired
	protected TransportFormsRepository transportFormsRepository;

	@Autowired
	protected IntegrationProfileRepository integrationProfileRepository;

	@Autowired
	protected MessageRepository messageRepository;

	@Autowired
	protected ConstraintsRepository constraintsRepository;

	@Autowired
	protected AppInfoRepository appInfoRepository;

	@Autowired
	protected DocumentRepository documentRepository;

	@Autowired
	protected TestPlanRepository testPlanRepository;

	@Autowired
	protected CFTestStepRepository cfTestStepRepository;

	@Autowired
	protected CFTestStepGroupRepository cfTestSteGroupRepository;

	@Autowired
	protected CFTestPlanRepository cfTestPlanRepository;

	@Autowired
	protected TestStepRepository testStepRepository;

	@Autowired
	protected TestCaseDocumentationRepository testCaseDocumentationRepository;

	@Autowired
	protected VocabularyLibraryRepository vocabularyLibraryRepository;

	@Autowired
	private AccountService accountService;

	@Override
	public Domain findOneByKey(String key) {
		// TODO Auto-generated method stub
		return domainRepo.findOneByKey(key);
	}

	@Override
	public void save(Domain entry) {
		domainRepo.saveAndFlush(entry);
	}

	// @Override
	// public List<Domain> findShortAll(boolean disabled) {
	// // TODO Auto-generated method stub
	// return domainRepo.findShortAll(disabled);
	// }

	@Override
	public List<Domain> findShortAllByScopeAndAuthorname(TestScope scope, String authorUsername) {
		// TODO Auto-generated method stub
		return domainRepo.findShortAllByScopeAndAuthorname(scope, authorUsername);
	}

	@Override
	public Domain findOne(Long id) {
		// TODO Auto-generated method stub
		return domainRepo.findOne(id);
	}

	@Override
	public void delete(Domain domain) {
		String d = domain.getDomain();
		testPlanRepository.deleteByDomain(d);
		cfTestPlanRepository.deleteByDomain(d);
		vocabularyLibraryRepository.deleteByDomain(d);
		constraintsRepository.deleteByDomain(d);
		integrationProfileRepository.deleteByDomain(d);
		transportFormsRepository.deleteByDomain(d);
		documentRepository.deleteByDomain(d);
		domainRepo.delete(domain);
	}

	@Override
	public List<Domain> findShortAllWithGlobalOrAuthornameOrParticipantEmail(String authorUsername,
			String participantEmail) {

		// TODO Auto-generated method stub
		List<Domain> all = domainRepo.findAll();
		List<Domain> results = null;
		if (all != null && !all.isEmpty()) {
			results = new ArrayList<Domain>();
			for (Domain dom : all) {
				if (TestScope.GLOBAL.equals(dom.getScope()) || authorUsername.equals(dom.getAuthorUsername())
						|| (dom.getParticipantEmails() != null
								&& dom.getParticipantEmails().contains(participantEmail))) {
					results.add(dom);
				}
			}
		}
		return results;
	}

	@Override
	public List<Domain> findShortAllGlobalDomains() {
		return domainRepo.findAllShortWithGlobal();
	}

	@Override
	public void deletePreloaded() {
		domainRepo.deletePreloaded();

	}

	@Override
	public void hasPermission(String domainKey, Authentication auth) throws DomainException {
		try {
			String username = auth.getName();
			if (!"app".equalsIgnoreCase(domainKey)) {
				Domain domain = findOneByKey(domainKey);
				if (domain == null) {
					throw new DomainException("Unknown domain " + domainKey);
				}
				if (!domain.getAuthorUsername().equals(auth.getName()) && !userService.isAdmin(username)) {
					Account account = accountService.findByTheAccountsUsername(auth.getName());
					if (account != null) {
						String email = account.getEmail();
						if (!userService.isAdminByEmail(email)) {
							throw new DomainException("You do not have the permission to perform this operation");
						}

					} else {
						throw new DomainException("This operation is not supported by this tool");
					}
				}
			} else {
				if (!userService.isAdmin(username)) {
					Account account = accountService.findByTheAccountsUsername(auth.getName());
					if (account != null) {
						String email = account.getEmail();
						if (!userService.isAdminByEmail(email)) {
							throw new DomainException("You do not have the permission to perform this operation");
						}
					} else {
						throw new DomainException("This operation is not supported by this tool");
					}
				}
			}
		} catch (NoUserFoundException e) {
			throw new DomainException(e);
		}
	}

	@Override
	public void canDelete(String domainKey, Authentication auth) throws DomainException {
		hasPermission(domainKey, auth);
	}

	@Override
	public void canPublish(String domainKey, Authentication auth) throws DomainException {
		try {
			String username = auth.getName();
			if (!userService.isAdmin(username)) {
				Account account = accountService.findByTheAccountsUsername(auth.getName());
				if (account != null) {
					String email = account.getEmail();
					if (!userService.isAdminByEmail(email)) {
						throw new DomainException("You do not have the permission to perform this operation");
					}

				} else {
					throw new DomainException("This operation is not supported by this tool");
				}
			}
		} catch (NoUserFoundException e) {
			throw new DomainException(e);
		}
	}

	@Override
	public List<Domain> findShortAllByAuthorname(String authorname) {
		// TODO Auto-generated method stub
		return domainRepo.findShortAllByAuthorname(authorname);
	}

	@Override
	public List<Domain> findShortAllWithAuthornameOrParticipantEmail(String authorname, String participantEmail) {
		// TODO Auto-generated method stub
		List<Domain> all = domainRepo.findAll();
		List<Domain> results = null;
		if (all != null && !all.isEmpty()) {
			results = new ArrayList<Domain>();
			for (Domain dom : all) {
				if (authorname.equals(dom.getAuthorUsername()) || (dom.getParticipantEmails() != null
						&& dom.getParticipantEmails().contains(participantEmail))) {
					results.add(dom);
				}
			}
		}
		return results;
	}

	@Override
	public List<Domain> findShortAll() {
		return domainRepo.findAll();
	}

}
