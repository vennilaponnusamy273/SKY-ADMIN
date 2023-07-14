package in.codifi.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import in.codifi.api.entity.ApplicationUserEntity;

public interface ApplicationUserRepository extends JpaRepository<ApplicationUserEntity, Long> {

	List<ApplicationUserEntity> findByIdBetween(long startId, long endId);
	ApplicationUserEntity findByMobileNo(@Param("mobileNo") Long mobileNumber);
}
