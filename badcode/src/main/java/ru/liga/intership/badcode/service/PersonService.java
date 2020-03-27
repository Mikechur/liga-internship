package ru.liga.intership.badcode.service;


import ru.liga.intership.badcode.domain.Person;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersonService {

    private final Connection conn;

    public PersonService(Connection conn) {
        this.conn = conn;
    }

    /**
     * Возвращает средний индекс массы тела всех лиц в переданном списке
     *
     * @return
     */
    public double getPersonsAverageBMI(List<Person> persons) {
        double totalImt = 0.0;
        long countOfPerson = 0;
        try {

            for (Person p : persons) {
                double heightInMeters = p.getHeight() / 100d;
                double imt = p.getWeight() / (Double) (heightInMeters * heightInMeters);
                totalImt += imt;
            }
            countOfPerson = persons.size();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return totalImt / countOfPerson;
    }

    public List<Person> getPersonsByQuery(String query){
        List<Person> foundedPersons = new ArrayList<>();
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                Person p = new Person();
                //Retrieve by column name
                p.setId(rs.getLong("id"));
                p.setSex(rs.getString("sex"));
                p.setName(rs.getString("name"));
                p.setAge(rs.getLong("age"));
                p.setWeight(rs.getLong("weight"));
                p.setHeight(rs.getLong("height"));
                foundedPersons.add(p);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return foundedPersons;
    }

    public void closeConnection(){
        try {
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public Connection getConn() {
        return conn;
    }

}
