package tests;

import config.Connection;
import model.Person;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import repository.PersonRepository;

import java.util.UUID;

public class TestPersonRepository {

    private RestHighLevelClient client;

    @Before
    public void before() {
        client = Connection.client();
    }


    @After
    public void after() throws Exception {
        client.close();
    }

    @Test
    public void testCRUD() {

        PersonRepository repository = new PersonRepository(client);

        // TODO run only once
        // create index
        {
//            repository.createIndex();
        }

        String id = UUID.randomUUID().toString();
        System.out.println("generated id = " + id);

        // read
        {
            Person person = repository.getById(id);
            Assert.assertNull(person);
        }

        // insert
        {
            Person person = new Person();
            person.setPersonId(id);
            person.setName("ion");

            repository.insert(person);
        }

        // read
        {
            Person person = repository.getById(id);
            Assert.assertNotNull(person);
            Assert.assertEquals(id, person.getPersonId());
            Assert.assertEquals("ion", person.getName());
        }

        // update
        {
            Person person = new Person();
            person.setPersonId(id);
            person.setName("gheorghe");

            Person updatedPerson = repository.update(person);
            Assert.assertNotNull(person);
            Assert.assertEquals(id, updatedPerson.getPersonId());
            Assert.assertEquals("gheorghe", updatedPerson.getName());
        }

        // read
        {
            Person person = repository.getById(id);
            Assert.assertNotNull(person);
            Assert.assertEquals(id, person.getPersonId());
            Assert.assertEquals("gheorghe", person.getName());
        }

        // delete
        {
            repository.deleteById(id);
        }

        // read
        {
            Person person = repository.getById(id);
            Assert.assertNull(person);
        }

    }
}
