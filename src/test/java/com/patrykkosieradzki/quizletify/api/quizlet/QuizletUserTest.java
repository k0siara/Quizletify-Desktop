package com.patrykkosieradzki.quizletify.api.quizlet;

import com.patrykkosieradzki.quizletify.api.google.Language;
import com.patrykkosieradzki.quizletify.api.quizlet.QuizletException;
import com.patrykkosieradzki.quizletify.api.quizlet.QuizletSet;
import com.patrykkosieradzki.quizletify.api.quizlet.QuizletUser;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static junit.framework.TestCase.assertEquals;

public class QuizletUserTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private QuizletUser user;
    private String username = "k00siara";
    private String clientId = "NQvmENjEg4";
    private String secretKey = "MvvmFTxmTKC4zDxjXSfFvz";

    @Before
    public void before() {
        user = new QuizletUser(username, clientId, secretKey);
    }

    @Test
    public void whenUserIsInstantiatedThenUsernameIsStored() {
        assertEquals(username, user.getUsername());
    }

    @Test
    public void whenUserIsInstantiatedThenClientIdIsStored() {
        assertEquals(clientId, user.getClientId());
    }

    @Test
    public void whenUserIsInstantiatedThenSecretKeyIsStored() {
        assertEquals(secretKey, user.getSecretKey());
    }

    @Test
    public void whenUserGetSetWithWrongTitleThenQuizletException() throws QuizletException {
        String title = "123asdads/...";
        exception.expect(QuizletException.class);
        exception.expectMessage("Set Not Found " + title);
        user.getSet(title);
    }

    @Test
    public void whenUserGetSetWithWrongIdThenQuizletException() throws QuizletException {
        int id = 1010101912;
        exception.expect(QuizletException.class);
        exception.expectMessage("Set Not Found " + id);
        user.getSet(id);
    }

    @Test
    public void whenUserAddSetWithLessThan2TermsThenQuizletException() throws QuizletException {
        QuizletSet set = new QuizletSet("title", Language.ENGLISH, Language.ENGLISH);
        set.addTerm("test", "test");
        exception.expect(QuizletException.class);
        exception.expectMessage("Set Must Contain At Least 2 Terms");
        user.addSet(set);
    }

    @Test
    public void whenUserDeleteSetWithWrongTitleThenQuizletException() throws QuizletException {
        String title = "123123qsdasdowb";
        exception.expect(QuizletException.class);
        exception.expectMessage("Set Not Found " + title);
        user.deleteSet(title);
    }

    @Test
    public void whenUserDeleteSetWithWrongIdThenQuizletException() throws QuizletException {
        int id = 1010101912;
        exception.expect(QuizletException.class);
        exception.expectMessage("Set Not Found " + id);
        user.deleteSet(id);
    }


}
