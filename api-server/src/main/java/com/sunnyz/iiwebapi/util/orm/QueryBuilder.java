package com.sunnyz.iiwebapi.util.orm;

import java.util.ArrayList;
import java.util.List;

public class QueryBuilder {

    private List<Query> queries = new ArrayList<>();

    public QueryBuilder setEqual(String field, Object value) {
        this.queries.add(new Query(field, Operator.equal, value));
        return this;
    }

    public QueryBuilder setLike(String field, Object value) {
        this.queries.add(new Query(field, Operator.like, value));
        return this;
    }

    public QueryBuilder setGreatThan(String field, Object value) {
        this.queries.add(new Query(field, Operator.gt, value));
        return this;
    }

    public QueryBuilder setGreatThanEqual(String field, Object value) {
        this.queries.add(new Query(field, Operator.gte, value));
        return this;
    }

    public QueryBuilder setLessThan(String field, Object value) {
        this.queries.add(new Query(field, Operator.lt, value));
        return this;
    }

    public QueryBuilder setLessThanEqual(String field, Object value) {
        this.queries.add(new Query(field, Operator.lte, value));
        return this;
    }

    public List<Query> getQueries() {
        if (this.queries == null) {
            this.queries = new ArrayList<>();
        }
        return queries;
    }
}
