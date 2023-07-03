/*
 * Java / RDBMS integration by JDBC
 * 
 * https://github.com/egalli64/jd
 */
package com.example.jd.s09;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Another JavaBean for coders from the EMPLOYEE table
 * questa classe è un DTO: data transfer object, serve per portare dati in giro, faccio fuori il result set, trasformare dati in Java Bean e si distribuiscono
 */
public class Coder {
    private String firstName;
    private String lastName;
    private LocalDate hired;
    private double salary;

    public Coder() {
    }

    public Coder(String firstName, String lastName, LocalDate hired, double salary) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.hired = hired;
        this.salary = salary;
    }

    public Coder(String firstName, String lastName, double salary) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.hired = LocalDate.now();
        this.salary = salary;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getHired() {
        return hired;
    }

    public void setHired(LocalDate hired) {
        this.hired = hired;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Coder that = (Coder) o;
        return Double.compare(that.salary, salary) == 0 && Objects.equals(firstName, that.firstName)
                && Objects.equals(lastName, that.lastName) && Objects.equals(hired, that.hired);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, hired, salary);
    }

    @Override
    public String toString() {
        return "Coder [firstName=" + firstName + ", lastName=" + lastName + ", hired=" + hired + ", salary=" + salary
                + "]";
    }
}