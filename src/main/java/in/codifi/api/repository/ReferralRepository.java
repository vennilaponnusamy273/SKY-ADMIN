package in.codifi.api.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import in.codifi.api.entity.ReferralEntity;

public interface ReferralRepository extends CrudRepository<ReferralEntity, Long> {

	ReferralEntity findByMobileNo(long mobileNo);

	ReferralEntity findByIdAndReferralBy(Long id, String referralBy);

	List<ReferralEntity> findByReferralBy(String referralBy);
}
