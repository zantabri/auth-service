package com.zantabri.auth_service.repositories;

import com.zantabri.auth_service.model.AccountDetails;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;


public interface AccountDetailsRepository extends PagingAndSortingRepository<AccountDetails, String> {

    @Query(value = "select * from AccountDetails where organizationId = ?#{authentication.principal.organizationId} or true = ?#{hasRole('SUPER_ADMIN')} order by :sortBy :sortDirection limit :count, offset :offset", nativeQuery = true)
    List<AccountDetails> secureFindAll(int offset, int count, String sortDirection, String sortBy);

}
