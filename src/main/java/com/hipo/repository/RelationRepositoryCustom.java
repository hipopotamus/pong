package com.hipo.repository;

import com.hipo.domain.entity.Relation;
import com.querydsl.core.QueryResults;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RelationRepositoryCustom {
    public QueryResults<Relation> findRequestsByPage(Long accountId, Pageable pageable);

    public List<Relation> findAllRequestFromOther(Long accountId);

    public QueryResults<Relation> findRequestsFromOtherByPage(Long accountId, Pageable pageable);

    public QueryResults<Relation> findFriendsByPage(Long accountId, Pageable pageable);

    public QueryResults<Relation> findBlocksByPage(Long accountId, Pageable pageable);
}
