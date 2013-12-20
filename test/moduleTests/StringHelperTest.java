package moduleTests;


import helpers.StringHelper;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;
/**
 * Created by nepjua on 12/20/13.
 */
public class StringHelperTest {

    @Test
    public void toSlug() {
        assertThat(StringHelper.toSlug("Text to slugify")).isEqualTo("text-to-slugify");
    }
}
