
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
import java.io.*;
import java.math.BigInteger;  

class ChangUserRegistration{  
	
	public int Di, PW, Rd, T, PID, PIN;
	public String com1;
	
	 public static double acosh(double x)
	 {
	 return Math.log(x + Math.sqrt(x*x - 1.0));
	 }
	 
	 public static double chebyshev(double x, int z, int n) {
		return Math.cosh(n*acosh(x)%z);
	 }
	 
	 public static String XOREncode(String st, String key) {
		 StringBuilder sb = new StringBuilder();
			for(int i = 0; i < key.length(); i++)
			    sb.append((char)(st.charAt(i) ^ key.charAt(i)));
			String str = sb.toString();
			str = str + st.substring(key.length());
			//System.out.println(st.substring(key.length()));
			return str;
	 }
	 
	 public static String XORDecodekey(String st, String key) {
		 StringBuilder sb = new StringBuilder();
			for(int i = 0; i < key.length(); i++)
			    sb.append((char)(st.charAt(i) ^ key.charAt(i)));
			String str = sb.toString();
			return str;
	 }
	 
	 public static String XORDecodeString(String st, String key) {
		 StringBuilder sb = new StringBuilder();
			for(int i = 0; i < key.length(); i++)
			    sb.append((char)(st.charAt(i) ^ key.charAt(i)));
			String str = sb.toString();
			str = str + st.substring(key.length());
			return str;
	 }
	 
	 public static String getSha256(String str) {
		 MessageDigest digest;
		 String encoded = null;
			try {
				digest = MessageDigest.getInstance("SHA-256");
				byte[] hash = digest.digest(str.getBytes(StandardCharsets.UTF_8));
				encoded = Base64.getEncoder().encodeToString(hash);
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return encoded;
	 } 
	
	public static void main(String args[])throws Exception{  
		
		
		final String HOST = "127.0.0.1";
        final int PORT = 4082;
        
        int UIDi = 111;
        int UPWDi = 12345;
        
       // String BIOi = "00:00:5e:00:53:af";
        
        Socket socket = new Socket(HOST, PORT);
		DataInputStream din=new DataInputStream(socket.getInputStream());  
		DataOutputStream dout=new DataOutputStream(socket.getOutputStream());  
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));  

		String input="";  
		while(!input.equals("stop")){  
			input=br.readLine();  
			
			if (input.equalsIgnoreCase("stop")) {
				dout.writeUTF(input);
				dout.flush();
				System.out.println("---->>> connection aborted.......");
            	break;
            } 
////////////////////////////////////User Registration /////////////////////////////////
			else if (input.equals("u")){
				dout.writeUTF("u"); // send option
				dout.flush();
				
            	Random rnd = new SecureRandom();
    			int ri = BigInteger.probablePrime(15, rnd).intValue();
    			String MPi = getSha256(""+ri+UPWDi);
    			String mUreg = ""+UIDi+"<-->"+MPi;
    			
            	dout.writeUTF(mUreg); // send
				dout.flush();
					
				String received_GW = din.readUTF(); // MIi+"<-->"+ei;
				
				//String received[] = received_GW.split("<-->"); // Mpi, IDi
				//String MIi = received[0];
				//String ei = received[1];
				
				String store = received_GW+"<-->"+ri;
				
				System.out.println("SC: "+store);
				
				Writer output;
	    		output = new BufferedWriter(new FileWriter("SC.txt"));  //clears file every time
	    		output.append(store);
	    		output.close(); 
				/*File file = new File("SC.txt");
				FileWriter fileWriter = new FileWriter(file,true);
				fileWriter.write("\r\n");*/
	    		System.out.println("user registration completed.");
	    		
				/*
				 * String content = new Scanner(new File("SC.txt")).useDelimiter("\\Z").next();
				 * System.out.println("\n----> "+content);
				 * 
				 * String recvd[] = content.split("-1-");
				 * System.out.println("\n----> length: "+recvd.length);
				 */
            } 
//////////////////////////////////// Sensor Registration /////////////////////////////////
            else if(input.equals("s")) { 
            	dout.writeUTF("s"); // send option
				dout.flush();
            	int SIDj = 2222;
            	//int XGWN = 4321;
            	
            	
    			dout.writeUTF(""+SIDj); // send
				dout.flush();

				String fi = din.readUTF(); // receive
				String storeSensor = ""+SIDj+"<-->"+fi; 
							
            	Writer output;
	    		output = new BufferedWriter(new FileWriter("mem.txt"));  //clears file every time
	    		output.append(storeSensor);
	    		output.close();
	    		System.out.println("Sensor registration completed.");
    			
            } else {
            	dout.writeUTF(""); // send option
				dout.flush();
            	System.out.println("type 'u' for user registration then hit enter");
            	System.out.println("type 's' for sensor registration then hit enter");
            }
		}

		dout.close();  
		socket.close();
	
	}
} 