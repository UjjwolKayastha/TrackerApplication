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
import kayastha.ujjwol.atrackerapp.message.Message;

import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class SendMessageUITest {
    String  message;

    @Before
    public void setup() {
        message = "Hello from the other side";
    }

    @Rule
    public ActivityTestRule<Message> activityTestRule = new ActivityTestRule<>(
            Message.class
    );


    @Test
    public void testMEssage() {
        Espresso.onView(ViewMatchers.withId(R.id.emojicon_edit_text))
                .perform(ViewActions.typeText(String.valueOf(message)), ViewActions.closeSoftKeyboard());
          Intents.init();

        Espresso.onView(withId(R.id.submit_button))
                .perform(ViewActions.click());
        Intents.release();
    }
}
