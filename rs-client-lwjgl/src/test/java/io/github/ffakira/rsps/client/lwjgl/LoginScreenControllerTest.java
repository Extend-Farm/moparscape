package io.github.ffakira.rsps.client.lwjgl;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class LoginScreenControllerTest {

  @Test
  void editsUsernameAndPasswordAcrossFocusChanges() {
    LoginScreenController controller = new LoginScreenController();

    controller.append('a');
    controller.append('k');
    controller.append('i');
    controller.append('r');
    controller.append('a');
    controller.focusNext();
    controller.append('s');
    controller.append('e');
    controller.append('c');
    controller.append('r');
    controller.append('e');
    controller.append('t');

    LoginScreenState state = controller.state();
    assertThat(state.stage()).isEqualTo(TitleScreenStage.WELCOME);
    assertThat(state.username()).isEqualTo("akira");
    assertThat(state.password()).isEqualTo("secret");
    assertThat(state.maskedPassword()).isEqualTo("******");
    assertThat(controller.canSubmit()).isTrue();
  }

  @Test
  void backspaceAffectsFocusedFieldOnly() {
    LoginScreenController controller = new LoginScreenController();

    controller.append('a');
    controller.append('b');
    controller.focus(LoginField.PASSWORD);
    controller.append('1');
    controller.append('2');
    controller.backspace();

    LoginScreenState state = controller.state();
    assertThat(state.username()).isEqualTo("ab");
    assertThat(state.password()).isEqualTo("1");
  }

  @Test
  void clearResetsStateToFreshLoginForm() {
    LoginScreenController controller = new LoginScreenController();

    controller.append('a');
    controller.showCredentials();
    controller.focusNext();
    controller.append('1');
    controller.clear();

    assertThat(controller.state()).isEqualTo(
        new LoginScreenState(TitleScreenStage.CREDENTIALS, "", "", LoginField.USERNAME)
    );
    assertThat(controller.canSubmit()).isFalse();
  }

  @Test
  void clearThenShowWelcomeReturnsToInitialFrame() {
    LoginScreenController controller = new LoginScreenController();

    controller.showCredentials();
    controller.append('a');
    controller.focusNext();
    controller.append('1');
    controller.clear();
    controller.showWelcome();

    assertThat(controller.state()).isEqualTo(
        new LoginScreenState(TitleScreenStage.WELCOME, "", "", LoginField.USERNAME)
    );
  }

  @Test
  void canTransitionAcrossTitleScreenStages() {
    LoginScreenController controller = new LoginScreenController();

    controller.showPrivateServerInfo();
    assertThat(controller.state().stage()).isEqualTo(TitleScreenStage.PRIVATE_SERVER_INFO);

    controller.showCredentials();
    assertThat(controller.state().stage()).isEqualTo(TitleScreenStage.CREDENTIALS);

    controller.showWelcome();
    assertThat(controller.state().stage()).isEqualTo(TitleScreenStage.WELCOME);
  }

  @Test
  void enterAdvancesFromUsernameThenSubmitsFromPassword() {
    LoginScreenController controller = new LoginScreenController();

    controller.showCredentials();
    controller.append('a');
    controller.append('k');
    controller.append('i');
    controller.append('r');
    controller.append('a');

    assertThat(controller.advanceOrSubmitOnEnter()).isFalse();
    assertThat(controller.state().focusedField()).isEqualTo(LoginField.PASSWORD);

    controller.append('s');
    controller.append('e');
    controller.append('c');
    controller.append('r');
    controller.append('e');
    controller.append('t');

    assertThat(controller.advanceOrSubmitOnEnter()).isTrue();
  }
}
