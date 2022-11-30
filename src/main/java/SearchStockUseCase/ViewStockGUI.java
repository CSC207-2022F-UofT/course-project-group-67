package SearchStockUseCase;

import APIInterface.StockAPIGateway;
import APIInterface.StockAPIRequest;
import APIInterface.StockAPIResponse;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;


public class ViewStockGUI extends JFrame implements iViewStockGUI {
    private double stockPrice;
    private Calendar from = Calendar.getInstance();
    private Interval stockPriceInterval = Interval.DAILY;
    private final String stockSymbol;
    private List<HistoricalQuote> histData;
    private JPanel mainPanel;
    private JLabel stockLabel;
    private JButton buyStockButton;
    private JButton sellStockButton;
    private JPanel graph;
    private JButton todayButton;
    private JButton weekButton;
    private JButton yearButton;
    private JPanel infoPanel;
    private JLabel currentPrice;
    private JLabel up_down;
    private JLabel curr_high;
    private JLabel curr_low;
    private JButton refreshButton;
    private JTable priceTable;
    private JScrollPane tableScrollPane;
    private JButton backButton;
    public ViewStockGUI(String symbol) {
        super();
        this.stockSymbol = symbol;
        priceTable.setDefaultEditor(Object.class, null); //Disabling cell editing
        stockLabel.setText(this.stockSymbol + stockMarketStatus());
//        this.from.add(Calendar.DATE, -7); //Date of the last 7 days
//
//        //Setting up Button
//        refreshButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                try {
//                    updateValues();
//                } catch (IOException ex) {
//                    throw new RuntimeException(ex);
//                }
//            }
//        });
//
//        updateTable();
//        todayButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                try {
//                    todayButtonAction();
//                    updateTable();
//                } catch (IOException ex) {
//                    throw new RuntimeException(ex);
//                }
//            }
//        });
//        weekButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                try {
//                    weeklyButtonAction();
//                    updateTable();
//                } catch (IOException ex) {
//                    throw new RuntimeException(ex);
//                }
//            }
//        });
//        yearButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                try {
//                    monthlyButtonAction();
//                    updateTable();
//                } catch (IOException ex) {
//                    throw new RuntimeException(ex);
//                }
//
//            }
//        });
        //Setting up JFrame
        this.setContentPane(this.mainPanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
    }

    private String getStockHigh() {
//        new DecimalFormat("0.00").format(latestValues.getHigh())
        return "";
    }

    @Override
    public void updateTable(DefaultTableModel tableModel) {
        // Initializing the JTable
        priceTable.setModel(tableModel);
    }

    @Override
    public double updatePrice() throws Exception {
        /* This function should only be called periodically every minute*/
        StockAPIResponse stockAPIResponse;
        try {
            stockAPIResponse = new StockAPIGateway().getPrice(new StockAPIRequest(this.stockSymbol));
        } catch (IOException e) {
            throw new Exception(String.format("Invalid stock Symbol %s", this.stockSymbol));
        }
        return stockAPIResponse.getPrice();
    }

    @Override
    public void updateValues() throws IOException {
//        //Getting updated values from API
//        this.stock = new StockAPIGateway().getPriceHist(new StockAPIRequest(this.stockSymbol, this.from, this.stockPriceInterval));
//        this.histData = this.stock.getHistData();
//
//        //Setting up labels
//        HistoricalQuote latestValues = histData.get(histData.size() - 1);
//        curr_high.setText("High: " + new DecimalFormat("0.00").format(latestValues.getHigh()));
//        curr_low.setText("Low: " + new DecimalFormat("0.00").format(latestValues.getLow()));
//        currentPrice.setText("Current: " + new DecimalFormat("0.00").format(this.stock.getPrice()));
    }

    @Override
    public void todayButtonAction(Runnable onTodayButton){
        todayButton.addActionListener(e -> onTodayButton.run());
    }

    @Override
    public void weeklyButtonAction(Runnable onWeeklyButton){
        weekButton.addActionListener(e -> onWeeklyButton.run());
    }

    @Override
    public void yearlyButtonAction(Runnable onYearlyButton){
        yearButton.addActionListener(e -> onYearlyButton.run());
    }

    @Override
    public void addBuyStockAction(Runnable onBuyStock) {
        buyStockButton.addActionListener(e -> onBuyStock.run());
    }

    @Override
    public void addSellStockAction(Runnable onSellStock) {
        sellStockButton.addActionListener(e -> onSellStock.run());
    }

    @Override
    public void addBackAction(Runnable onBack) {
        backButton.addActionListener(e -> onBack.run());
    }

    @Override
    public String stockMarketStatus() {
        if (LocalDate.EPOCH.getDayOfWeek() == DayOfWeek.SATURDAY || LocalDate.EPOCH.getDayOfWeek() == DayOfWeek.SUNDAY) {
            return "(Closed)";
        } else if (9 > LocalTime.MAX.getHour() || LocalTime.MAX.getHour() > 16) {
            return "(Closed)";
        }
        return "";
    }

    @Override
    public void close() {
        dispose();
    }

    @Override
    public String getStockSymbol() {
        return this.stockSymbol;
    }

    @Override
    public void setHistData(List<HistoricalQuote> historicalQuotes) {
        this.histData = historicalQuotes;
    }

    @Override
    public void setStockPrice(double stockPrice){
        this.stockPrice = stockPrice;
    }

    @Override
    public void loadLabels(){
        //Setting up labels
        HistoricalQuote latestValues = histData.get(histData.size() - 1);
        curr_high.setText("High: " + latestValues.getHigh());
        curr_low.setText("Low: " + new DecimalFormat("0.00").format(latestValues.getLow()));
        currentPrice.setText("Current: " + new DecimalFormat("0.00").format(this.stockPrice));

        HistoricalQuote previousDayValues = histData.get(histData.size() - 1);
        double last_close = previousDayValues.getClose().doubleValue();
        up_down.setText(String.format("Up/Down: %.2f", (this.stockPrice - last_close)));
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayoutManager(4, 4, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.setPreferredSize(new Dimension(500, 500));
        buyStockButton = new JButton();
        buyStockButton.setText("Buy Stock");
        mainPanel.add(buyStockButton, new GridConstraints(3, 2, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        sellStockButton = new JButton();
        sellStockButton.setText("Sell Stock");
        mainPanel.add(sellStockButton, new GridConstraints(3, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        graph = new JPanel();
        graph.setLayout(new GridLayoutManager(2, 5, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(graph, new GridConstraints(1, 0, 1, 4, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(-1, 500), null, 0, false));
        todayButton = new JButton();
        todayButton.setText("Daily");
        graph.add(todayButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        weekButton = new JButton();
        weekButton.setText("Week");
        graph.add(weekButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        yearButton = new JButton();
        yearButton.setText("Year");
        graph.add(yearButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        refreshButton = new JButton();
        refreshButton.setHideActionText(true);
        refreshButton.setText("Refresh");
        graph.add(refreshButton, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        tableScrollPane = new JScrollPane();
        tableScrollPane.putClientProperty("html.disable", Boolean.TRUE);
        graph.add(tableScrollPane, new GridConstraints(1, 0, 1, 5, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        priceTable = new JTable();
        priceTable.setAutoCreateColumnsFromModel(true);
        priceTable.setAutoCreateRowSorter(true);
        priceTable.setAutoResizeMode(2);
        priceTable.setAutoscrolls(false);
        priceTable.setShowHorizontalLines(false);
        priceTable.setShowVerticalLines(true);
        tableScrollPane.setViewportView(priceTable);
        backButton = new JButton();
        backButton.setText("Back");
        graph.add(backButton, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        stockLabel = new JLabel();
        stockLabel.setHorizontalAlignment(0);
        stockLabel.setHorizontalTextPosition(0);
        stockLabel.setText("Stock");
        mainPanel.add(stockLabel, new GridConstraints(0, 0, 1, 4, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(10, 10), new Dimension(-1, 50), null, 0, false));
        infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(infoPanel, new GridConstraints(2, 0, 1, 4, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        currentPrice = new JLabel();
        currentPrice.setText("Current");
        infoPanel.add(currentPrice, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        up_down = new JLabel();
        up_down.setText("Up/Down");
        infoPanel.add(up_down, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        curr_high = new JLabel();
        curr_high.setText("High");
        infoPanel.add(curr_high, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        curr_low = new JLabel();
        curr_low.setText("Low");
        infoPanel.add(curr_low, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }

}
