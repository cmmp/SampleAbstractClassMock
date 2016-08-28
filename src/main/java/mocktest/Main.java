package mocktest;

public class Main {
	
	public static void main(String[] args) {
		SomeEndpoint se = new SomeEndpoint();
		String output = se.request("GET", "http://www.google.com", "index.html");
		System.out.println(output);
	}

}
