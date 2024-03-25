package in.codifi.api.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import in.codifi.api.entity.BackOfficeApiEntity;

public interface BackOfficeApiRepository extends CrudRepository<BackOfficeApiEntity, Long> {

	BackOfficeApiEntity findByapplicationId(Long applicationId);
	
	 @Query("SELECT ce FROM tbl_backoffice_entity ce WHERE ce.createdOn BETWEEN :fromDateTime AND :toDateTime")
	 List<BackOfficeApiEntity> findByDate(
	         @Param("fromDateTime") Date fromDateTime,
	         @Param("toDateTime") Date toDateTime
	 );
}
