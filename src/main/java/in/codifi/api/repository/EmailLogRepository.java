package in.codifi.api.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import in.codifi.api.entity.EmailLogEntity;

public interface EmailLogRepository extends CrudRepository<EmailLogEntity, Long> {

	List<EmailLogEntity> findByIdBetween(long startId, long endId);

	List<EmailLogEntity> findByCreatedOnBetween(Date fromDate, Date toDate);
}
