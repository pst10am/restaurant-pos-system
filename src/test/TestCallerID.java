package test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import resrc.CIDRecord;

public class TestCallerID {
	
	TestCallerID() {
		PortListener plst = new PortListener();
		plst.start();
	}
	
	class PortListener extends Thread {
		public void run() {
			System.out.println("thread started ... ");
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
					System.out.println("wait ... ");
					socket.receive(packet);
					System.out.println("run ... ");
					if (new String(buffer).equals(new String(lastBuffer))) {
						continue; 
					} else {
						lastBuffer = buffer.clone();
					}
					cid1.importCallRecord(new String(buffer));
					System.out.printf(" > received -> [%s]\n"
							+ " > duration:[%d] name:[%s] phone:[%s]\n", cid1.simpleResult, cid1.duration, cid1.name, cid1.phone);
				}
			} catch (SocketException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("thread ended ... ");
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new TestCallerID();
	}
}
