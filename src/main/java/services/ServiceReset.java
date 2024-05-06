package services;

import entities.Reset;
import utils.MyDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ServiceReset {

	private final Connection con;

	public ServiceReset() {
		this.con = MyDatabase.getInstance().getConnection();
	}

	public boolean ajout(Reset t) {

		try {
			String req = "SELECT * from utilisateur where email=?";
			PreparedStatement pst = con.prepareStatement(req);
			pst.setString(1, t.getEmail());
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				String reqs = "INSERT INTO reset(email,code,timeMils)VALUES(?,?,?)";
				PreparedStatement psts = con.prepareStatement(reqs);
				psts.setString(1, t.getEmail());
				psts.setInt(2, t.getCode());
				psts.setString(3, t.getTimeMils());
				psts.executeUpdate();
				return true;
			}
			else {
				return false;
			}

		} catch (SQLException e) {

			System.out.println(e.getMessage());
		}
		return true;
	}


}
