package gov.nist.hit.core.service.impl;

import gov.nist.hit.core.domain.UserConfig;
import gov.nist.hit.core.repo.UserConfigRepository;
import gov.nist.hit.core.service.UserConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@Service
public class UserConfigServiceImpl implements UserConfigService {

    @Autowired
    protected UserConfigRepository userConfigRepository;

    @Autowired
    protected EntityManager entityManager;


    @Override
    public Long findUserIdByProperties(Map<String, String> criteria) {
        String sql = findOneQuery(criteria);
        Query q = entityManager.createNativeQuery(sql, UserConfig.class);
        UserConfig res = getUserConfigResult(q);
        return res.getId();
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
    public List<UserConfig> findAllByProperties(Map<String, String> criteria) {
        String sql = findOneQuery(criteria);
        Query q = entityManager.createNativeQuery(sql, UserConfig.class);
        return q.getResultList();
    }

    @Override
    public void delete(List<UserConfig> userConfigList) {
        userConfigRepository.delete(userConfigList);
    }

    private String findOneQuery(Map<String, String> criteria) {
        String sql = "SELECT ID FROM USERCONFIG uc";
        ArrayList<String> conditions = new ArrayList<>();
        Iterator<Entry<String, String>> it = criteria.entrySet().iterator();
        int i = 1;
        while (it.hasNext()) {
            Entry<String, String> pair = it.next();
            String key = pair.getKey();
            String value = pair.getValue();
            String alias = "ucp" + i;
            sql +=
                    " LEFT OUTER JOIN USER_CONFIG_PROPERTIES " + alias + " ON uc.id = " + alias
                            + ".USER_CONFIG_PROPERTIES_ID AND " + alias + ".property_key = '" + key + "' AND " + alias
                            + ".property_value = '" + value + "'";
            conditions.add(alias + ".property_key is not null");
            i++;
        }
        sql += " WHERE ";
        for (int j = 0; j < conditions.size(); j++) {
            if (j > 0) {
                sql += " AND ";
            }
            sql += conditions.get(j);
        }

        return sql;
    }

    private UserConfig getUserConfigResult(Query query) {
        query.setMaxResults(1);
        List<UserConfig> list = query.getResultList();
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

}
