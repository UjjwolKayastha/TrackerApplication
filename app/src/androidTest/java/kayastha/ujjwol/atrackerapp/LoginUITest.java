package kayastha.ujjwol.atrackerapp;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class LoginUITest {

    String  email, password;

    @Before
    public void setup() {
        email = "kayastha@gmail.com";
        password = "test@123";
    }

    @Rule
    public ActivityTestRule<SignIn> activityTestRule = new ActivityTestRule<>(
            SignIn.class
    );


    @Test
    public void testLogin() {
        Espresso.onView(withId(R.id.signin_email))
                .perform(ViewActions.typeText(String.valueOf(email)), ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.signin_password))
                .perform(ViewActions.typeText(String.valueOf(password)), ViewActions.closeSoftKeyboard());

        Intents.init();
        Espresso.onView(withId(R.id.btn_signIn))
                .perform(ViewActions.click());
        Intents.release();
    }



}
