package net.graysenko.com.ForumDQC.Repositories;

import net.graysenko.com.ForumDQC.Models.UserINF;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserINFRepository extends JpaRepository<UserINF, Long> {
    Optional<UserINF> findByUsername(String username);

    UserINF findById(long id);
}
