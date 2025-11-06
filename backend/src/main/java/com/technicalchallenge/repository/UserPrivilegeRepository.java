package com.technicalchallenge.repository;

import com.technicalchallenge.model.UserPrivilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.technicalchallenge.model.UserPrivilegeID;


@Repository
public interface UserPrivilegeRepository extends JpaRepository<UserPrivilege, UserPrivilegeID> {}
