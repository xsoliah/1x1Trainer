package com.example.manuel.a1x1trainer;

import com.example.manuel.a1x1trainer.Activities.LoginActivity;
import com.example.manuel.a1x1trainer.Ressources.RuntimeConstants;

import org.junit.Rule;
import org.junit.Test;

import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static com.example.manuel.a1x1trainer.TestUtils.checkButtonClosesActivity;
import static com.example.manuel.a1x1trainer.TestUtils.checkButtonWithText;
import static com.example.manuel.a1x1trainer.TestUtils.checkButtonWithoutText;
import static com.example.manuel.a1x1trainer.TestUtils.checkEditText;
import static com.example.manuel.a1x1trainer.TestUtils.checkViewVisible;
import static com.example.manuel.a1x1trainer.TestUtils.checkTextViewShows;
import static com.example.manuel.a1x1trainer.TestUtils.typeTextToEditText;

public class LoginActivityTest {
    // TODO: change!!!
    private String username = RuntimeConstants.RUNNING_LIVE ? "manuelhess" : "test_admin_1";
    private String password = RuntimeConstants.RUNNING_LIVE ? "1201manueL" : "tapw1";


    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule =
            new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void testElements() {
        checkTextViewShows(R.id.login_title_text, R.string.login_title);
        checkTextViewShows(R.id.login_name_text, R.string.login_name);
        checkTextViewShows(R.id.login_password_text, R.string.login_passowrd);

        checkButtonWithText(R.id.login_register_btn, R.string.login_register);
        checkButtonWithoutText(R.id.login_login_btn);
        checkEditText(R.id.login_username_input);
        checkEditText(R.id.login_password_input);
    }

    @Test
    public void testBackClick() {
        checkButtonClosesActivity(mActivityRule, R.id.login_back_btn);
    }

    @Test
    public void testInvalidUsernameLogin() {
        typeTextToEditText(R.id.login_password_input, password);
        onView(withId(R.id.login_login_btn)).perform(click());
        checkViewVisible(R.id.login_error_msg);
        checkTextViewShows(R.id.login_error_msg, R.string.login_invalid_input_err_msg);
    }

    @Test
    public void testInvalidPasswordLogin() {
        typeTextToEditText(R.id.login_username_input, username);
        onView(withId(R.id.login_login_btn)).perform(click());
        checkViewVisible(R.id.login_error_msg);
        checkTextViewShows(R.id.login_error_msg, R.string.login_invalid_input_err_msg);
    }

    @Test
    public void testInvalidUsernameAndPasswordLogin() {
        onView(withId(R.id.login_login_btn)).perform(click());
        checkViewVisible(R.id.login_error_msg);
        checkTextViewShows(R.id.login_error_msg, R.string.login_invalid_input_err_msg);
    }
}
