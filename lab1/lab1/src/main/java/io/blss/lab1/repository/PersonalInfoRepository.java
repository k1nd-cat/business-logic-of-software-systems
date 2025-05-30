package io.blss.lab1.repository;

import io.blss.lab1.entity.PersonalInfo;
import io.blss.lab1.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonalInfoRepository extends JpaRepository<PersonalInfo, Long> {
    Optional<PersonalInfo> findByUser(User user);
}
