package com.sunnyz.iiwebapi.base;

import com.sunnyz.iiwebapi.util.orm.SortType;
import org.springframework.data.domain.Sort;

import java.util.Map;
import java.util.Set;

public class BaseService {
    /**
     * 获取springdata排序对象 默认以创建日期倒序排列
     *
     * @param sorts 自定义排序map
     * @return springdata 排序对象
     */
    public Sort getSort(Map<String, SortType> sorts) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdDate");
        if (sorts != null && sorts.size() > 0) {
            Set<String> keys = sorts.keySet();
            sort = null;
            for (String key : keys) {
                if (sort == null) {
                    sort = Sort.by(sorts.get(key).toString().toLowerCase().equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, key);
                } else {
                    sort.and(Sort.by(sorts.get(key).toString().toLowerCase().equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, key));
                }
            }
        }
        return sort;
    }
}
