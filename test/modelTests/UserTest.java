package modelTests;

import exceptions.AppException;
import models.User;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Created by nepjua on 12/16/13.
 */
public class UserTest {

    @Test
    public void build() throws AppException {
        User user = new User();

        user.username = "test1";
        user.name = "Test 1 Name";
        user.setPassword("pw");
        user.save();
    }
}
