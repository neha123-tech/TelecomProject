package telecomAssignments;


import static io.restassured.RestAssured.given;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.response.Response;

public class telecomAssignements {
	
	
	String tokenValue;
	String loginToken;
	String userEmail;
	String id;
	
	@Test(priority=1)
	public void addNewUser() {
		
		userEmail="neha"+System.currentTimeMillis()+"@yopmail.com";
		
		Response res =given().header("Content-Type","application/json")
		.body("{\n"
				+ "\"firstName\": \"digi\",\n"
				+ "\"lastName\": \"QA\",\n"
				+ "\"email\": \"" + userEmail + "\",\n"
				+ "\"password\": \"myPassword\"\n"
				+ "}")
		.when().post("https://thinking-tester-contact-list.herokuapp.com/users");
		res.then().log().body();
		
		tokenValue = res.jsonPath().getString("token");
		System.out.println("UserToken "+ tokenValue);
		Assert.assertEquals(res.getStatusCode(), 201, "Expected status code 201 for add user");
		System.out.println("New user created with status code "+res.statusCode());
		System.out.println("Add new user Status line is "+ res.getStatusLine());

		
	}
	@Test(priority=2,dependsOnMethods="addNewUser")
	public void getUserProfile() {
		Response res =		given().header("Content-Type","application/json")
					.header("Authorization","Bearer "+tokenValue)
			.when().get(" https://thinking-tester-contact-list.herokuapp.com/users/me");
		
		res.then().log().body();
		System.out.println("Get user profile status code "+res.statusCode());
		System.out.println("Get user profile status line is "+ res.getStatusLine());
	}

	@Test(priority=3,dependsOnMethods="addNewUser")
	public void updateUser() {
		Response res = given().header("Content-Type","application/json")
		.header("Authorization","Bearer "+tokenValue).body("{\n"
				+ "\"firstName\": \"Updated\",\n" 
				+ "\"lastName\": \"Username\",\n"
				+ "\"email\": \""+ userEmail +"\",\n"
				+ "\"password\": \"myNewPassword\"\n"
				+ "}")
		.when().patch("https://thinking-tester-contact-list.herokuapp.com/users/me");
		
		res.then().log().body();
		Assert.assertEquals(res.getStatusCode(), 200, "Expected status code 200 for update User");
		System.out.println("Update user profile status code "+res.statusCode());
		System.out.println("Update user profile status line is "+ res.getStatusLine());

	}
	
	@Test(priority=4,dependsOnMethods={"addNewUser","updateUser"})
	public void logInUser() {
		
		Response res = given().header("Content-Type","application/json")
		.body("{\n"+
			"\"email\": \"" + userEmail +"\",\n"+
			"\"password\": \"myNewPassword\"\n"+
			"}").when().post("https://thinking-tester-contact-list.herokuapp.com/users/login");
		res.then().log().body();
		System.out.println("New user created with status code "+res.statusCode());
		loginToken = res.jsonPath().getString("token");
		System.out.println("loginToken "+ loginToken);
		Assert.assertEquals(res.getStatusCode(), 200, "Expected status code 200 for login");
		System.out.println("User login status code "+res.statusCode());
		System.out.println("User login status line is "+ res.getStatusLine());
		
	}
	@Test(priority =5,dependsOnMethods={"logInUser"})
	public void addContact() {
		Response res =given().header("Content-Type","application/json").header("Authorization","Bearer "+loginToken)
		.body("{\r\n"
				+ "\"firstName\": \"John\",\r\n"
				+ "\"lastName\": \"Doe\",\r\n"
				+ "\"birthdate\": \"1970-01-01\", \"email\": \"jdoe@fake.com\", \"phone\": \"8005555555\",\r\n"
				+ "\"street1\": \"1 Main St.\", \"street2\": \"Apartment A\", \"city\": \"Anytown\",\r\n"
				+ "\"stateProvince\": \"KS\", \"postalCode\": \"12345\", \"country\": \"USA\"\r\n"
				+ "}").when().post("https://thinking-tester-contact-list.herokuapp.com/contacts");
		res.then().log().body();
		  id=res.jsonPath().getString("_id");

		System.out.println("Add contact status code is "+ res.getStatusCode());
		System.out.println("Add contact status line is "+ res.getStatusLine());

		Assert.assertEquals(res.getStatusCode(), 201, "Expected status code 201 for add contact");
		
	}
	
	@Test(priority =6,dependsOnMethods={"logInUser","addContact"})
	public void getContactList() {
		Response res = given().header("Content-Type","application/json").header("Authorization","Bearer "+loginToken)
				.when().get("https://thinking-tester-contact-list.herokuapp.com/contacts");
				res.then().log().body();
				Assert.assertEquals(res.getStatusCode(), 200, "Expected status code 200 for contact list");
				System.out.println("Get contact list status code is "+res.getStatusCode());
				System.out.println("Get contact list status line is "+res.getStatusLine());
				

	}
	
	@Test(priority =7,dependsOnMethods={"logInUser","addContact"})
	public void getContact() {
	Response res = given().header("Content-Type","application/json").header("Authorization","Bearer "+loginToken)
	.when().get("https://thinking-tester-contact-list.herokuapp.com/contacts");
	res.then().log().body();
	Assert.assertEquals(res.getStatusCode(), 200, "Expected status code 200 for get contact");
	System.out.println("Get contact status code is "+res.getStatusCode());
	System.out.println("Get contact status line is "+res.getStatusLine());
	
	}
	
	@Test(priority =8,dependsOnMethods={"logInUser","addContact"})
	  public void updateContact()
	  {
		  Response res=given()
		  .header("Content-Type","application/json")
		  .header("Accept","application/json")
		  .header("Authorization","Bearer "+loginToken)
		  
		  .body("{ \n"
		  		+ "\"firstName\": \"suku\", \n"
		  		+ "\"lastName\": \"roghe\",\n"
		  		+ " \n"
		  		+ " \n"
		  		+ "\"birthdate\": \"1992-02-02\", \n"
		  		+ "\"email\": \"newsuku@gmail.com\", \n"
		  		+ "\"phone\": \"8005554242\", \n"
		  		+ "\"street1\": \"13 School St.\", \n"
		  		+ "\"street2\": \"Apt. 5\", \n"
		  		+ "\"city\": \"Washington\", \n"
		  		+ "\"stateProvince\": \"QC\", \n"
		  		+ "\"postalCode\": \"A1A1A1\", \n"
		  		+ "\"country\": \"Canada\" \n"
		  		+ "} ")
		  
		  
		  
		  .when().put("https://thinking-tester-contact-list.herokuapp.com/contacts/"+id);
		  
		  res.then().log().body();
		  System.out.println("User updated with code: "+res.statusCode());
	  }
	
	@Test(priority =9,dependsOnMethods={"logInUser","addContact"})
	  public void updateContactpatch()
	  {
		  Response res=given()
				  .header("Content-Type","application/json")
				  .header("Accept","application/json")
				  .header("Authorization","Bearer "+loginToken)
				  .body("{ \n"
				  		+ "\"firstName\": \"Amy\", \n"
				  		+ "\"lastName\": \"Miller\"} ")
				  .when().put("https://thinking-tester-contact-list.herokuapp.com/contacts/"+id);
		  
		  res.then().log().body();
		  System.out.println("User updated with code: "+res.statusCode());
	
		Assert.assertEquals(res.getStatusCode(), 200, "Expected status code 200 for update contact using PATCH");
		System.out.println("Partial update contact status code is "+res.getStatusCode());
		System.out.println("Partial update status line is "+res.getStatusLine());
		
}
	
	@Test(priority =10,dependsOnMethods={"logInUser","addContact"})
	public void logoutUser() {
		Response res = given().header("Content-Type","application/json").header("Authorization","Bearer "+loginToken)
				.when().post("https://thinking-tester-contact-list.herokuapp.com/users/logout");
	
	res.then().log().body();
	
	Assert.assertEquals(res.getStatusCode(), 200, "Expected status code 200 for user logout");
	System.out.println("User logout status code is "+res.getStatusCode());
	System.out.println("User logout status line is "+res.getStatusLine());
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}