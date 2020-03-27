package ru.liga.intership.badcode;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.liga.intership.badcode.domain.Person;
import ru.liga.intership.badcode.service.PersonService;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BadcodeApplicationTests {
    static PersonService personService;

    @Before
    public void openConnection() {
        try {
            personService = new PersonService(DriverManager.getConnection("jdbc:hsqldb:mem:test", "sa", ""));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @After
    public void closeConnection() {
        personService.closeConnection();
    }

    @Test
    public void contextLoads() {
        assertNotNull(personService.getConn());
    }

    @Test
    public void testPersonlistByQuery() {
        String query = "SELECT * FROM person WHERE sex = 'female'";
        List<Person> personList = personService.getPersonsByQuery(query);
        assertFalse(personList.isEmpty());
    }

//	@Test(expected = SQLSyntaxErrorException.class)
//	public void testPersonlistByWrongQuery(){
//		String query = "SELECT * FROM person WHERE keks = 'female'";
//		List<Person> personList = personService.getPersonsByQuery(query);
//	}

    @Test
    public void testPersonAverageImt() {
        String query = "SELECT * FROM person WHERE sex = 'male' AND age > 18";
        List<Person> personList = personService.getPersonsByQuery(query);
        int averageBMI = (int) personService.getPersonsAverageBMI(personList);
        assertEquals(25, averageBMI);
    }

}
