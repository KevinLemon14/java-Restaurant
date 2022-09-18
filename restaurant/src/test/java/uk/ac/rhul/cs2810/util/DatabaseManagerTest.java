package uk.ac.rhul.cs2810.util;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.jupiter.api.Test;

/**
 * Test case class for executing jUnit tests associated to the DatabaseManager Class.
 * 
 * @author Marley Dey
 *
 */
class DatabaseManagerTest {

  // Test that the database manager can only be assess as a single instance
  @Test
  void testSingletonInstance() {
    assertNotNull(DatabaseManager.getInstance());
  }

  // Test that you can create a valid prepared statement and that it does return null due to an
  // exception occurring.
  @Test
  void testCreatingSimplePrepStatement() {
    assertNotNull(DatabaseManager.getInstance()
        .createPreparedStatement("SELECT * FROM TestTable WHERE id=?;", 2),
        "Creating a valid prepared statement should not return a null object");
  }

  // Test that you can create a valid prepared statement and that it does return null due to an
  // exception occurring with multiple arguments.
  @Test
  void testCreatingSimplePrepStatementMutipleArgs() {
    assertNotNull(
        DatabaseManager.getInstance()
            .createPreparedStatement("SELECT * FROM TestTable WHERE id=? age=?;", 2, 21),
        "Creating a valid prepared statement should not return a null object");
  }

  // Test you can disconnect from the database connection.
  @Test
  void testDisconnectingConnection() {
    assertDoesNotThrow(() -> {
      DatabaseManager.getInstance().disconnect();
    }, "Disconnecting successfully or not from the database should not throw any exceptions as "
        + "they should be handled");
  }

  // Test that you can execute a prepared statement.
  @Test
  void testExecutingPreparedStatement() {
    assertDoesNotThrow(() -> {
      DatabaseManager dm = DatabaseManager.getInstance();
      dm.execute(dm.createPreparedStatement(
          "INSERT INTO TestTable (name, age, description) VALUES (?, ?, ?)", "Peter", 23,
          "Must be a twit"));
    }, "Executing a valid prepared statement should not result in any exceptions being thrown");
  }

  // Test that you can get a column results from a table
  @Test
  void testQuerySelectingAll() {
    assertDoesNotThrow(() -> {
      DatabaseManager dm = DatabaseManager.getInstance();
      ResultSet rs =
          dm.query(dm.createPreparedStatement("SELECT * FROM TestTable WHERE age=?", 43));
      if (rs.next()) {
        System.out.println(rs.getString("name"));
      }
      try {
        rs.getStatement().close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }, "Querying and selecting a column from a resultSet should not throw any exceptions");
  }

  // Test that you can get the next auto increment id in a table
  @Test
  void testGettingNextID() {
    assertNotEquals(-1, DatabaseManager.getInstance().getNextAutoIncrement("TestTable"),
        "Getting the next auto incremented id value should not return the error -1 value.");
  }

}
