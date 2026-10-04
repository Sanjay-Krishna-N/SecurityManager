package org.secured.repository;

import org.secured.model.request.RegisterRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * ------------------------------------------------------------------------
 * Author   : Sanjay Krishna Narayanan
 * Created  : 9/28/26
 * Version  : 1.0
 * ------------------------------------------------------------------------
 */
@Repository
public interface UsersRepository extends JpaRepository<RegisterRequest,Long> {
    boolean existsByUsername(String username);
    RegisterRequest findByUsername(String username);
}
