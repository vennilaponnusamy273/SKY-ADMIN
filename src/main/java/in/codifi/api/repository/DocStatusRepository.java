package in.codifi.api.repository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import in.codifi.api.entity.DocStatusEntity;

public interface DocStatusRepository extends JpaRepository<DocStatusEntity, Long> {

	DocStatusEntity findByApplicationIdAndTypeDoc(long applicationId,String docType);
	List<DocStatusEntity> findByApplicationId(Long applicationId);
}
