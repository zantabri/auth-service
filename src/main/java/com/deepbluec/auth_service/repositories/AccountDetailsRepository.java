package com.deepbluec.auth_service.repositories;

import com.deepbluec.auth_service.model.AccountDetails;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface AccountDetailsRepository extends PagingAndSortingRepository<AccountDetails, String> {

}
