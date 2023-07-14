package in.codifi.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import in.codifi.api.entity.ReferralEntity;

public interface ReferralRepository extends JpaRepository<ReferralEntity, Long> {

	ReferralEntity findByMobileNo(long mobileNo);
}
