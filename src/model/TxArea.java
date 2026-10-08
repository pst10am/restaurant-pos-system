package model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TxArea {

	private int areaId = 0;
	private String areaName = "";
	private double areaDeliFee = 0;
	private int areaStatus = 0;
	
	private TxAreaVT[] vertices = null;
	
	// Constructor
	
	private TxArea() {}
	
	// Factory
	
	public static TxArea fromDb(ResultSet rs1) throws SQLException {
		TxArea obj1 = new TxArea();
		obj1.areaId = rs1.getInt("area_id");
		obj1.areaName = rs1.getString("area_name");
		obj1.areaDeliFee = rs1.getDouble("area_deli_fee");
		obj1.areaStatus = rs1.getInt("area_status");
		return obj1;
	}
	
	// Miscellaneous
	
	public double[] x_vertices() {
		double[] values = new double[vertices.length];
		for (int i=0; i < vertices.length; i++) {
			values[i] = vertices[i].getVx();
		}
		return values;
	}
	
	public double[] y_vertices() {
		double[] values = new double[vertices.length];
		for (int i=0; i < vertices.length; i++) {
			values[i] = vertices[i].getVy();
		}
		return values;
	}

	// Setter+Getter
	
	public int getAreaId() {
		return areaId;
	}

	public void setAreaId(int val) {
		this.areaId = val;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String val) {
		this.areaName = val;
	}

	public double getAreaDeliFee() {
		return areaDeliFee;
	}

	public void setAreaDeliFee(double val) {
		this.areaDeliFee = val;
	}

	public int getAreaStatus() {
		return areaStatus;
	}

	public void setAreaStatus(int val) {
		this.areaStatus = val;
	}

	public TxAreaVT[] getVertices() {
		return vertices;
	}
	
	public void setVertices(TxAreaVT[] val) {
		vertices = val;
	}
}
