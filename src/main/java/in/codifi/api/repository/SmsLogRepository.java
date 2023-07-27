package in.codifi.api.repository;

import java.util.Date;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import in.codifi.api.entity.SmsLogEntity;

public interface SmsLogRepository extends CrudRepository<SmsLogEntity, Long> {

	SmsLogEntity findByMobileNoAndLogMethod(Long mobileNo, String logMethod);

	List<SmsLogEntity> findByIdBetween(long startId, long endId);
	
	List<SmsLogEntity> findByCreatedOnBetween(Date fromDate, Date toDate);
}
