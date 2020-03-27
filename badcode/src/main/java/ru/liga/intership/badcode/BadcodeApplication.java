package ru.liga.intership.badcode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.liga.intership.badcode.domain.Person;
import ru.liga.intership.badcode.service.PersonService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

@SpringBootApplication
public class BadcodeApplication {

    public static void main(String[] args) throws SQLException {
        SpringApplication.run(BadcodeApplication.class, args);

        PersonService personService = new PersonService(DriverManager.getConnection("jdbc:hsqldb:mem:test", "sa", ""));

        String query = "SELECT * FROM person WHERE sex = 'male' AND age > 18";

        List<Person> maleAgeGreaterThan18 = personService.getPersonsByQuery(query);

        double personAverageBMI = personService.getPersonsAverageBMI(maleAgeGreaterThan18);
        System.out.println(personAverageBMI);

        personService.closeConnection();

//        // После закрытия соединения метод работает без Exception-а, это норма?
//        // Даже если убрать final у conn будет работать

        double personAverageBMI2 = personService.getPersonsAverageBMI(maleAgeGreaterThan18);

        System.out.println(personAverageBMI2);
    }
}
