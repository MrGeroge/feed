package net.zypro.feed.repository;

import java.util.List;

import net.zypro.feed.domain.Tag;
import net.zypro.feed.domain.TagCollection;
import net.zypro.feed.domain.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagCollectionRepository extends JpaRepository<TagCollection,Integer> {
TagCollection findByUserAndTag(User user,Tag tag);
List<TagCollection> findByUser(User user);
}
