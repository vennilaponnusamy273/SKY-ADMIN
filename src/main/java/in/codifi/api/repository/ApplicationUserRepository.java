package in.codifi.api.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import in.codifi.api.entity.ApplicationUserEntity;

public interface ApplicationUserRepository extends CrudRepository<ApplicationUserEntity, Long> {

	List<ApplicationUserEntity> findByIdBetween(long startId, long endId);
}
