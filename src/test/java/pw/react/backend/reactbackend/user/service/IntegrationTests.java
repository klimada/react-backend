package pw.react.backend.reactbackend.user.service;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.junit4.SpringRunner;
import pw.react.backend.reactbackend.user.User;
import pw.react.backend.reactbackend.user.UserRepository;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.spy;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationTests {

    private UserService service;

    @Autowired
    private UserRepository repository;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate myrest = new TestRestTemplate();

    private HttpHeaders header ;


    @Before
    public void setUp() throws Exception {
        service = spy(new UserService(repository));
        myrest.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        header = new HttpHeaders();
        header.add("admin", "password");
        header.setContentType(MediaType.APPLICATION_JSON);
        User a = new User("a","Ada", "Klimczak",LocalDate.of(1998,1,20));
        User w = new User("w","Wiola", "Kucharska",  LocalDate.of(2000,3,4));
        repository.save(a);
        repository.save(w);
    }

    private String getport() {
        return "http://localhost:" + port ;
    }

    @Test
    public void GetUserByLoginTest() {
        HttpEntity httpEntity = new HttpEntity(header);

        ResponseEntity<String> re1 = myrest.exchange(
                getport()+"/users/login/a",
                HttpMethod.GET,
                httpEntity,
                String.class);

        ResponseEntity<String> re2 = myrest.exchange(
                getport()+"/users/login/xxx",
                HttpMethod.GET,
                httpEntity,
                String.class);

        String response1 = "User with login: a exists in database";

        assertThat(re2.getStatusCode() == HttpStatus.NOT_FOUND);
        assertThat(re1.getBody().compareTo(response1) == 0);
    }

    @Test
    public void DeleteUserTest()
    {
        HttpEntity httpEntity = new HttpEntity(header);

        ResponseEntity<String> re1 = myrest.exchange(
                getport()+"/users/login/a",
                HttpMethod.DELETE,
                httpEntity,
                String.class);

        ResponseEntity<String> re2 = myrest.exchange(
                getport() + "/users/login/xxx",
                HttpMethod.DELETE,
                httpEntity,
                String.class);

        String response1 = "User a has been deleted";
        String response2 = "User with login xxx does not exist";

        assertThat(response1.compareTo(re1.getBody()) == 0);
        assertThat(re2.getStatusCode() == HttpStatus.NOT_FOUND);
    }

    @Test
    public void CreateUserTest() throws JSONException
    {
        User c = new User("c","Carl", "Man",  LocalDate.of(2000,3,4));
        HttpEntity<User> httpEntity = new HttpEntity<>(c, header);

        ResponseEntity<String> re1 = myrest.exchange(
                getport()+"/users",
                HttpMethod.POST,
                httpEntity,
                String.class);

        String expected = "{" +
                        "\"login\": \"c\"," +
                        "\"firstName\": \"Carl\"," +
                        "\"lastName\": \"Man\"," +
                        "\"birthDate\": \"2000-03-04\""+
                        "}";

        JSONAssert.assertEquals(expected, re1.getBody(), JSONCompareMode.LENIENT);
    }

    @Test
    public void UpdateUserTest() throws JSONException {
        List<User> user = repository.findAllByLogin("a");
        user.get(0).setFirstName("Francis");
        user.get(0).setLastName("Dunn");
        HttpEntity<User> httpEntity = new HttpEntity<>(user.get(0), header);

        ResponseEntity<String> updatedUserResponse = myrest.exchange(
                getport()+"/users",
                HttpMethod.PUT,
                httpEntity,
                String.class);

        String expected = "{" +
                        "\"login\": \"a\"," +
                        "\"firstName\": \"Francis\"," +
                        "\"lastName\": \"Dunn\"," +
                        "\"birthDate\": \"1998-01-20\""+
                        "}";

        JSONAssert.assertEquals(expected, updatedUserResponse.getBody(), JSONCompareMode.LENIENT);
    }


}

