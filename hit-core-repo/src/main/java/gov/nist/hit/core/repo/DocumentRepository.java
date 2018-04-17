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

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import gov.nist.hit.core.domain.Document;
import gov.nist.hit.core.domain.DocumentType;
import gov.nist.hit.core.domain.TestScope;

public interface DocumentRepository extends JpaRepository<Document, Long> {

  // @Query("select doc from Document doc where doc.type = 'RELEASENOTE' order by doc.version desc")
  // public List<Document> findAllReleaseNotes();
  //
  // @Query("select doc from Document doc where doc.type = 'KNOWNISSUE' order by doc.version desc")
  // public List<Document> findAllKnownIssues();
  //
  // @Query("select doc from Document doc where doc.type = 'USERDOC' and doc.domain = :domain and
  // doc.scope = :scope order by doc.position asc")
  // public List<Document> findAllUserDocsByDomainAndScope(@Param("domain") String domain,
  // @Param("scope") TestScope scope);

  //
  // @Query("select doc from Document doc where doc.type = :type and doc.domain = :domain and
  // doc.scope = :scope order by doc.position asc")
  // public List<Document> findAllResourceDocsByTypeAndDomainAndScope(@Param("type") DocumentType
  // type,
  // @Param("domain") String domain, @Param("scope") TestScope scope);

  // @Query("select doc from Document doc where doc.type = :type and doc.domain = :domain and
  // doc.authorUsername = :authorUsername and doc.scope =:scope order by doc.position asc")
  // public List<Document> findAllResourceDocsByTypeAndDomainAndAuthorAndScope(
  // @Param("type") DocumentType type, @Param("domain") String domain,
  // @Param("authorUsername") String authorUsername, @Param("scope") TestScope scope);


  @Query("select doc from Document doc where doc.name = :name")
  public Document findOneByName(@Param("name") String name);

  // @Query("select doc from Document doc where doc.type = 'DELIVERABLE' order by doc.date desc")
  // public List<Document> findAllDeliverableDocs();
  //
  // @Query("select doc from Document doc where doc.type = 'INSTALLATION'")
  // public Document findInstallationDoc();


  @Query("select doc from Document doc where doc.domain = :domain and doc.type = :type and doc.scope = :scope order by doc.position asc")
  public List<Document> findAllByDomainAndScopeAndType(@Param("domain") String domain,
      @Param("scope") TestScope scope, @Param("type") DocumentType type);


  @Query("select doc from Document doc where  doc.domain = :domain and doc.authorUsername = :authorUsername and doc.scope =:scope and doc.type = :type order by doc.position asc")
  public List<Document> findAllByDomainAndAuthorAndScopeAndType(@Param("domain") String domain,
      @Param("authorUsername") String authorUsername, @Param("scope") TestScope scope,
      @Param("type") DocumentType type);



}
