package tests;

import config.Connection;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.After;
import org.junit.Before;

public abstract class TestsTemplate {

    protected RestHighLevelClient client;

    @Before
    public void before() {
        client = Connection.client();
    }


    @After
    public void after() throws Exception {
        client.close();
    }

}
