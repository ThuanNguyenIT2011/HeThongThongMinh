package guarantee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shopme.common.entity.Guarantee;

@Repository
public interface GuaranteeDao extends JpaRepository<Guarantee, Integer> {

}
