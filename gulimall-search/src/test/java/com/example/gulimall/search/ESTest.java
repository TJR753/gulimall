package com.example.gulimall.search;

import com.example.gulimall.search.config.ESConfig;
import org.apache.lucene.index.Term;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.TermVectorsResponse;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.Avg;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ESTest {
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Test
    public void esTest(){
        System.out.println(restHighLevelClient);
    }

    @Test
    public void indexTest() throws IOException {
        IndexRequest indexRequest = new IndexRequest("users");
        indexRequest.id("1");
        indexRequest.source("username","mike","age",18,"gender","m");
        IndexResponse indexResponse = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
        System.out.println(indexResponse.getResult());
    }
    @Test
    public void getTest() throws IOException {
        GetRequest getRequest = new GetRequest("users",1+"");
        GetResponse getResponse = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
        Map<String, Object> map = getResponse.getSourceAsMap();
        map.forEach((k,v)->{
            System.out.println(k+":"+v);
        });
//        System.out.println((String)getResponse.getField("username").getValue());
    }

    @Test
    public void updateTest() throws IOException {
        UpdateRequest updateRequest = new UpdateRequest("users", "1").doc("username", "zhangsna", "age", "78");
        restHighLevelClient.update(updateRequest,RequestOptions.DEFAULT);
    }

    @Test
    public void searchTest() throws IOException {
        SearchRequest searchRequest = new SearchRequest("bank");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //根据年龄聚合，求出每个年龄的平均工资
        TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders.terms("ageAgg").field("age");
        termsAggregationBuilder.subAggregation(AggregationBuilders.avg("balanceAgg").field("balance"));
        searchSourceBuilder.aggregation(termsAggregationBuilder);
        searchSourceBuilder.size(0);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, ESConfig.COMMON_OPTIONS);
        System.out.println("查询语句"+searchSourceBuilder.toString());
        Aggregations aggregations = searchResponse.getAggregations();
        Terms ageTerm = aggregations.get("ageAgg");
        List<? extends Terms.Bucket> buckets = ageTerm.getBuckets();
        for(Terms.Bucket bucket:buckets){
            System.out.println("年龄："+bucket.getKeyAsString());
            Avg balanceAgg = bucket.getAggregations().get("balanceAgg");
            System.out.println("平均薪资:"+balanceAgg.getValue());
        }

    }
}
