package model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TxAreaVT {

	private int areaId = 0;
	private int vseq = 0;
	private double vx = 0;
	private double vy = 0;
	
	// Constructor
	
	private TxAreaVT() {}
	
	// Factory
	
	public static TxAreaVT fromDb(ResultSet rs1) throws SQLException {
		TxAreaVT obj1 = new TxAreaVT();
		obj1.areaId = rs1.getInt("area_id");
		obj1.vseq = rs1.getInt("vseq");
		obj1.vx = rs1.getDouble("vx");
		obj1.vy = rs1.getDouble("vy");
		return obj1;
	}
	
	// Miscellaneous
	
	// Setter+Getter

	public int getAreaId() {
		return areaId;
	}
	public void setAreaId(int val) {
		this.areaId = val;
	}

	public int getVseq() {
		return vseq;
	}
	public void setVseq(int val) {
		this.vseq = val;
	}

	public double getVx() {
		return vx;
	}
	public void setVx(double val) {
		this.vx = val;
	}

	public double getVy() {
		return vy;
	}
	public void setVy(double val) {
		this.vy = val;
	}
}
