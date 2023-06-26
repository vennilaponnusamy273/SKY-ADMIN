package in.codifi.api.repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import in.codifi.api.entity.TxnDetailsEntity;

public interface TxnDetailsRepository extends CrudRepository<TxnDetailsEntity, Long> {

	TxnDetailsEntity findByapplicationId(Long applicationId);
	TxnDetailsEntity findBytxnId(String txnId);
	
	
	@Query(value = "SELECT txd.folderLocation FROM tbl_txn_details txd WHERE applicationId = :applicationId AND updatedOn = (SELECT MAX(updatedOn) FROM tbl_txn_details WHERE applicationId = :applicationId)")
	String findsinglefilefolder(@Param("applicationId") long applicationId);

	

}
