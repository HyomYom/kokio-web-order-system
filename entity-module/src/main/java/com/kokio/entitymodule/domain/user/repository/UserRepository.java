package com.kokio.entitymodule.domain.user.repository;

import com.kokio.entitymodule.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByEmail(String email);

  Optional<User> findByIdAndEmail(Long id, String email);

  Optional<User> findByEmailAndVerifyIsTrue(String email);

  boolean existsByEmail(String email);
}
