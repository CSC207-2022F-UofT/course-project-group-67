package SellStockUseCase;
import APIInterface.StockAPIAccess;
import APIInterface.StockAPIRequest;
import APIInterface.StockAPIResponse;
import entities.Portfolio;
import java.io.IOException;

public class SellUseCaseInteractor {

    /**
     * This method is used to sell stocks from a portfolio
     * @param sell The request object containing the portfolio, symbol, and quantity
     * @return The response object containing the portfolio, symbol, quantity, and price
     * @throws IOException
     */

    public SellOutputResponse sellStock(SellInputRequest sell) throws IOException {
        Portfolio portfolio = sell.getPortfolio();
        String symbol = sell.getSymbol();
        int quantity = sell.getQuantity();
        boolean possible = portfolio.sellStock(symbol, quantity);
        StockAPIAccess stockAPIAccess = new StockAPIAccess();
        StockAPIRequest stockAPIRequest = new StockAPIRequest(symbol);
        StockAPIResponse stockAPIResponse = stockAPIAccess.accessAPI(stockAPIRequest);
        if(possible){
            double totalValue = stockAPIResponse.price * quantity;
            return new SellOutputResponse("Sale successful", totalValue, quantity, symbol);
        }
        else{
            return new SellOutputResponse("Sale unsuccessful", 0, 0, symbol);
        }
    }
}
