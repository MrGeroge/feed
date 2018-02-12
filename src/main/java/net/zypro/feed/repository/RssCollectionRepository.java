package net.zypro.feed.repository;

import java.util.List;

import net.zypro.feed.domain.RssCollection;
import net.zypro.feed.domain.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RssCollectionRepository extends
		JpaRepository<RssCollection, Integer> {
	List<RssCollection> findByUser(User user);
}
