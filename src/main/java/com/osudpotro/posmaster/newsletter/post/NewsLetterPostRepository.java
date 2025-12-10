package com.osudpotro.posmaster.newsletter.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsLetterPostRepository extends JpaRepository<NewsLetterPost, Long> {
}
