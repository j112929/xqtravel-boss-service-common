package com.xqcx.boss.util;

import com.xqcx.basic.dto.SysConfig;
import com.xqcx.boss.site.dto.CityResult;
import com.xqcx.common.model.BossUserSession;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HYY on 2017/7/21.
 */
public class UserCityUtil {
    public static List<CityResult> getUserCity(BossUserSession session, List<SysConfig> configs) {
        if (configs != null && configs.size() > 0) {
            for (SysConfig config : configs) {
                if (Long.parseLong(config.getKey()) == session.getCityId()) {
                    List<CityResult> cities = new ArrayList<>(1);
                    CityResult city = new CityResult();
                    city.setId(Long.parseLong(config.getKey()));
                    city.setCityName(config.getValue());
                    city.setCityCode(config.getName());
                    city.setGdCode(config.getDesc());
                    cities.add(city);
                    return cities;
                }
            }
        }
        return null;
    }
}
