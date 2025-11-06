package com.technicalchallenge.repository;

import com.technicalchallenge.model.UserPrivilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.technicalchallenge.model.UserPrivilegeId;

import java.util.Optional;


@Repository
public interface UserPrivilegeRepository extends JpaRepository<UserPrivilege, UserPrivilegeId> {

Optional<UserPrivilege> findByUserIdAndPrivilegeId(Long userId, Long privilegeId);

void deleteByUserIdAndPrivilegeId(Long userId, Long privilegeId);

}
