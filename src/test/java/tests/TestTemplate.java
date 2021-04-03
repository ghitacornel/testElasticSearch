package tests;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.After;
import org.junit.Before;

public abstract class TestTemplate {

    protected RestHighLevelClient client;
    protected RestHighLevelClient client1;
    protected RestHighLevelClient client2;
    protected RestHighLevelClient client3;

    @Before
    public void before() {
        client = getClient();
        client1 = getClient1();
        client2 = getClient2();
        client3 = getClient3();
    }


    @After
    public void after() throws Exception {
        client.close();
        client1.close();
        client2.close();
        client3.close();
    }

    private static RestHighLevelClient getClient() {
        return new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("localhost", 9200, "http"),
                        new HttpHost("localhost", 9201, "http"),
                        new HttpHost("localhost", 9202, "http")));
    }

    private static RestHighLevelClient getClient1() {
        return new RestHighLevelClient(RestClient.builder(new HttpHost("localhost", 9200, "http")));
    }

    private static RestHighLevelClient getClient2() {
        return new RestHighLevelClient(RestClient.builder(new HttpHost("localhost", 9201, "http")));
    }

    private static RestHighLevelClient getClient3() {
        return new RestHighLevelClient(RestClient.builder(new HttpHost("localhost", 9202, "http")));
    }

}
