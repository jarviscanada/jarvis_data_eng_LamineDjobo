package ca.jrvs.apps.jdbc;




import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCExecutor {

    private static final Logger logger = LoggerFactory.getLogger(JDBCExecutor.class);

    public static void main(String... args){



        DataBaseConnectionManager dcm = new DataBaseConnectionManager("localhost","5432","hplussport",
        "postgres","password");

        try {

            Connection connection = dcm.getConnection();
            CustomerDAO customerDAO = new CustomerDAO(connection);
            Customer customer = new Customer();
            customer.setFirstName("Lamine");
            customer.setLastName("Djobo");
            customer.setEmail("lamine.djobo@bob.com");
            customer.setPhone("(514) 345-4567");
            customer.setAddress("123 young st");
            customer.setCity("toronto");
            customer.setState("ON");
            customer.setZipCode("M5J 0T5");
            customerDAO.create(customer);

        } catch (SQLException e) {
            logger.error("SQL Exception occurred", e);
        }

        }


    }

