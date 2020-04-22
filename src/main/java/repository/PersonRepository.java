package repository;

import model.Person;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.common.xcontent.XContentType;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PersonRepository {

    private final RestHighLevelClient client;
    private static final String INDEX = "persondata";

    public PersonRepository(RestHighLevelClient client) {
        this.client = client;
    }

    public void createIndex() {
        CreateIndexRequest request = new CreateIndexRequest(INDEX);
        try {
            CreateIndexResponse response = client.indices().create(request, RequestOptions.DEFAULT);
            System.out.println(response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void insert(Person person) {

        // to map
        Map<String, Object> map = new HashMap<>();
        map.put("personId", person.getPersonId());
        map.put("name", person.getName());

        IndexRequest request = new IndexRequest(INDEX).source(map).id(person.getPersonId());
        try {
            System.out.println("insert request " + request);
            IndexResponse response = client.index(request, RequestOptions.DEFAULT);
            System.out.println("insert response " + response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Person getById(String id) {
        GetRequest request = new GetRequest(INDEX);
        request.id(id);

        GetResponse response;
        try {
            System.out.println("get request " + request);
            response = client.get(request, RequestOptions.DEFAULT);
            System.out.println("get response " + response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (!response.isExists()) return null;


        // from map
        Map<String, Object> map = response.getSourceAsMap();
        Person person = new Person();
        person.setPersonId((String) map.get("personId"));
        person.setName((String) map.get("name"));

        return person;
    }

    public Person update(Person person) {

        try {
            UpdateRequest request = new UpdateRequest(INDEX, person.getPersonId()).fetchSource(true);

            // to map
            Map<String, Object> map = new HashMap<>();
            map.put("personId", person.getPersonId());
            map.put("name", person.getName());
            request.doc(map, XContentType.JSON);

            // update
            System.out.println("update request " + request);
            UpdateResponse response = client.update(request, RequestOptions.DEFAULT);
            System.out.println("update response " + response);

            // from map
            {
                Map<String, Object> responseMap = response.getGetResult().sourceAsMap();
                Person personResponse = new Person();
                personResponse.setPersonId((String) responseMap.get("personId"));
                personResponse.setName((String) responseMap.get("name"));
                return personResponse;
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteById(String id) {
        DeleteRequest request = new DeleteRequest(INDEX, id);
        try {
            System.out.println("delete request " + request);
            DeleteResponse response = client.delete(request, RequestOptions.DEFAULT);
            System.out.println("delete response " + response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
