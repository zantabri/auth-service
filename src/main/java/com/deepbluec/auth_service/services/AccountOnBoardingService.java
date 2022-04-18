package com.deepbluec.auth_service.services;

import com.deepbluec.auth_service.model.AccountDetails;

/**
 * services account onboarding requests.
 */
public interface AccountOnBoardingService {

    /**
     *
     * @param accountDetails
     * @return
     */
    AccountDetails register(AccountDetails accountDetails);

    /**
     *
     * @param username
     * @param activationToken
     * @return
     */
    boolean activate(String username, String activationToken);

}
