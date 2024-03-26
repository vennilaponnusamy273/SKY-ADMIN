package in.codifi.api.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.codifi.api.entity.ApplicationUserEntity;

public interface ApplicationUserRepository extends JpaRepository<ApplicationUserEntity, Long> {

	List<ApplicationUserEntity> findByIdBetween(long startId, long endId);

	ApplicationUserEntity findByMobileNo(Long mobileNumber);
	
	 @Query("SELECT ce FROM tbl_application_master ce WHERE ce.createdOn BETWEEN :fromDateTime AND :toDateTime")
	 List<ApplicationUserEntity> findByDate(
	         @Param("fromDateTime") Date fromDateTime,
	         @Param("toDateTime") Date toDateTime
	 );
}
