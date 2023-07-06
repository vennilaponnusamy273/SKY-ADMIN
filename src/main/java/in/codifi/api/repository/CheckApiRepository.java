package in.codifi.api.repository;

import org.springframework.data.repository.CrudRepository;

import in.codifi.api.entity.CheckApiEntity;

public interface CheckApiRepository extends CrudRepository<CheckApiEntity, Long> {

	CheckApiEntity findByapplicationId(Long applicationId);

}
