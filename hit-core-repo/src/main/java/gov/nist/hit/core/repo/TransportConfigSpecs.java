package gov.nist.hit.core.repo;

import gov.nist.hit.core.domain.KeyValuePair;
import gov.nist.hit.core.domain.TestStepTestingType;
import gov.nist.hit.core.domain.TransportConfig;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

public class TransportConfigSpecs {


  // public boolean matches(List<KeyValuePair> pairs, TestStepTestingType type) {
  // if (!pairs.isEmpty()) {
  // for (KeyValuePair pair : pairs) {
  // if (!matches(pair, type)) {
  // return false;
  // }
  // }
  // return true;
  // }
  // return false;
  // }

  public static Specification<TransportConfig> matches(List<KeyValuePair> pairs,
      TestStepTestingType type) {
    return new Specification<TransportConfig>() {
      @Override
      public Predicate toPredicate(Root<TransportConfig> root, CriteriaQuery<?> query,
          CriteriaBuilder cb) {

        return cb.and(root.get(TransportConfig_.)., y);

        // TODO Auto-generated method stub
        return null;
      }
    };
  }

  public static Specification<Customer> hasSalesOfMoreThan(MontaryAmount value) {
    return new Specification<Customer>() {
      public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

        // build query here
      }
    };
  }


}
