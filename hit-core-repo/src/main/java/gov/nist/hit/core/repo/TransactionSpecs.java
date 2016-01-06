/**
 * This software was developed at the National Institute of Standards and Technology by employees of
 * the Federal Government in the course of their official duties. Pursuant to title 17 Section 105
 * of the United States Code this software is not subject to copyright protection and is in the
 * public domain. This is an experimental system. NIST assumes no responsibility whatsoever for its
 * use by other parties, and makes no guarantees, expressed or implied, about its quality,
 * reliability, or any other characteristic. We would appreciate acknowledgement if the software is
 * used. This software can be redistributed and/or modified freely provided that any derivative
 * works bear some notice that they are derived from it, and any modified versions bear some notice
 * that they have been modified.
 */
package gov.nist.hit.core.repo;

import gov.nist.hit.core.domain.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

/**
 * @author Harold Affo
 * 
 */
public class TransactionSpecs {

  // public static Specification<Account> hasAccountType(final String accountType) {
  // return new Specification<Account>() {
  // public Predicate toPredicate(Root<Account> root, CriteriaQuery<?> query,
  // CriteriaBuilder builder) {
  // return builder.equal(root.get("accountType"), accountType);
  // }
  // };
  // }
  //
  // public static Specification<Account> companyIsLike(final String searchTerm) {
  // return new Specification<Account>() {
  // public Predicate toPredicate(Root<Account> root, CriteriaQuery<?> query,
  // CriteriaBuilder builder) {
  // return builder.like(builder.lower(root.<String>get("company")), getLikePattern(searchTerm));
  // }
  //
  // private String getLikePattern(final String searchTerm) {
  // StringBuilder pattern = new StringBuilder();
  // pattern.append(searchTerm.toLowerCase());
  // pattern.append("%");
  // return pattern.toString();
  // }
  //
  //
  //
  // };
  //
  // }

  public static Specification<Transaction> matches(final Map<String, String> criteria) {
    return new Specification<Transaction>() {
      @Override
      public Predicate toPredicate(Root<Transaction> root, CriteriaQuery<?> query,
          CriteriaBuilder builder) {
        List<Predicate> predicates = new ArrayList<Predicate>();
        Path<Map<String, String>> propertiesRoot = root.join("properties");
        for (String key : criteria.keySet()) {
          Predicate p = builder.and(propertiesRoot.in(key), propertiesRoot.in(criteria.get(key)));
          predicates.add(p);
        }
        return builder.isTrue(propertiesRoot.in(criteria));
      }
    };
  }
}
