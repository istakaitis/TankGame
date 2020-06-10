package com.tools.linkmode;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * SSH Port forwarding
 */
public class SSHService {
	static Integer lport;// local port
	static String rhost;// remote database host
	static int rport;// remote database port

	static String user;// ssh username
	static String password;// ssh password
	static String host;// ssh host
	static int port;// ssh port

	static {
		// read properties
		try {
			// get the path of properties file
			InputStream is = SSHService.class.getClassLoader().getResourceAsStream("SSH.properties");
			Properties prop = new Properties();
			prop.load(is);
			// read value from properties
			lport = Integer.valueOf(prop.getProperty("lport"));
			rhost = prop.getProperty("rhost");
			rport = Integer.valueOf(prop.getProperty("rport"));
			user = prop.getProperty("user");
			password = prop.getProperty("password");
			host = prop.getProperty("host");
			port = Integer.valueOf(prop.getProperty("port"));

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void sshRun() {
		JSch jsch = new JSch();
		Session session = null;
		try {
			session = jsch.getSession(user, host, port);
			session.setPassword(password);
			session.setConfig("StrictHostKeyChecking", "no");
			// step1: link to ssh
			session.connect();
			System.out.println(session.getServerVersion());
			// step2: ssh local port forwarding
			int assinged_port = session.setPortForwardingL(lport, rhost, rport);
			System.out.println("localhost:" + assinged_port + " -> " + rhost + ":" + rport);
		} catch (Exception e) {
			if (null != session) {
				// close ssh
				session.disconnect();
			}
			e.printStackTrace();
		}
	}

	public void insertUser(User user) {
		// sshRun();
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		Connection conn = null;
		PreparedStatement ps1 = null;
		PreparedStatement ps2 = null;
		try {
			conn = DriverManager.getConnection("jdbc:postgresql://localhost:5455/godavari", "godavari", "w33858a9yf");

			String sql1 = "insert into users (username,password,login_attempts) values (?,?,?)";
			ps1 = conn.prepareStatement(sql1);

			ps1.setString(1, user.getUsername());
			ps1.setString(2, user.getPassword());
			ps1.setInt(3, user.getLogin_attempts());

			String sql2 = "insert into statistics (username, games_played, wins, losses, kills, deaths, win_rate,"
					+ "kill_death_ratio) values (?,?,?,?,?,?,?,?)";
			ps2 = conn.prepareStatement(sql2);
			ps2.setString(1, user.getUsername());
			ps2.setInt(2, 0);
			ps2.setInt(3, 0);
			ps2.setInt(4, 0);
			ps2.setInt(5, 0);
			ps2.setInt(6, 0);
			ps2.setString(7, "0");
			ps2.setString(8, "0");

			ps1.execute();
			ps2.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (ps1 != null) {
				try {
					ps1.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (ps2 != null) {
				try {
					ps2.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void updateStatistics(Statistics ss) {
		// sshRun();
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = DriverManager.getConnection("jdbc:postgresql://localhost:5455/godavari", "godavari", "w33858a9yf");

			String sql = "update statistics set games_played = ?,wins = ?,losses = ?,kills = ?,deaths = ?,"
					+ "win_rate = ?,kill_death_ratio = ? where username = ?;";
			ps = conn.prepareStatement(sql);

			ps.setInt(1, ss.getGames_played());
			ps.setInt(2, ss.getWins());
			ps.setInt(3, ss.getLosses());
			ps.setInt(4, ss.getTotal_kills());
			ps.setInt(5, ss.getTotal_deaths());
			ps.setString(6, ss.getWin_rate());
			ps.setString(7, ss.getKill_death_ratio());
			ps.setString(8, ss.getUsername());
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void updateUser(User user) {
		// sshRun();
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DriverManager.getConnection("jdbc:postgresql://localhost:5455/godavari", "godavari", "w33858a9yf");
			String sql = "update users set password = ?,login_attempts = ? where username = ?";
			ps = conn.prepareStatement(sql);

			ps.setString(1, user.getPassword());
			ps.setInt(2, user.getLogin_attempts());
			ps.setString(3, user.getUsername());

			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void deleteUser(String username) {
		// sshRun();
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = DriverManager.getConnection("jdbc:postgresql://localhost:5455/godavari", "godavari",
					"w33858a9yf");
			String sql = "delete from users where username = ?";
			preparedStatement = connection.prepareStatement(sql);

			preparedStatement.setString(1, username);
			preparedStatement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (preparedStatement != null) {
				try {
					preparedStatement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public void deleteStatistics(String username) {
		// sshRun();
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = DriverManager.getConnection("jdbc:postgresql://localhost:5455/godavari", "godavari",
					"w33858a9yf");
			String sql = "delete from statistics where username = ?";
			preparedStatement = connection.prepareStatement(sql);

			preparedStatement.setString(1, username);
			preparedStatement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (preparedStatement != null) {
				try {
					preparedStatement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public User selectUser(String username) {
		// sshRun();
		User user = null;
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		Connection conn = null;
		PreparedStatement ps = null;

		ResultSet rs = null;
		String sql = "select * from users where username = ?";
		try {
			conn = DriverManager.getConnection("jdbc:postgresql://localhost:5455/godavari", "godavari", "w33858a9yf");
			ps = conn.prepareStatement(sql);
			ps.setString(1, username);
			rs = ps.executeQuery();
			while (rs.next()) {
				user = new User();
				user.setUsername(rs.getString("username"));
				user.setPassword(rs.getString("password"));
				user.setLogin_attempts(rs.getInt("login_attempts"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		return user;
	}

	public Statistics selectStatistics(String username) {
		// sshRun();
		Statistics statistics = null;
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		Connection conn = null;
		PreparedStatement ps = null;

		ResultSet rs = null;
		String sql = "select * from statistics where username = ?";
		try {
			conn = DriverManager.getConnection("jdbc:postgresql://localhost:5455/godavari", "godavari", "w33858a9yf");
			ps = conn.prepareStatement(sql);
			ps.setString(1, username);
			rs = ps.executeQuery();
			while (rs.next()) {
				statistics = new Statistics();
				statistics.setUsername(rs.getString("username"));
				statistics.setGames_played(rs.getInt("games_played"));
				statistics.setWins(rs.getInt("wins"));
				statistics.setLosses(rs.getInt("losses"));
				statistics.setTotal_kills(rs.getInt("kills"));
				statistics.setTotal_deaths(rs.getInt("deaths"));
				statistics.setWin_rate();
				statistics.setKill_death_ratio();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return statistics;
	}

	public void selectTop1() {
		// sshRun();
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		Connection conn = null;
		PreparedStatement ps_wr = null;
		PreparedStatement ps_kdr = null;

		ResultSet rs_wr = null;
		ResultSet rs_kdr = null;

		String sql_wr = "select username, win_rate from statistics order by win_rate desc " + "limit 1;";
		String sql_kdr = "select username, kill_death_ratio from statistics order by kill_death_ratio desc "
				+ "limit 1;";

		try {
			conn = DriverManager.getConnection("jdbc:postgresql://localhost:5455/godavari", "godavari", "w33858a9yf");

			ps_wr = conn.prepareStatement(sql_wr);
			rs_wr = ps_wr.executeQuery();
			while (rs_wr.next()) {
				System.out
						.println("WIN RATE TOP 1\n" + rs_wr.getString("username") + " " + rs_wr.getString("win_rate"));
			}

			ps_kdr = conn.prepareStatement(sql_kdr);
			rs_kdr = ps_kdr.executeQuery();
			while (rs_kdr.next()) {
				System.out.println("KILL DEATH RATIO TOP 1\n" + rs_kdr.getString("username") + " "
						+ rs_kdr.getString("kill_death_ratio"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs_wr != null) {
				try {
					rs_wr.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (ps_wr != null) {
				try {
					ps_wr.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			if (rs_kdr != null) {
				try {
					rs_kdr.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (ps_kdr != null) {
				try {
					ps_kdr.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public Statistics selectTopWins() {
		Statistics statistics = null;
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		Connection conn = null;
		PreparedStatement ps_wins = null;

		ResultSet rs_wins = null;

		String sql_wins = "select username, wins from statistics order by wins desc " + "limit 1;";

		try {
			conn = DriverManager.getConnection("jdbc:postgresql://localhost:5455/godavari", "godavari", "w33858a9yf");

			ps_wins = conn.prepareStatement(sql_wins);
			rs_wins = ps_wins.executeQuery();
			while (rs_wins.next()) {
				statistics = new Statistics();
				statistics.setUsername(rs_wins.getString("username"));
				statistics.setWins(rs_wins.getInt("wins"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs_wins != null) {
				try {
					rs_wins.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (ps_wins != null) {
				try {
					ps_wins.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return statistics;
	}

	public Statistics selectTopKills() {
		Statistics statistics = null;
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		Connection conn = null;
		PreparedStatement ps_kills = null;

		ResultSet rs_kills = null;

		String sql_kills = "select username, kills from statistics order by kills desc " + "limit 1;";

		try {
			conn = DriverManager.getConnection("jdbc:postgresql://localhost:5455/godavari", "godavari", "w33858a9yf");

			ps_kills = conn.prepareStatement(sql_kills);
			rs_kills = ps_kills.executeQuery();
			while (rs_kills.next()) {
				statistics = new Statistics();
				statistics.setUsername(rs_kills.getString("username"));
				statistics.setTotal_kills(rs_kills.getInt("kills"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs_kills != null) {
				try {
					rs_kills.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (ps_kills != null) {
				try {
					ps_kills.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

		}
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return statistics;
	}

	public Statistics selectTopWinRate() {
		Statistics statistics = null;
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		Connection conn = null;
		PreparedStatement ps_wr = null;

		ResultSet rs_wr = null;

		String sql_wr = "select * from statistics order by win_rate desc " + "limit 1;";

		try {
			conn = DriverManager.getConnection("jdbc:postgresql://localhost:5455/godavari", "godavari", "w33858a9yf");

			ps_wr = conn.prepareStatement(sql_wr);
			rs_wr = ps_wr.executeQuery();
			while (rs_wr.next()) {
				statistics = new Statistics();
				statistics.setUsername(rs_wr.getString("username"));
				statistics.setGames_played(rs_wr.getInt("games_played"));
				statistics.setWins(rs_wr.getInt("wins"));
				statistics.setLosses(rs_wr.getInt("losses"));
				statistics.setTotal_kills(rs_wr.getInt("kills"));
				statistics.setTotal_deaths(rs_wr.getInt("deaths"));
				statistics.setWin_rate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs_wr != null) {
				try {
					rs_wr.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (ps_wr != null) {
				try {
					ps_wr.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

		}
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return statistics;
	}

	public Statistics selectTopK_D_R() {
		Statistics statistics = null;
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		Connection conn = null;
		PreparedStatement ps_kdr = null;

		ResultSet rs_kdr = null;

		String sql_kdr = "select * from statistics order by kill_death_ratio desc " + "limit 1;";

		try {
			conn = DriverManager.getConnection("jdbc:postgresql://localhost:5455/godavari", "godavari", "w33858a9yf");

			ps_kdr = conn.prepareStatement(sql_kdr);
			rs_kdr = ps_kdr.executeQuery();
			while (rs_kdr.next()) {
				statistics = new Statistics();
				statistics.setUsername(rs_kdr.getString("username"));
				statistics.setGames_played(rs_kdr.getInt("games_played"));
				statistics.setWins(rs_kdr.getInt("wins"));
				statistics.setLosses(rs_kdr.getInt("losses"));
				statistics.setTotal_kills(rs_kdr.getInt("kills"));
				statistics.setTotal_deaths(rs_kdr.getInt("deaths"));
				statistics.setKill_death_ratio();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs_kdr != null) {
				try {
					rs_kdr.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (ps_kdr != null) {
				try {
					ps_kdr.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return statistics;
	}
}