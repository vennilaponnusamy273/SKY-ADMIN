package in.codifi.api.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import in.codifi.api.entity.IvrEntity;

public interface IvrRepository extends CrudRepository<IvrEntity, Long> {

	IvrEntity findByApplicationId(Long applicationId);
	
	IvrEntity findByApplicationIdAndDocumentType(long applicationId, String documentType);

	void deleteByApplicationId(long applicationId);
	
	@Transactional
	@Query(value = " SELECT attachementUrl FROM tbl_ivr_details where documentType = :documentType and applicationId = :applicationId ")
	String getAttachmentUrl(@Param("documentType") String documentType, @Param("applicationId") long applicationId);

}
