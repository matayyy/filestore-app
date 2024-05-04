package com.matay.customer;

import com.matay.AbstractTestcontainers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.jupiter.api.Assertions.*;

class CustomerJDBCDataAccessServiceTest extends AbstractTestcontainers {

    private CustomerJDBCDataAccessService underTest;
    private CustomerRowMapper customerRowMapper = new CustomerRowMapper();


    @BeforeEach
    void setUp() {
        underTest = new CustomerJDBCDataAccessService(
                getJdbcTemplate(),
                customerRowMapper
        );
    }

    @Test
    void selectAllCustomers() {
        //GIVEN

        //WHEN

        //THEN
    }

    @Test
    void selectCustomerById() {
        //GIVEN

        //WHEN

        //THEN
    }

    @Test
    void insertCustomer() {
        //GIVEN

        //WHEN

        //THEN
    }

    @Test
    void existsPersonWithEmail() {
        //GIVEN

        //WHEN

        //THEN
    }

    @Test
    void existPersonWithId() {
        //GIVEN

        //WHEN

        //THEN
    }

    @Test
    void deleteCustomerById() {
        //GIVEN

        //WHEN

        //THEN
    }

    @Test
    void updateCustomer() {
        //GIVEN

        //WHEN

        //THEN
    }
}