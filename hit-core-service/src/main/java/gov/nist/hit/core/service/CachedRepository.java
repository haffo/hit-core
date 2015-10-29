package gov.nist.hit.core.service;

import gov.nist.hit.core.domain.Constraints;
import gov.nist.hit.core.domain.IntegrationProfile;
import gov.nist.hit.core.domain.VocabularyLibrary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by fdevaulx on 10/21/15.
 */
public class CachedRepository {

    static final public Logger logger = LoggerFactory.getLogger(CachedRepository.class);

    private Map<String, IntegrationProfile> cachedProfiles;
    private Map<String, VocabularyLibrary> cachedVocabLibraries;
    private Map<String, Constraints> cachedConstraints;

    public CachedRepository() {
        logger.info("cachedRepo instantiated");
    }

    public Map<String, IntegrationProfile> getCachedProfiles() {
        cachedProfiles = cachedProfiles != null ? cachedProfiles : new HashMap<String, IntegrationProfile>();
        logger.info("cachedProfiles size: "+cachedProfiles.size());
        return cachedProfiles;
    }
    public Map<String, VocabularyLibrary> getCachedVocabLibraries() {
        cachedVocabLibraries = cachedVocabLibraries != null ? cachedVocabLibraries : new HashMap<String, VocabularyLibrary>();
        logger.info("cachedVocabLibraries size: "+cachedVocabLibraries.size());
        return cachedVocabLibraries;
    }

    public Map<String, Constraints> getCachedConstraints() {
        cachedConstraints = cachedConstraints != null ? cachedConstraints : new HashMap<String, Constraints>();
        logger.info("cachedConstraints size: "+cachedConstraints.size());
        return cachedConstraints;
    }

}
