package gov.nist.healthcare.tools.core.services;

import gov.nist.healthcare.tools.core.models.VocabularyCollection;
import gov.nist.healthcare.tools.core.models.VocabularySearchResult;
import gov.nist.healthcare.tools.core.models.VocabularySearchResultItem;
import gov.nist.healthcare.tools.core.services.exception.VocabularySearchException;

import java.util.Collection;
import java.util.List;

public abstract class VocabularySearchService {

	/**
	 * This searchTableElementByCodeAndDescription method takes searchValue and
	 * SearchCriteria as input and search all the table from the xml and returns
	 * collection of string array having matching value and related table info.
	 * 
	 * @param searchValue
	 *            the search value
	 * @param searchCriteria
	 *            the search criteria
	 * @return the collection
	 * @throws VocabularySearchException
	 *             the illegal argument exception
	 */

	public abstract Collection<VocabularySearchResultItem> searchByTestContextId(
			Long testContextId, String searchValue, String searchCriteria)
			throws VocabularySearchException;

	/**
	 * 
	 * @param vocabularyLibraryId
	 * @return
	 */
	public abstract VocabularySearchResult searchTable(Long vocabCollectionId,
			Long vocabularyLibraryId, String tableNumber, String valueSetName)
			throws VocabularySearchException;

	/**
	 * 
	 * @return
	 */
	public abstract List<VocabularyCollection> getVocabularyCollectionByTestCaseContextId(
			Long testCaseContextId);

	/**
	 * 
	 * @param id
	 * @return
	 */
	public abstract VocabularyCollection getVocabularyCollection(Long id);

}
