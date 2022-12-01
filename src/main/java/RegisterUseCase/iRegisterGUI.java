package RegisterUseCase;

public interface iRegisterGUI {
    /**
     * This is the interface for the register use case
     * Refactored from RegisterUseCase.RegisterGUI
     */
    String getUsername();

    String getPassword();

    String getPasswordConfirm();

    void addRegisterAction(Runnable onSignUp);

    void addBackAction(Runnable onLogin);

    void presentUsernameError();

    void presentPasswordInvalidError();

    void presentPasswordNotMatchError();

    void close();
}