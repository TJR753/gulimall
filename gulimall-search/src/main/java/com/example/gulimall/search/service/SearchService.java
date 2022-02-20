package com.example.gulimall.search.service;

import com.example.common.to.es.SkuESModel;

import java.io.IOException;
import java.util.List;

public interface SearchService {

    Boolean save(List<SkuESModel> skuESModelList) throws IOException;
}
