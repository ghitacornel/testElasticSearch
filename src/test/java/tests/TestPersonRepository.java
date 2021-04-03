package tests;

import model.Person;
import org.elasticsearch.ElasticsearchStatusException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import repository.PersonRepository;

import java.util.UUID;

public class TestPersonRepository extends TestTemplate {

    PersonRepository repository;

    @Before
    public void ensureIndexExists() {

        repository = new PersonRepository(client);

        // TODO run only once
        // create index
        try {
            repository.createIndex();
        } catch (ElasticsearchStatusException e) {
            System.err.println("index exists " + e.getMessage());
        }

    }

    @Test
    public void testCRUD() {

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
