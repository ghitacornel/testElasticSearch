package tests;

import model.Person;
import org.elasticsearch.ElasticsearchStatusException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import repository.PersonRepository;

import java.util.UUID;

public class TestSynchronizationBetweenDifferentMachines extends TestTemplate {

    PersonRepository repository1;
    PersonRepository repository2;
    PersonRepository repository3;

    @Before
    public void ensureIndexExists() {

        repository1 = new PersonRepository(client1);
        repository2 = new PersonRepository(client2);
        repository3 = new PersonRepository(client3);

        // TODO run only once
        // create index
        try {
            repository1.createIndex();
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
            Person person = repository1.getById(id);
            Assert.assertNull(person);
        }

        // insert
        {
            Person person = new Person();
            person.setPersonId(id);
            person.setName("ion");

            repository1.insert(person);
        }

        // read
        {
            Person person = repository2.getById(id);
            Assert.assertNotNull(person);
            Assert.assertEquals(id, person.getPersonId());
            Assert.assertEquals("ion", person.getName());
        }

        // update
        {
            Person person = new Person();
            person.setPersonId(id);
            person.setName("gheorghe");

            Person updatedPerson = repository2.update(person);
            Assert.assertNotNull(person);
            Assert.assertEquals(id, updatedPerson.getPersonId());
            Assert.assertEquals("gheorghe", updatedPerson.getName());
        }

        // read
        {
            Person person = repository3.getById(id);
            Assert.assertNotNull(person);
            Assert.assertEquals(id, person.getPersonId());
            Assert.assertEquals("gheorghe", person.getName());
        }

        // delete
        {
            repository3.deleteById(id);
        }

        // read
        {
            Person person = repository1.getById(id);
            Assert.assertNull(person);
        }

    }
}
