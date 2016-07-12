package gov.nist.hit.core.service;

import gov.nist.auth.hit.core.domain.AccountPasswordReset;

public interface AccountPasswordResetService {

  AccountPasswordReset findByTheAccountsUsername(String username);

  AccountPasswordReset findOne(Long id);

  AccountPasswordReset save(AccountPasswordReset acc);


}
