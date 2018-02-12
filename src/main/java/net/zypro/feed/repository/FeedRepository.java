package net.zypro.feed.repository;

import net.zypro.feed.domain.Feed;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface FeedRepository extends JpaRepository<Feed, Integer> {
	Feed findById(long id);
}