package SellStockUseCase;

import entities.Portfolio;


public class SellStockPresenter {
    private final iSellStockGUI view;
    private final Portfolio portfolio;
    private final SellStockController controller;

    public SellStockPresenter(iSellStockGUI view, Portfolio portfolio) {
        this.view = view;
        controller = new SellStockController();
        this.portfolio = portfolio;
        view.addSellAction(this::onSell);
        view.addGoBackAction(this::onBack);
    }
    private void onBack() {
        view.close();
    }
    private void onSell() {
        String symbol = view.getSymbol();
        int quantity = view.getQuantity();
        try {
            SellOutputResponse response = controller.sellStock(new SellInputRequest(portfolio, symbol));
            if (response.possible()) {
                view.displaySuccess();
            } else {
                view.displayQuantityFailure();
            }
        } catch (Exception e) {
            view.displayConnectionFailure();
        }
    }
}