package com.matay.customer;

import com.matay.exception.DuplicateResourceException;
import com.matay.exception.RequestValidationException;
import com.matay.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerDao customerDao;
    private CustomerService underTest;

    @BeforeEach
    void setUp() {
        underTest = new CustomerService(customerDao);
    }

    @Test
    void getAllCustomers() {
        //WHEN
        underTest.getAllCustomers();

        //THEN
        verify(customerDao).selectAllCustomers();
    }

    @Test
    void canGetCustomerById() {
        //GIVEN
        int id = 1;
        Customer customer = new Customer(
                id, "Matay", "matay@code.com", 25
        );
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        //WHEN
        Customer actual = underTest.getCustomerById(id);

        //THEN
        assertThat(actual).isEqualTo(customer);
    }

    @Test
    void willThrowWhenGetCustomerReturnsEmptyOptional() {
        //GIVEN
        int id = 1;
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.empty());

        //WHEN
        //THEN
        assertThatThrownBy(() -> underTest.getCustomerById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Customer with id [%s] not found".formatted(id));

    }

    @Test
    void addCustomer() {
        //GIVEN
        String email = "matay@code.com";

        when(customerDao.existsPersonWithEmail(email)).thenReturn(false);

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                "Matay", email, 25
        );

        //WHEN
        underTest.addCustomer(request);

        //THEN
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).insertCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getId()).isNull();
        assertThat(capturedCustomer.getName()).isEqualTo(request.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(request.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(request.age());
    }

    @Test
    void shouldThrowWhenEmailTakenWhileAddingCustomer() {
        //GIVEN
        String email = "matay@code.com";

        when(customerDao.existsPersonWithEmail(email)).thenReturn(true);

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                "Matay", email, 25
        );

        //WHEN
        assertThatThrownBy(() -> underTest.addCustomer(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Email already in use. Please choose a different email address.");

        //THEN
        verify(customerDao, never()).insertCustomer(any());
    }

    @Test
    void deleteCustomerById() {
        //GIVEN
        int id = 1;
        when(customerDao.existPersonWithId(id)).thenReturn(true);

        //WHEN
        underTest.deleteCustomerById(id);

        //THEN
        verify(customerDao).deleteCustomerById(id);
    }

    @Test
    void willThrowWhenIdIsNotPresentWhenDeletingCustomer() {
        //GIVEN
        int id = 1;
        when(customerDao.existPersonWithId(id)).thenReturn(false);

        //WHEN
        assertThatThrownBy(() -> underTest.deleteCustomerById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Customer with id [%s] not found".formatted(id));

        //THEN
        verify(customerDao, never()).deleteCustomerById(any());
    }

    @Test
    void canUpdateAllCustomersProperties() {
        //GIVEN
        int id = 1;
        Customer customer = new Customer(
                id, "Matay", "matay@code.com", 25
        );
        when(customerDao.selectCustomerById(1)).thenReturn(Optional.of(customer));

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                "yataM", "code@matay.com", 52
        );
        when(customerDao.existsPersonWithEmail(updateRequest.email())).thenReturn(false);

        //WHEN
        underTest.updateCustomer(id,updateRequest);

        //THEN
        ArgumentCaptor<Customer> customerArgumentCaptor =
                ArgumentCaptor.forClass(Customer.class);

        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(updateRequest.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(updateRequest.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(updateRequest.age());
    }

    @Test
    void canUpdateOnlyCustomerName() {
        //GIVEN
        int id = 1;
        Customer customer = new Customer(
                id, "Matay", "matay@code.com", 25
        );
        when(customerDao.selectCustomerById(1)).thenReturn(Optional.of(customer));

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                "yataM", null, null
        );

        //WHEN
        underTest.updateCustomer(id,updateRequest);

        //THEN
        ArgumentCaptor<Customer> customerArgumentCaptor =
                ArgumentCaptor.forClass(Customer.class);

        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(updateRequest.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());
    }

    @Test
    void canUpdateOnlyCustomerEmail() {
        //GIVEN
        int id = 1;
        Customer customer = new Customer(
                id, "Matay", "matay@code.com", 25
        );
        when(customerDao.selectCustomerById(1)).thenReturn(Optional.of(customer));

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                null, "code@matay.com", null
        );
        when(customerDao.existsPersonWithEmail(updateRequest.email())).thenReturn(false);

        //WHEN
        underTest.updateCustomer(id,updateRequest);

        //THEN
        ArgumentCaptor<Customer> customerArgumentCaptor =
                ArgumentCaptor.forClass(Customer.class);

        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(capturedCustomer.getEmail()).isEqualTo(updateRequest.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());
    }

    @Test
    void canUpdateOnlyCustomerAge() {
        //GIVEN
        int id = 1;
        Customer customer = new Customer(
                id, "Matay", "matay@code.com", 25
        );
        when(customerDao.selectCustomerById(1)).thenReturn(Optional.of(customer));

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                null, null, 52
        );

        //WHEN
        underTest.updateCustomer(id,updateRequest);

        //THEN
        ArgumentCaptor<Customer> customerArgumentCaptor =
                ArgumentCaptor.forClass(Customer.class);

        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
        assertThat(capturedCustomer.getAge()).isEqualTo(updateRequest.age());
    }

    @Test
    void willThrowWhenUpdatedEmailIsTaken() {
        //GIVEN
        int id = 1;
        Customer customer = new Customer(
                id, "Matay", "matay@code.com", 25
        );
        when(customerDao.selectCustomerById(1)).thenReturn(Optional.of(customer));

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                customer.getName(), "code@matay.com" , customer.getAge()
        );

        //WHEN
        when(customerDao.existsPersonWithEmail(updateRequest.email())).thenReturn(true);

        assertThatThrownBy(() -> underTest.updateCustomer(id, updateRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Email already in use. Please choose a different email address.");

        //THEN
        verify(customerDao, never()).updateCustomer(any());
    }

    @Test
    void willThrowWhenCustomerUpdateHasNoChanges() {
        //GIVEN
        int id = 1;
        Customer customer = new Customer(
                id, "Matay", "matay@code@gmail.com", 25
        );

        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        //WHEN
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                customer.getName(), customer.getEmail(), customer.getAge()
        );

        assertThatThrownBy(() -> underTest.updateCustomer(id, updateRequest))
                .isInstanceOf(RequestValidationException.class)
                .hasMessage("No data changes found");

        //THEN
        verify(customerDao, never()).updateCustomer(any());
    }
}