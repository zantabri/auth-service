package com.zantabri.auth_service.repositories;

import com.zantabri.auth_service.model.AccountDetails;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface AccountDetailsRepository extends PagingAndSortingRepository<AccountDetails, String> {

}
