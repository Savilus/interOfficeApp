package io.mkolodziejczyk92.data.service;

import io.mkolodziejczyk92.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UsersRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {


}
