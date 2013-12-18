package modelTests;

import models.Post;
import models.User;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Created by nepjua on 12/16/13.
 */
public class PostTest {

    @Test
    public void build() {
        Post p = new Post();

        User u = User.findById(Long.valueOf(1));


    }

}
