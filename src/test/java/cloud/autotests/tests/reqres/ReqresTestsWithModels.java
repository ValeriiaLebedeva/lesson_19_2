package cloud.autotests.tests.reqres;

import cloud.autotests.tests.reqres.lombok.CreateUserRequest;
import cloud.autotests.tests.reqres.lombok.CreateUserResponse;
import cloud.autotests.tests.reqres.lombok.RegisterUser;
import cloud.autotests.tests.reqres.lombok.SuccessfulRegisteredUser;
import io.qameta.allure.restassured.AllureRestAssured;
import org.junit.jupiter.api.Test;

import static cloud.autotests.filters.CustomLogFilter.customLogFilter;
import static cloud.autotests.tests.reqres.Specs.request;
import static cloud.autotests.tests.reqres.Specs.responseSpec;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class ReqresTestsWithModels {

    @Test
    void singleUserNotFound() {
        given()
                .filter(new AllureRestAssured())
                .when()
                .log().all()
                .get("https://reqres.in/api/users/23")
                .then()
                .log().all()
                .statusCode(404);
    }

    @Test
    void listResourceWithSpecWithGroovy() {
        given()
                .filter(customLogFilter().withCustomTemplates())
                .spec(request)
                .when()
                .get("/unknown")
                .then()
                .spec(responseSpec)
                .log().all()
                .body("data.findAll{it.year>2002}.year.flatten()",
                        hasItem(2003));
    }

    @Test
    void createUser() {

        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setName("morpheus");
        createUserRequest.setJob("leader");

        CreateUserResponse response = given()
                .filter(customLogFilter().withCustomTemplates())
                .contentType(JSON)
                .body(createUserRequest)
                .when()
                .log().all()
                .post("https://reqres.in/api/users")
                .then()
                .log().all()
                .statusCode(201)
                .extract().as(CreateUserResponse.class);

        assertEquals(createUserRequest.getName(), response.getName());
        assertEquals(createUserRequest.getJob(), response.getJob());
    }

    @Test
    void registerSuccessfulWithSpec() {

        RegisterUser registerUser = new RegisterUser();
        registerUser.setEmail("eve.holt@reqres.in");
        registerUser.setPassword("pistol");

        SuccessfulRegisteredUser successfulRegisteredUser =
        given()
                .filter(customLogFilter().withCustomTemplates())
                .spec(request)
                .body(registerUser)
                .when()
                .post("/register")
                .then()
                .spec(responseSpec)
                .log().all()
                .extract().as(SuccessfulRegisteredUser.class);

        assertEquals(successfulRegisteredUser.getId(),4);
        assertEquals(successfulRegisteredUser.getToken(),"QpwL5tke4Pnpja7X4");
    }

    @Test
    void registerUnsuccessful() {

        RegisterUser registerUserWithoutPass = new RegisterUser();
        registerUserWithoutPass.setEmail("eve.holt@reqres.in");

        given()
                .filter(customLogFilter().withCustomTemplates())
                .contentType(JSON)
                .body(registerUserWithoutPass)
                .when()
                .log().all()
                .post("https://reqres.in/api/register")
                .then()
                .log().all()
                .statusCode(400)
                .body("error", is("Missing password"));
    }
}