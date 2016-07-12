package gov.nist.hit.core.service.impl;

import gov.nist.auth.hit.core.domain.AccountPasswordReset;
import gov.nist.auth.hit.core.repo.AccountPasswordResetRepository;
import gov.nist.hit.core.service.AccountPasswordResetService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(value = "authTransactionManager")
public class AccountPasswordResetServiceImpl implements AccountPasswordResetService {

  @Autowired
  protected AccountPasswordResetRepository accountPasswordResetRepository;

  @Override
  public AccountPasswordReset findByTheAccountsUsername(String username) {
    return accountPasswordResetRepository.findByTheAccountsUsername(username);
  }

  @Override
  public AccountPasswordReset findOne(Long id) {
    return accountPasswordResetRepository.findOne(id);
  }

  @Override
  @Transactional(value = "authTransactionManager")
  public AccountPasswordReset save(AccountPasswordReset acc) {
    // TODO Auto-generated method stub
    return accountPasswordResetRepository.save(acc);
  }



}
