package com.patrykkosieradzki.quizletify.api.quizlet.authorization;

import com.patrykkosieradzki.quizletify.api.quizlet.QuizletException;
import com.patrykkosieradzki.quizletify.api.quizlet.QuizletUser;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class QuizletAuthTest {

    private QuizletUser user;
    private QuizletAuth authorization;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void before() {
        String username = "k00siara";
        String clientId = "NQvmENjEg4";
        String secretKey = "MvvmFTxmTKC4zDxjXSfFvz";
        user = new QuizletUser(username, clientId, secretKey);
        authorization = new QuizletAuth(user);
    }

    @Test
    public void givenProperUsernameAndClientIdAndSecretKeyThenNoException() throws Exception {
        authorization.getAccessToken();
    }

    @Test
    public void givenWrongUsernameThenQuizletException() throws Exception {
        String username = "123...123";
        String clientId = "NQvmENjEg4";
        String secretKey = "MvvmFTxmTKC4zDxjXSfFvz";
        exception.expect(QuizletException.class);
        exception.expectMessage("Wrong Username");
        new QuizletAuth(new QuizletUser(username, clientId, secretKey)).getAccessToken();
    }

    @Test
    public void givenWrongClientIdThenQuizletException() throws Exception {
        String username = "k00siara";
        String clientId = "1234abcd";
        String secretKey = "MvvmFTxmTKC4zDxjXSfFvz";
        exception.expect(QuizletException.class);
        exception.expectMessage("Wrong ClientId");
        new QuizletAuth(new QuizletUser(username, clientId, secretKey)).getAccessToken();
    }

    @Test
    public void givenWrongSecretKeyThenQuizletException() throws Exception {
        String username = "k00siara";
        String clientId = "NQvmENjEg4";
        String secretKey = "MvzxzcxxmTDxjXasdasdvz";
        exception.expect(QuizletException.class);
        exception.expectMessage("Wrong SecretKey");
        new QuizletAuth(new QuizletUser(username, clientId, secretKey)).getAccessToken();
    }


}
