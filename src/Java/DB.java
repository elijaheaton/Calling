package Java;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class DB {
    Connection connection = null;

    /**
     * Database connector needed to start the server
     */
    public void dbConnect() {

        try {
            Class.forName("org.postgresql.Driver");

            this.connection = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/calling",
                    "caller",
                    "anunguessablepassword"
            );
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        System.out.println("Opened database successfully");
    }

    public void dbDisconnect() {
        try {
            assert (!this.connection.isClosed());
            this.connection.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() +": " + e.getMessage());
            System.exit(0);
        }
    }

    /**
     * Create a database if it doesn't exist
     */
    public void initializeDB() {
        try {
            Class.forName("org.postgresql.Driver");
            assert(!connection.isClosed());

            Statement statement = connection.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS CONNECTIONS " +
                    "(ID          CHAR(50)  NOT NULL," +
                    " NAME        CHAR(50)  NOT NULL," +
                    " CONNECTION  INT       NOT NULL," +
                    " PRIMARY KEY(ID, NAME) )";
            statement.executeUpdate(sql);
            statement.close();
            // connection.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }
        System.out.println("Table created successfully");
    }

    public Map<String, Integer> selectByID(String ID) {
        Map<String, Integer> map = new HashMap<>();
        try {
            Class.forName("org.postgresql.Driver");
            assert(!this.connection.isClosed());
            connection.setAutoCommit(false);

            Statement statement = this.connection.createStatement();
            ResultSet rs = statement.executeQuery(
                    "SELECT * FROM CONNECTIONS WHERE ID = '"+ID+"';"
            ); // TODO: what about sql injections?

            while ( rs.next() ) {
                String  name = rs.getString("name");
                int connect  = rs.getInt("connection");
                map.put(name, connect); // TODO: what if two people join with the same name?
            }
            rs.close();
            statement.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }
        System.out.println("Selection by ID done successfully");
        return map;
    }

    public boolean deleteByID(String ID) {
        try {
            Class.forName("org.postgresql.Driver");
            assert (!this.connection.isClosed());
            this.connection.setAutoCommit(false);

            Statement statement = this.connection.createStatement();
            String sql = "DELETE from COMPANY where ID = '"+ID+"';"; // TODO: sql injection
            statement.executeUpdate(sql);
            this.connection.commit();

            statement.close();

        } catch ( Exception e ) {
            return false;
        }
        return true; // TODO: just because it doesnt break, does that mean something was deleted?
    }

    public boolean insertConnection(String ID, String name, int connection) {

        try {
            Class.forName("org.postgresql.Driver");
            assert (!this.connection.isClosed());
            this.connection.setAutoCommit(false);

            Statement statement = this.connection.createStatement();
            String sql = "INSERT INTO CONNECTIONS (ID,NAME,CONNECTION) "
                    + "VALUES ('"+ID+"', '"+name+"', "+connection+");";
            // TODO: sql injections
            statement.executeUpdate(sql);

            statement.close();
            this.connection.commit();
        } catch (Exception e) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            return false;
        }
        return true; // TODO: does this mean that things are all good and fair in the world?
    }
}
