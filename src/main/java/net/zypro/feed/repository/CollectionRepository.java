package net.zypro.feed.repository;

import java.util.List;

import net.zypro.feed.domain.Collection;
import net.zypro.feed.domain.Feed;
import net.zypro.feed.domain.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CollectionRepository extends
		JpaRepository<Collection, Integer> {
	Collection findById(int id);

	Collection findByUserAndFeed(User user, Feed feed);

	List<Collection> findByUser(User user);
}
