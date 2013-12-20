package modelTests;

import models.Post;
import models.User;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;

/**
 * Created by nepjua on 12/16/13.
 */
public class PostTest {

    public void findById() {
        Post p = new Post();

        User u = User.find.byId(Long.valueOf(1));
    }

    @Test
    public void build() {
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
            @Override
            public void run() {
                findById();
            }
        });
    }

}
