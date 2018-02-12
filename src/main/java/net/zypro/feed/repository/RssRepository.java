package net.zypro.feed.repository;

import java.util.List;

import net.zypro.feed.domain.Rss;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RssRepository extends JpaRepository<Rss, Integer> {
	Rss findById(int id);

	List<Rss> findByNameLike(String name);
}
