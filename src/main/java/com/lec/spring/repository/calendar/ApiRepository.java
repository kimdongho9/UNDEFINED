package com.lec.spring.repository.calendar;

import com.lec.spring.domain.calendar.Api;

import java.util.List;

public interface ApiRepository {

    List<Api> findAll();

    int save(Api api);

}
