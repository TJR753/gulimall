package com.example.gulimall.search.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.example.common.to.es.SkuESModel;
import com.example.gulimall.search.config.ESConfig;
import com.example.gulimall.search.constant.ESIndexConstant;
import com.example.gulimall.search.service.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestHighLevelClient;

import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SearchServiceImpl implements SearchService {
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Override
    public Boolean save(List<SkuESModel> skuESModelList) throws IOException {
        List<IndexRequest> indexRequestList = skuESModelList.stream().map(skuESModel -> {
            IndexRequest indexRequest = new IndexRequest(ESIndexConstant.PRODUCT_INDEX);
            String json = JSONObject.toJSONString(skuESModel);
            indexRequest.id(skuESModel.getSkuId() + "").source(json, XContentType.JSON);
            return indexRequest;
        }).collect(Collectors.toList());

        BulkRequest bulkRequest = new BulkRequest();
        for(IndexRequest indexRequest:indexRequestList){
            bulkRequest.add(indexRequest);
        }
        BulkResponse bulkResponse = restHighLevelClient.bulk(bulkRequest, ESConfig.COMMON_OPTIONS);
        boolean hasFailures = bulkResponse.hasFailures();
        if(hasFailures){
            BulkItemResponse[] items = bulkResponse.getItems();
            Arrays.stream(items).forEach(bulkItemResponse -> {
                String s=bulkItemResponse.getItemId()+" "+bulkItemResponse.getFailureMessage();
                log.error(s);
            });
        }
        return true;
    }
}
