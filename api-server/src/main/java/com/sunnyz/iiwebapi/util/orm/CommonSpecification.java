package com.sunnyz.iiwebapi.util.orm;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.sql.Timestamp;
import java.util.List;
import java.util.regex.Pattern;


public class CommonSpecification<T> implements Specification<T> {

    public static final Pattern DATE_PATTERN = Pattern.compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))$");


    private List<Query> querys;

    /**
     * 默认构造器
     */
    public CommonSpecification() {
    }

    /**
     * 设置查询条件构造器
     *
     * @param queries 查询条件集合
     */
    public CommonSpecification(List<Query> queries) {
        this.querys = queries;
    }

    /**
     * Creates a WHERE clause for a query of the referenced entity in form of a {@link Predicate} for the given
     * {@link Root} and {@link CriteriaQuery}.
     *
     * @param root
     * @param query
     * @param cb
     * @return a {@link Predicate}, must not be {@literal null}.
     */
    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        return this.buildPredicate(this.querys, root, cb);
    }

    /**
     * 构造es查询
     *
     * @param querys 请求中的查询集合
     * @return es查询表达式
     */
    private Predicate buildPredicate(List<Query> querys, Root<T> root, CriteriaBuilder cb) {
        Predicate predicate = cb.conjunction();
        if (querys != null && querys.size() > 0) {
            for (Query q : querys) {
                Predicate p = this.getQueryPredicate(q, root, cb);
                if (p != null) {
                    predicate.getExpressions().add(p);
                }
            }
        }
        return predicate;
    }

    /**
     * 格式化查询值
     *
     * @param query 查询对象
     * @return
     */
    private Predicate getQueryPredicate(Query query, Root<T> root, CriteriaBuilder cb) {
        if (query == null) {
            return null;
        }
        if (query.getField() != null && query.getValue() != null && (query.getField().toLowerCase().endsWith("date") || query.getField().toLowerCase().endsWith("time"))) {
            if (query.getValue() instanceof String && DATE_PATTERN.matcher(query.getValue().toString()).matches()) {
                switch (query.getOperator()) {
                    case equal:
                        return cb.equal(root.<Timestamp>get(query.getField()), Timestamp.valueOf(((String) query.getValue()).toString().replace('/', '-') + " 00:00:00"));
                    case lt:
                        return cb.lessThan(root.<Timestamp>get(query.getField()), Timestamp.valueOf(query.getValue().toString().replace('/', '-') + " 00:00:00"));
                    case lte:
                        return cb.lessThanOrEqualTo(root.<Timestamp>get(query.getField()), Timestamp.valueOf(query.getValue().toString().replace('/', '-') + " 23:59:59"));
                    case gt:
                        return cb.greaterThan(root.<Timestamp>get(query.getField()), Timestamp.valueOf(query.getValue().toString().replace('/', '-') + " 00:00:00"));
                    case gte:
                        return cb.greaterThanOrEqualTo(root.<Timestamp>get(query.getField()), Timestamp.valueOf(query.getValue().toString().replace('/', '-') + " 23:59:59"));
                    default:
                        break;
                }
            }
        }

        switch (query.getOperator()) {
            case equal:
                if (query.getValue() != null && StringUtils.isNotBlank(query.getValue().toString())) {
                    return cb.equal(root.<String>get(query.getField()), query.getValue());
                }
                break;
            case like:
                if (query.getValue() != null && StringUtils.isNotBlank(query.getValue().toString())) {
                    return cb.like(root.<String>get(query.getField()), "%" + query.getValue().toString() + "%");
                }
                break;
            case lt:
                if (query.getValue() != null && StringUtils.isNotBlank(query.getValue().toString())) {
                    return cb.lessThan(root.<String>get(query.getField()), query.getValue().toString());
                }
                break;
            case lte:
                if (query.getValue() != null && StringUtils.isNotBlank(query.getValue().toString())) {
                    return cb.lessThanOrEqualTo(root.<String>get(query.getField()), query.getValue().toString());
                }
                break;
            case gt:
                if (query.getValue() != null && StringUtils.isNotBlank(query.getValue().toString())) {
                    return cb.greaterThan(root.<String>get(query.getField()), query.getValue().toString());
                }
                break;
            case gte:
                if (query.getValue() != null && StringUtils.isNotBlank(query.getValue().toString())) {
                    return cb.greaterThanOrEqualTo(root.<String>get(query.getField()), query.getValue().toString());
                }
                break;
            default:
                break;
        }
        return null;
    }


    public List<Query> getQuerys() {
        return querys;
    }

    public void setQuerys(List<Query> querys) {
        this.querys = querys;
    }
}