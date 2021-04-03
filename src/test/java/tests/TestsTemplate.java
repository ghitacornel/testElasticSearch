package tests;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.After;
import org.junit.Before;

public abstract class TestsTemplate {

    protected RestHighLevelClient client;

    @Before
    public void before() {
        client = getClient();
    }


    @After
    public void after() throws Exception {
        client.close();
    }

    private static RestHighLevelClient getClient() {
        return new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("localhost", 9200, "http"),
                        new HttpHost("localhost", 9201, "http"),
                        new HttpHost("localhost", 9202, "http")));
    }

}
