package resrc;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import model.CallInfo;

public class CallerID extends Thread {
	
	private static CallerID actObj = null;
	private java.util.HashMap<Integer, CallInfo> calls;
	
	private CallerID() {
		calls = new java.util.HashMap<>();
	}
	
	public static CallerID getInstance() {
		if (null == actObj) {
			actObj = new CallerID();
		}
		return actObj;
	}
	
	public static CallInfo getCIDRecord(int line) {
		if (null == actObj) {
			System.out.println("CID did not init.");
			return null;
		}
		if (!actObj.calls.containsKey(line)) {
			return null;
		}
		return actObj.calls.get(line);
	}

	public void run() {
		System.out.println("> callerid has started.");
		CIDRecord cid1 = new CIDRecord("");
		try (
			DatagramSocket socket = new DatagramSocket(3520);
		) {
			socket.setReuseAddress(true);
			
			byte[] buffer = new byte[1024];
			byte[] lastBuffer = new byte[1024];
			
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			while (true)
			{	
				buffer = new byte[1024];
				packet = new DatagramPacket(buffer, buffer.length);
				System.out.println("> callerid ready .. ");
				socket.receive(packet);
				if (new String(buffer).equals(new String(lastBuffer))) {
					continue; 
				} else {
					lastBuffer = buffer.clone();
				}
				cid1.importCallRecord(new String(buffer));
				System.out.printf("> line:[%d] name:[%s] phone:[%s]\n", 
					//cid1.simpleResult, 
					cid1.line, cid1.name, cid1.phone);
				if (cid1.inbound) {
					if (calls.containsKey(cid1.line)) {
						CallInfo cif = calls.get(cid1.line);
						cif.update(cid1);
					} else {
						calls.put(cid1.line, new CallInfo(cid1));
					}
				}
				System.out.println();
			}
		} catch (SocketException e) {
			e.printStackTrace();
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
		System.out.println("> callerid has ended.");
	}

}
