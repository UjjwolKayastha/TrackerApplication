package kayastha.ujjwol.atrackerapp.UnitTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import kayastha.ujjwol.atrackerapp.models.UserData;
import kayastha.ujjwol.atrackerapp.utilities.Firebase_method;

public class SendUserDataUnitTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void sendUserData() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Negative value not allowed");
        Firebase_method t = new Firebase_method();

//        this will throw a NPE because, this function needs firebase Uid to store the data in database
        t.create_new_userData("Gorkha","gorkha@gmail.com", "test@123", "Male","123456");
    }

    @Test
    public void searchEmailCheck() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Negative value not allowed");
        Firebase_method t = new Firebase_method();

        t.searchEmail("ushmita@gmail.com", new Firebase_method.ResultCallBack<UserData>() {
            @Override
            public void onResult(UserData data) {
                String name = data.getName();
            }
        });
    }


}
