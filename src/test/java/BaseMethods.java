import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.example.api.User;
import org.example.api.UserLogin;
import org.example.api.UserRegistration;
import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.WebDriver;
import java.time.Duration;

import static org.example.browser.BrowserTypeSetup.browserDriverSetUp;


public class BaseMethods {

    public static final String MAIN_PAGE = "https://stellarburgers.nomoreparties.site/";
    private final String email = "Mike@gogo.pwr";
    private final String password = "gogopwr";
    private final String name = "Mike";
    private final String incorrectPassword = "1234";



    private WebDriver driver;
    private final User user = new User();
    private final UserRegistration userRegistration = new UserRegistration(email, password, name);
    private final UserLogin userLogin = new UserLogin(email, password);
    private final UserLogin loginIncorrectUser = new UserLogin(email, incorrectPassword);


    public void setDriver(){
        driver = browserDriverSetUp();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get(MAIN_PAGE);
        driver.manage().window().maximize();
    }


    @Before
    public void setUp() {
        RestAssured.baseURI = MAIN_PAGE;
        setDriver();
    }

    @After
    public void cleanUp(){
        Response response = getUser().loginUser(getUserLogin());
        if(response.jsonPath().get("success").equals(true)){
            getUser().logoutUser(response.jsonPath().get("accessToken"));
            getUser().deleteUser(response.jsonPath().get("accessToken"));
        }
        response = getUser().loginUser(getLoginIncorrectUser());
        if(response.jsonPath().get("success").equals(true)){
            getUser().logoutUser(response.jsonPath().get("accessToken"));
            getUser().deleteUser(response.jsonPath().get("accessToken"));
        }
        driver.quit();
    }

    public WebDriver getDriver() {
        return driver;
    }

    public User getUser() {
        return user;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public UserLogin getUserLogin() {
        return userLogin;
    }

    public UserRegistration getUserRegistration() {
        return userRegistration;
    }

    public String getIncorrectPassword() { return incorrectPassword; }

    public UserLogin getLoginIncorrectUser() { return loginIncorrectUser; }

}
