package LoginUseCase;

public class UserLoginController {
    private UserLoginInteractor interactor;

    public UserLoginController() {
        interactor = new UserLoginInteractor();
    }

    public UserLoginResponse loginUser(UserLoginRequest request) {
        return new UserLoginResponse(
                interactor.loginUser(
                        request.username(),
                        request.password(),
                        request.loginDate()));
    }

}