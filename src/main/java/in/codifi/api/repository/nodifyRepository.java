package in.codifi.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import in.codifi.api.entity.notifyEntity;

public interface nodifyRepository extends JpaRepository<notifyEntity, Long> {

	notifyEntity findByMobileNo(long mobileNo);
}
