package in.codifi.api.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.codifi.api.entity.DocumentEntity;

public interface DocumentEntityRepository extends JpaRepository<DocumentEntity, Long> {

	DocumentEntity findByApplicationIdAndDocumentType(long applicationId, String documentType);
	
	 @Query("SELECT ce FROM tbl_document_details ce WHERE ce.createdOn BETWEEN :fromDateTime AND :toDateTime AND documentType='ESIGN_DOCUMENT'")
	 List<DocumentEntity> findByDate(
	         @Param("fromDateTime") Date fromDateTime,
	         @Param("toDateTime") Date toDateTime
	 );

}
