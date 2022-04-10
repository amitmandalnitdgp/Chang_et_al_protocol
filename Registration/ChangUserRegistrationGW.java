import java.net.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;
import java.util.Scanner;
import java.io.*;
import java.math.BigInteger;  

class ChangUserRegistrationGW{  
	
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
        final int PORT = 4082;
        int XGWN = 4321;
        
        ServerSocket serverSocket = new ServerSocket(PORT);
        Socket clientSocket = serverSocket.accept();
        DataInputStream din=new DataInputStream(clientSocket.getInputStream());  
		DataOutputStream dout=new DataOutputStream(clientSocket.getOutputStream());  
		
		String input="",str2="";  
		while(!input.equals("stop")){  
			
			input=din.readUTF(); // receive option
			System.out.println("input: "+input);
			
			if (input.equalsIgnoreCase("stop")) {
            	serverSocket.close();
            	System.out.println("---->>> connection aborted.......");
                break;
            } 
//////////////////////////////////User Registration /////////////////////////////////			
			else if (input.equals("u")){
				input=din.readUTF(); // receive data
            	String received[] = input.split("<-->"); // Mpi, IDi
    			String IDi = received[0];
    			String MPi = received[1];
    			
    			Random rnd = new SecureRandom();
    			int rip = BigInteger.probablePrime(15, rnd).intValue();
    			String MIi =  getSha256(""+rip+IDi);
    			String fi = getSha256(MIi+XGWN);
    			String ei = XOREncode(MPi, fi);
    			String SC = MIi+"<-->"+ei;
    			
            	dout.writeUTF(SC); //send
				dout.flush();
				
				Writer output;
	    		output = new BufferedWriter(new FileWriter("GWmemUi.txt"));  //clears file every time
	    		output.append(SC);
	    		output.close();
	    		System.out.println("fi: "+ fi);
	    		System.out.println("user registration completed.");
            } 
//////////////////////////////////Sensor Registration /////////////////////////////////			
			else if(input.equals("s")) {
				input=din.readUTF(); // receive data
				
            	String fi = getSha256(input+XGWN);           	

            	dout.writeUTF(fi); //send
				dout.flush();
				
				String storeGW = input+"<-->"+fi;
				
				Writer output;
	    		output = new BufferedWriter(new FileWriter("GWmemSj.txt"));  //clears file every time
	    		output.append(storeGW);
	    		output.close();
				System.out.println("Sensor registration completed.");
            	
            } else {
            	System.out.println("type 'u' for user registration then hit enter");
            	System.out.println("type 's' for sensor registration then hit enter");
            }
		}
		din.close();  
		clientSocket.close();  
		serverSocket.close();

	}
}  