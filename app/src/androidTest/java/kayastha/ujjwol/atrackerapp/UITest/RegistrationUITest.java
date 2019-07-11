package kayastha.ujjwol.atrackerapp.UITest;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import kayastha.ujjwol.atrackerapp.R;
import kayastha.ujjwol.atrackerapp.SignUp;

import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;


@RunWith(AndroidJUnit4.class)
public class RegistrationUITest {

    String name, email, password, cpassword;

    @Before
    public void setup() {
        name = "hari";
        email = "hari@gmail.com";
        password = "test@123";
        cpassword = "test@123";
    }

    @Rule
    public ActivityTestRule<SignUp> activityTestRule = new ActivityTestRule<>(
            SignUp.class
    );


    @Test
    public void testRegistration() {
        Espresso.onView(ViewMatchers.withId(R.id.signup_name))
                .perform(ViewActions.typeText(String.valueOf(name)), ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.signup_email))
                .perform(ViewActions.typeText(String.valueOf(email)), ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.signup_password))
                .perform(ViewActions.typeText(String.valueOf(password)), ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.signup_confirmPassword))
                .perform(ViewActions.typeText(String.valueOf(cpassword)), ViewActions.closeSoftKeyboard());
        Espresso.onView(allOf(withText("Male"),
                withParent(withId(R.id.rg_gender))));

        Intents.init();
        Espresso.onView(withId(R.id.btn_signUp))
                .perform(ViewActions.click());
        Intents.release();

    }




}
