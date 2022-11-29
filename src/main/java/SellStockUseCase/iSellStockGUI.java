package SellStockUseCase;

public interface iSellStockGUI {
     /**
      * This is the interface for the sell stock use case
      */
     void close();
     void displayQuantityFailure();
     void displayConnectionFailure();
     int getQuantity();
     void addSellAction(Runnable onSell);
     void addGoBackAction(Runnable onBack);
     void displaySuccess();
     String getSymbol();

    void updateQuantityLabel(int quant);
}
