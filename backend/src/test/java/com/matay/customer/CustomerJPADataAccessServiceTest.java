package com.matay.customer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

class CustomerJPADataAccessServiceTest {

    private CustomerJPADataAccessService underTest;
    private AutoCloseable autoCloseable;

    @Mock
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new CustomerJPADataAccessService(customerRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void selectAllCustomers() {
        //WHEN
        underTest.selectAllCustomers();

        //THEN
        verify(customerRepository)
                .findAll();
    }

    @Test
    void selectCustomerById() {
        //GIVEN
        int id = 1;

        //WHEN
        underTest.selectCustomerById(id);

        //THEN
        verify(customerRepository)
                .findById(id);
    }

    @Test
    void insertCustomer() {
        //GIVEN
        Customer customer = new Customer(
                1, "matay", "matay@code.com", 25
        );

        //WHEN
        underTest.insertCustomer(customer);

        //THEN
        verify(customerRepository).save(customer);
    }

    @Test
    void existsPersonWithEmail() {
        //GIVEN
        String email = "matay@code.com";

        //WHEN
        underTest.existsPersonWithEmail(email);

        //THEN
        verify(customerRepository).existsCustomerByEmail(email);
    }

    @Test
    void existPersonWithId() {
        //GIVEN
        int id = 1;

        //WHEN
        underTest.existPersonWithId(id);

        //THEN
        verify(customerRepository).existsCustomerById(id);
    }

    @Test
    void deleteCustomerById() {
        //GIVEN
        int id = 1;

        //WHEN
        underTest.deleteCustomerById(id);

        //THEN
        verify(customerRepository).deleteById(id);
    }

    @Test
    void updateCustomer() {
        //GIVEN
        Customer customer = new Customer(
                1, "matay", "matay@code.com", 25
        );

        //WHEN
        underTest.updateCustomer(customer);

        //THEN
        verify(customerRepository).save(customer);
    }
}