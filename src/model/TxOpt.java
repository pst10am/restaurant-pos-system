package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import refx.OptionType;
import resrc.ResData;

public class TxOpt implements Comparable<TxOpt> {

	private int optId;
	private int optSeq;
	private String optNamePos;
	private String optNameWeb;
	private OptionType optType;
	private boolean optMand;
	private String optComment;
	private int optStatus;
	private java.util.Vector<TxOptItem> items = null;
	{
		optId = 0;
		optSeq = 0;
		optNamePos = "";
		optNameWeb = "";
		optType = OptionType.Single;
		optMand = false;
		optComment = "";
		optStatus = 1;
	}
	
	// Constructor
	
	private TxOpt() {}
	
	private TxOpt(ResultSet rs1) throws SQLException {
		optId = rs1.getInt("opt_id");
		optSeq = rs1.getInt("opt_seq");
		optNamePos = rs1.getString("opt_name_pos");
		optNameWeb = rs1.getString("opt_name_web");
		optType = OptionType.valueOf(rs1.getString("opt_type"));
		optMand = rs1.getBoolean("opt_mand");
		optComment = rs1.getString("opt_comment");
		optStatus = rs1.getInt("opt_status");
	}
	
	// Factory
	
	public static TxOpt newInstance() {
		return new TxOpt();
	}
	
	public static TxOpt fromDb(ResultSet rs1) throws SQLException {
		return new TxOpt(rs1);
	}
	
	// Miscellaneous

	@Override
	public int compareTo(TxOpt obj) {
		return this.optSeq - obj.optSeq;
	}
	
	public java.util.Vector<TxOptItem> getOptItems() {
		return items;
	}
	
	public void setOptItems(java.util.Vector<TxOptItem> _items) {
		items = _items;
	}
	
	public String toString() {
		return String.format("%s%s%s", 
			optStatus==1?"":"[x] ",
			optNamePos, 
			optComment.isEmpty() ? "": String.format(" [%s]", optComment));
	}
	
	public boolean hasItems() {
		return null != items && items.size() > 0;
	}
	
	// Setter+Getter

	public int getOptId() {
		return optId;
	}

	public void setOptId(int optId) {
		this.optId = optId;
	}

	public int getOptSeq() {
		return optSeq;
	}

	public void setOptSeq(int optSeq) {
		this.optSeq = optSeq;
	}

	public String getOptNamePos() {
		return optNamePos;
	}

	public void setOptNamePos(String optName1) {
		this.optNamePos = optName1;
	}

	public String getOptNameWeb() {
		return optNameWeb;
	}

	public void setOptNameWeb(String optName2) {
		this.optNameWeb = optName2;
	}

	public OptionType getOptType() {
		return optType;
	}

	public void setOptType(OptionType optType) {
		this.optType = optType;
	}

	public boolean isMandatory() {
		return optMand;
	}

	public void setOptMand(boolean optMand) {
		this.optMand = optMand;
	}

	public String getOptComment() {
		return optComment;
	}

	public void setOptComment(String optComment) {
		this.optComment = optComment;
	}

	public int getOptStatus() {
		return optStatus;
	}

	public void setOptStatus(int optStatus) {
		this.optStatus = optStatus;
	}

	public static Vector<TxOpt> getOptsByItmId(int itmId) throws SQLException {
		return ResData.getOptsByItmId(itmId);
	}

	public static Vector<TxOpt> getOptionsWithDelete() throws SQLException {
		return ResData.getOptions(true);
	}

	private static final String html1 = "<html><table cellspacing=0 cellpadding=3>"+
		"<tr><td align=right>Seq</td><td>%d</td></tr>"+
		"<tr><td align=right>Name</td><td><nobr><b>%s</b> [%s]</nobr></td></tr>"+
		"<tr><td align=right>Type</td><td>%s</td></tr>"+
		"<tr><td align=right>Mandatory</td><td>%s</td></tr>"+
		"<tr><td align=right>Comment</td><td><i>%s</i></td></tr>"+
		"</table>"+
		"</html>";
	private static final String htmlOpi = "<html><table cellspacing=0 cellpadding=3>%s</table></html>";
	private static final String htmlOpiRow = "<tr>"
			+ "<td>%s</td>"
			+ "<td align=right>%d</td>"
			+ "<td><nobr><b>%s</b> [%s]</nobr></td>"
			+ "<td align=right><b>$%.2f</b></td>"
			+ "</tr>";

	public String getHtmlStr() {
		return String.format(html1, 
			this.optSeq,
			this.optNamePos,
			this.optNameWeb,
			this.optType,
			this.optMand?"<font color='red'>Yes</font>":"No",
			this.optComment.isEmpty()?"-":this.optComment);
	}

	public String getHtmlOpiStr() {
		StringBuilder bld1 = new StringBuilder();
		for (TxOptItem opi1 : items) {
			bld1.append(String.format(htmlOpiRow,
				opi1.isOpiDefault()?"[d]":"",
				opi1.getOpiSeq(),
				opi1.getOpiNamePos(),
				opi1.getOpiNameWeb(),
				opi1.getOpiPrice()));
		}
		return String.format(htmlOpi, bld1.toString());
	}

	public void save() throws SQLException {
		ResData.saveTxOpt(this);
	}

	public void delete() throws SQLException {
		ResData.deleteTxOpt(this.optId, this.optStatus==1?2:1);
	}

	public void addOpi(TxOptItem opi1) {
		if (null == items) {
			items = new java.util.Vector<>();
		}
		items.add(opi1);
	}
}
