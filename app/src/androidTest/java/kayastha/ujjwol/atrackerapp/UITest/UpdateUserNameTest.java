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

import kayastha.ujjwol.atrackerapp.Profile;
import kayastha.ujjwol.atrackerapp.R;

import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class UpdateUserNameTest {

    String  upUsername;

    @Before
    public void setup() {
        upUsername = "UPDATED USERNAME";
    }

    @Rule
    public ActivityTestRule<Profile> activityTestRule = new ActivityTestRule<>(
            Profile.class
    );


    @Test
    public void testUsernameUpdate() {
        Intents.init();

        Espresso.onView(withId(R.id.btnUpdateDialog))
                .perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withId(R.id.updateUsernameEditText))
                .perform(ViewActions.typeText(String.valueOf(upUsername)), ViewActions.closeSoftKeyboard());

        Espresso.onView(withId(R.id.updateUsernameButton))
                .perform(ViewActions.click());

        Intents.release();
    }
}
