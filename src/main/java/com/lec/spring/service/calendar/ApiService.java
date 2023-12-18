package com.lec.spring.service.calendar;

import com.lec.spring.domain.calendar.Api;

import java.util.List;

public interface ApiService {

    List<Api> list();

    int save(Api api);

}
