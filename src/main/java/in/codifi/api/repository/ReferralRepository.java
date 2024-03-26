package in.codifi.api.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.codifi.api.entity.ReferralEntity;

public interface ReferralRepository extends JpaRepository<ReferralEntity, Long> {

	ReferralEntity findByMobileNo(long mobileNo);

	ReferralEntity findByIdAndReferralBy(Long id, String referralBy);

	List<ReferralEntity> findByReferralBy(String referralBy);
	
	  @Query("SELECT  ce  FROM tbl_referral_details ce WHERE ce.createdOn BETWEEN :fromDateTime AND :toDateTime")
	    List<ReferralEntity> findByDate(
	        @Param("fromDateTime") Date fromDateTime,
	        @Param("toDateTime") Date toDateTime
	    );
	
	@Query("SELECT COUNT(ce) FROM tbl_referral_details ce WHERE ce.createdOn BETWEEN :fromDateTime AND :toDateTime AND ce.refByName = :refByName")
    long getCountForReferralAndDateRange(
        @Param("refByName") String refByName,
        @Param("fromDateTime") Date fromDateTime,
        @Param("toDateTime") Date toDateTime
    );
	   
}
