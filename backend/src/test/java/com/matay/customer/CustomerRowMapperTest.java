package com.matay.customer;

import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CustomerRowMapperTest {

    @Test
    void mapRow() throws SQLException {
        //GIVEN
        CustomerRowMapper customerRowMapper = new CustomerRowMapper();

        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getString("name")).thenReturn("Matay");
        when(resultSet.getString("email")).thenReturn("matay@code.com");
        when(resultSet.getInt("age")).thenReturn(19);


        //WHEN
        Customer actual = customerRowMapper.mapRow(resultSet, 1);

        //THEN
        Customer expected = new Customer(
                1, "Matay", "matay@code.com", 19
        );

        assertThat(actual).isEqualTo(expected);
    }
}