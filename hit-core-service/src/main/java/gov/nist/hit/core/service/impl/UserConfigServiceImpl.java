package gov.nist.hit.core.service.impl;

import gov.nist.hit.core.domain.UserConfig;
import gov.nist.hit.core.repo.UserConfigRepository;
import gov.nist.hit.core.service.UserConfigService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@Service
public class UserConfigServiceImpl implements UserConfigService {

    static final Logger logger = LoggerFactory.getLogger(UserConfigServiceImpl.class);


    @Autowired
    protected UserConfigRepository userConfigRepository;

    @Autowired
    @PersistenceContext(unitName = "base-tool")
     protected EntityManager entityManager;


    @Override
    public Long findUserIdByProperties(Map<String, String> criteria) {
        String sql = findUserIdQuery(criteria);
        logger.info("Find one user with criterias : "+sql);
        List<UserConfig> userConfigList = entityManager.createNativeQuery(sql, UserConfig.class).getResultList();
        logger.info("Found userConfig : "+userConfigList);
        if(userConfigList.size()>0) {
            return userConfigList.get(0).getUserId();
        }
        return null;
    }

    @Override
    public void delete(UserConfig userConfig) {
        userConfigRepository.delete(userConfig);
    }

    @Override
    public void delete(Long id) {
        userConfigRepository.delete(id);
    }

    @Override
    public UserConfig save(UserConfig userConfig) {
        return userConfigRepository.saveAndFlush(userConfig);
    }

    @Override
    public void delete(List<UserConfig> userConfigList) {
        userConfigRepository.delete(userConfigList);
    }

    @Override
    public UserConfig findOneByUserId(Long userId){
        return userConfigRepository.getUserConfigByUserId(userId);
    }

    @Override
    public UserConfig findOne(Long userConfigId) {
        return userConfigRepository.findOne(userConfigId);
    }

    private String findUserIdQuery(Map<String, String> criteria) {
        String sql = "SELECT * FROM USERCONFIG uc";
        ArrayList<String> conditions = new ArrayList<>();
        Iterator<Entry<String, String>> it = criteria.entrySet().iterator();
        int i = 1;
        while (it.hasNext()) {
            Entry<String, String> pair = it.next();
            String key = pair.getKey();
            String value = pair.getValue();
            String alias = "ucp" + i;
            sql +=
                    " LEFT OUTER JOIN USER_CONFIG_PROPERTIES " + alias + " ON uc.ID = " + alias
                            + ".USER_CONFIG_PROPERTIES_ID AND " + alias + ".property_key = '" + key + "' AND " + alias
                            + ".property_value = '" + value + "'";
            conditions.add(alias + ".property_key is not null");
            i++;
        }
        if(conditions.size()>1) {
            sql += " WHERE ";
            for (int j = 0; j < conditions.size(); j++) {
                if (j > 0) {
                    sql += " AND ";
                }
                sql += conditions.get(j);
            }
        }
        return sql;
    }
}
