package uk.ac.rhul.cs2810.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * A Database API for connecting to the management database. Documentation on how to use the API can
 * be found on the project wiki on the repository page.
 * 
 * @author Marley Dey
 *
 */
public class DatabaseManager {

  private Connection connection;

  private String url;
  private String database;
  private String username;
  private String password;

  private static DatabaseManager instance = new DatabaseManager();


  /**
   * Constructor for connecting to the default database.
   */
  private DatabaseManager() {
    this.database = "RestaurantManagementDB";
    this.password = "GrilledCheese1";
    this.url = "51.195.137.215";
    this.username = "team27";
  }

  /*
   * Tests that the connection to the database is not already open or exists.
   */
  private boolean isConnectionOpen() {
    try {
      return connection != null && !connection.isClosed();
    } catch (SQLException ignored) {
      return false;
    }
  }

  /*
   * Connect to the MySQL database specified by the constructor. Finds the correct driver and
   * attempts a connection with the database.
   */
  private synchronized void attemptConnection() {

    // Find the mySQL driver for the DriverManager class.
    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
    } catch (ClassNotFoundException e) {
      System.out.println("The Driver class for the MySQL connection could not be found!");
      e.printStackTrace();
    }

    try {
      if (isConnectionOpen()) { // Is the connection already active?
        return;
      }

      // Attempt to connect to the database with the parsed details.
      connection = DriverManager.getConnection(
          "jdbc:mysql://" + url + "/" + database + "?user=" + username + "&password=" + password);
      System.out.println("Connection [MySQL] was established to the Database: " + database + "!");

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Create a prepared statement for the database server, to query or execute in it. This does not
   * execute or query the code, it simply helps create a prepared statement.
   * 
   * @param statement The initial un-formated statement with question marks.
   * @param args The objects that will replace the question marks.
   * @return The prepared statement constructed.
   */
  public synchronized PreparedStatement createPreparedStatement(String statement, Object... args) {
    if (!isConnectionOpen()) { // If not connection, attempt to connect to database.
      attemptConnection();
    }

    try {
      PreparedStatement ps = connection.prepareStatement(statement);

      // Set all arguments in the statement
      for (int i = 0; i < args.length; i++) {
        ps.setObject(i + 1, args[i]);
      }

      return ps;

    } catch (SQLException e) {
      System.out.println("The prepared statement " + statement + " could not be constructed!");
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Executes the prepared statement and handles any potential exceptions that could be thrown.
   * 
   * @param ps The prepared statement that the database will execute.
   */
  public synchronized void execute(PreparedStatement ps) {
    try {
      ps.execute();
    } catch (SQLException e) {
      System.out.println("The prepared statement could not be executed!");
      e.printStackTrace();
    } finally {

      try {
        ps.close();
      } catch (SQLException e) {
        System.out.println("The prepared statement could not be closed!");
        e.printStackTrace();
      }
    }
  }

  /**
   * Closes the connection to the database.
   */
  public synchronized void disconnect() {
    try {
      if (!isConnectionOpen()) {
        return;
      }

      connection.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Query the database and receive back a result set to get values from. When done with the result
   * set you should close the statement that produced the ReusltSet.
   * 
   * @param ps The prepared statement to query the database with.
   * @return The result set the query produced.
   */
  public synchronized ResultSet query(PreparedStatement ps) {
    try {
      return ps.executeQuery();

    } catch (SQLException e) {
      System.out.println("The prepared statement could not be queried!");
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Checks to see whether a table exists in the database by its table name.
   * 
   * @param tableName The name of the table as it appears in the database.
   * @return Whether the table exists in the database.
   */
  public synchronized boolean tableExists(String tableName) {
    ResultSet rs = query(createPreparedStatement("SHOW TABLES LIKE ?;", tableName));

    boolean exists = false;
    try {
      exists = rs.next();
      rs.getStatement().close();
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return exists;
  }

  /**
   * In a table where the primary key is auto incremented, this will help get the next id number.
   * 
   * @param tableName The table with auto increment.
   * @return Next id number.
   */
  public synchronized int getNextAutoIncrement(String tableName) {
    ResultSet rs = query(createPreparedStatement(
        "SELECT `auto_increment` FROM INFORMATION_SCHEMA.TABLES WHERE table_name = ?;", tableName));

    int nextValue = -1;
    try {
      if (!rs.next()) {
        return nextValue;
      }
      nextValue = rs.getInt("auto_increment");
      rs.getStatement().close();
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return nextValue;
  }

  /**
   * Returns the singleton instance of the database manager.
   */
  public static DatabaseManager getInstance() {
    return instance;
  }

}
