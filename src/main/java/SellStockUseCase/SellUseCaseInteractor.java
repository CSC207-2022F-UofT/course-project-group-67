package SellStockUseCase;
import APIInterface.StockAPIAccess;
import APIInterface.StockAPIRequest;
import APIInterface.StockAPIResponse;
import db.iEntityDBGateway;
import entities.Portfolio;
import main.OuterLayerFactory;

import java.io.IOException;

public class SellUseCaseInteractor {

    /**
     * The interactor for selling stocks from a portfolio. This class is used to
     * interact with the API and the portfolio to sell stocks.
     * @param sell The request object containing the portfolio, symbol, and quantity
     * @return The response object containing the portfolio, symbol, quantity, and price
     */

    public SellOutputResponse sellStock(SellInputRequest sell) {

        Portfolio portfolio = sell.getPortfolio();
        String symbol = sell.getSymbol();
        int quantity = sell.getQuantity();

        iEntityDBGateway dbGateway = OuterLayerFactory.instance.getEntityDSGateway();
        StockAPIAccess stockAPIAccess = new StockAPIAccess();
        StockAPIRequest stockAPIRequest = new StockAPIRequest(symbol);
        StockAPIResponse stockAPIResponse = stockAPIAccess.getPrice(stockAPIRequest);
        SellType possible = portfolio.sellStock(symbol, stockAPIResponse.getPrice(), quantity);

        if (possible == SellType.ERROR){
            return new SellOutputResponse(false);
        }
        else{
            dbGateway.updatePortfolioBalance(
                    portfolio.getName(),
                    portfolio.getBalance(),
                    portfolio.getUsername());

            if (possible == SellType.REMOVE) {
                dbGateway.deleteStock(
                        symbol,
                        portfolio.getUsername(),
                        portfolio.getName());
            }
            else{
                dbGateway.updateStockQuantity(
                        symbol,
                        portfolio.getStockQuantity(symbol),
                        portfolio.getUsername(),
                        portfolio.getName()
                );
            }

            return new SellOutputResponse(true);
        }
        catch (NullPointerException e) {
            return new SellOutputResponse("You do not own any of this stock", 0, symbol, false);
        }

    }
}