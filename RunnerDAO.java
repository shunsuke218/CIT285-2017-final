// Created by Shunsuke Haga
// Inherited the script wrote by IntelliJ IDEA (JCIAN)
// 

import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Logger;

public class RunnerDAO {
	/* Variale Declaration */
    private static Logger log = Logger.getLogger(RunnerDAO.class.getName());

	/* Constructor */
    public RunnerDAO() throws Exception {
	   String currentdir = System.getProperty("user.dir");
	   String url = System.getProperty("os.name").contains("OS X") ?
		   "jdbc:ucanaccess:///" + currentdir + "/runners.mdb" : // If OSX, use ucanaccess
		   "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=" + currentdir + "\\runners.mdb" ;  // If windows, use odbc.
    }

	/* Methods */
	// Connection and ResultSet
    private Connection getConnection() throws Exception {
        log.fine("getConnection called");
		return DriverManager.getConnection(url);
    }
	
    private void close(ResultSet resultSet, Statement statement, Connection connection) {
		while (true) {
			try {
				resultSet.close();
				statement.close();
				connection.close();
				break;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
    }

	/*
	// Create list of runners from ResultSet
    private List<Runner> resultSetToRunners(ResultSet resultSet) throws Exception {
        log.fine("resultSetToRunners called");
        ArrayList<Runner> runners = new ArrayList<Runner>();
        while (resultSet.next()) {
			Runner runner = new RunnerBuilder()
				.Gender(resultSet.getChar("A"))
				.PlaceOverall(resultSet.getInt("B"))
				.PlacecGender(resultSet.getInt("C"))
				.PlacecDivision(resultSet.getInt("D"))
				.LastName(resultSet.getString("E"))
				.FirstName(resultSet.getString("F"))
				.Country(resultSet.getString("G"))
				.Location(resultSet.getString("H"))
				.Bib(resultSet.getInt("I"))
				.Div(resultSet.getString("J"))
				.Age(resultSet.getInt("K"))
				.Half(resultSet.getString("L"))
				.Finish(resultSet.getString("M"))
				.buildRunner();
            runners.add(runner);
        }
        return runners;
    }
	*/

	// Edit Runners
    public List<Runner> getAllRunners() throws Exception {
        log.fine("getAllRunners called");
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
		ArrayList<Runner> runners = new ArrayList<Runner>();
        try {
            connection = getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM Amber ORDER BY B");
			//log.fine("resultSetToRunners called");
			while (resultSet.next()) {
				Runner runner = new RunnerBuilder()
					.Gender(resultSet.getString("A").charAt(0))
					.PlaceOverall(resultSet.getInt("B"))
					.PlacecGender(resultSet.getInt("C"))
					.PlacecDivision(resultSet.getInt("D"))
					.LastName(resultSet.getString("E"))
					.FirstName(resultSet.getString("F"))
					.Country(resultSet.getString("G"))
					.Location(resultSet.getString("H"))
					.Bib(resultSet.getInt("I"))
					.Div(resultSet.getString("J"))
					.Age(resultSet.getInt("K"))
					.Half(resultSet.getString("L"))
					.Finish(resultSet.getString("M"))
					.buildRunner();
				runners.add(runner);
			}
            return runners;
        } finally {
            close(resultSet, statement, connection);
        }
    }

    public void addRunner(Runner runner) throws Exception {
        log.fine("addRunner called");
		String query =
			"INSERT INTO Amber (A, B, C, D, E, F, G, H, I, J, K, L, M) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
		addEditRunner(runner, query);
    }

    public void editRunner(Runner runner) throws Exception {
        log.fine("updateRunner called");
		String query =
		"UPDATE Amber SET A = ?, B = ?, C = ?, D = ?, E = ?, F = ?, G = ?, H = ?, I = ?, J = ?, K = ?, L = ?, M = ? WHERE B = ?";
		addEditRunner(runner, query);
	}
	
	public void addEditRunner(Runner runner, String query) throws Exception {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = getConnection();
            statement = connection.prepareStatement(query);
			updateRunner(statement, runner);
        } finally {
            close(null, statement, connection);
        }
    }
	
    public void deleteRunner(Runner runner) throws Exception {
        log.fine("deleteRunner called");
		String query = "DELETE FROM Amber WHERE B = ?";
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, runner.getPlaceOverall());
            statement.executeUpdate();
        } finally {
            close(null, statement, connection);
        }
    }

	public void updateRunner(PreparedStatement statement, Runner runner) {
		try {
			int i = 1;
			statement.setString(i++, String.valueOf(runner.getGender()));
			statement.setInt(i++, runner.getPlaceOverall());
			statement.setInt(i++, runner.getPlaceGender());
			statement.setInt(i++, runner.getPlaceDivision());
			statement.setString(i++, runner.getLastName());
			statement.setString(i++, runner.getFirstName());
			statement.setString(i++, runner.getCountry());
			statement.setString(i++, runner.getLocation());
			statement.setInt(i++, runner.getBib());
			statement.setString(i++, runner.getDiv());
			statement.setInt(i++, runner.getAge());
			statement.setString(i++, runner.getHalfAsString());
			statement.setString(i++, runner.getFinishAsString());

			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
