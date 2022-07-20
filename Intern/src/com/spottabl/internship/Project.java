package com.spottabl.internship;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class Project {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/spottabl","root","qwer1234");

		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery("SELECT clientcode, SUM(Number_of_users_invited_from_spottabl) AS Number_of_users_invited_from_spottabl,\r\n"
				+ "SUM(Number_of_users_accepted_invite) AS Number_of_users_accepted_invite,SUM(Number_of_users_on_spottabl) AS Number_of_users_on_spottabl\r\n"
				+ " FROM \r\n"
				+ "((SELECT email,clientcode, \r\n"
				+ "       (CASE WHEN inviter LIKE '%spottabl%' THEN 1 ELSE 0 END) AS Number_of_users_invited_from_spottabl, \r\n"
				+ "       (CASE WHEN accepted = 1 THEN 1 ELSE 0 END) AS Number_of_users_accepted_invite\r\n"
				+ "       FROM clientuserinvites) cc\r\n"
				+ "LEFT OUTER JOIN \r\n"
				+ "(SELECT  email,(CASE WHEN email LIKE '%flexmoney%' OR email LIKE '%spottabl%' THEN 1 ELSE 0 END) AS Number_of_users_on_spottabl FROM registrations WHERE email LIKE '%flexmoney%' OR email LIKE '%spottabl%') rr\r\n"
				+ "ON  cc.email = rr.email ) GROUP BY clientcode  ");

		ResultSetMetaData metadata = rs.getMetaData();
		List<JSONObject> objectList = new ArrayList<>();
		System.out.println(metadata.getColumnCount());
		while(rs.next()) {
			JSONObject object = new JSONObject();
		for(int i=1 ; i<=metadata.getColumnCount(); i++) {			
			String colName = metadata.getColumnName(i);			
				String data = rs.getObject(i).toString();
				object.put(colName, data);
			}
		objectList.add(object);
		}
		System.out.println(objectList);
		st.close();
		con.close();
	}
}
