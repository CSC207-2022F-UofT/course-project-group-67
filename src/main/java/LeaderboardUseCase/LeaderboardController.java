package LeaderboardUseCase;

import entities.Leaderboard;

public class LeaderboardController {
    private final LeaderboardUseCaseInteractor interactor;

    /**
     * Creates a new instance of LeaderboardController with an instance of the LeaderboardUseCaseInteractor
     */
    public LeaderboardController() {
        this.interactor = new LeaderboardUseCaseInteractor();
    }

    /*
     * @return a LeaderboardResponse object generated from interactor.updateLeaderboard()
     */
    public LeaderboardResponse currLeaderboard() {
        return new LeaderboardResponse(interactor.updateLeaderboard());
    }
}
