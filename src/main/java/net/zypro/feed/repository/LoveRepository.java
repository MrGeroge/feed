package net.zypro.feed.repository;

import java.util.List;

import net.zypro.feed.domain.Feed;
import net.zypro.feed.domain.Love;
import net.zypro.feed.domain.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoveRepository extends JpaRepository<Love, Integer> { // µ„‘ﬁ±Ì
	Love findByUserAndFeed(User user, Feed feed);
	List<Love> findByUser(User user);
}
