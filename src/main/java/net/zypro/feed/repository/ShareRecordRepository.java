package net.zypro.feed.repository;

import net.zypro.feed.domain.ShareRecord;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface ShareRecordRepository extends JpaRepository<ShareRecord, Integer> {

}
