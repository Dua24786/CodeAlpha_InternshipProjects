import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

class Stock {
    private String symbol;
    private double price;

    public Stock(String symbol, double price) {
        this.symbol = symbol;
        this.price = price;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getPrice() {
        return price;
    }
}

class Holding {
    private Stock stock;
    private int quantity;

    public Holding(Stock stock, int quantity) {
        this.stock = stock;
        this.quantity = quantity;
    }

    public Stock getStock() {
        return stock;
    }

    public int getQuantity() {
        return quantity;
    }

    public void addQuantity(int qty) {
        quantity += qty;
    }

    public void removeQuantity(int qty) {
        quantity -= qty;
    }
}

class Portfolio {
    private double balance;
    private ArrayList<Holding> holdings;

    public Portfolio(double balance) {
        this.balance = balance;
        holdings = new ArrayList<>();
    }

    public double getBalance() {
        return balance;
    }

    public ArrayList<Holding> getHoldings() {
        return holdings;
    }

    public boolean buyStock(Stock stock, int qty) {
        double cost = stock.getPrice() * qty;

        if (cost > balance)
            return false;

        balance -= cost;

        for (Holding h : holdings) {
            if (h.getStock().getSymbol().equals(stock.getSymbol())) {
                h.addQuantity(qty);
                return true;
            }
        }

        holdings.add(new Holding(stock, qty));
        return true;
    }

    public boolean sellStock(Stock stock, int qty) {

        for (Holding h : holdings) {

            if (h.getStock().getSymbol().equals(stock.getSymbol())) {

                if (h.getQuantity() < qty)
                    return false;

                h.removeQuantity(qty);
                balance += stock.getPrice() * qty;

                if (h.getQuantity() == 0)
                    holdings.remove(h);

                return true;
            }
        }

        return false;
    }

    public double getPortfolioValue() {
        double value = balance;

        for (Holding h : holdings) {
            value += h.getStock().getPrice() * h.getQuantity();
        }

        return value;
    }
}

public class StockTradingPlatform extends JFrame {

    private JComboBox<String> stockBox;
    private JTextField quantityField;
    private JTextArea displayArea;

    private ArrayList<Stock> marketStocks;
    private Portfolio portfolio;

    public StockTradingPlatform() {

        portfolio = new Portfolio(10000);

        marketStocks = new ArrayList<>();

        marketStocks.add(new Stock("AAPL", 180));
        marketStocks.add(new Stock("GOOGL", 140));
        marketStocks.add(new Stock("TSLA", 250));
        marketStocks.add(new Stock("AMZN", 160));

        setTitle("Stock Trading Platform");
        setSize(700, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        createGUI();
        updateDisplay();
    }

    private void createGUI() {

        JPanel panel = new JPanel();

        stockBox = new JComboBox<>();

        for (Stock stock : marketStocks) {
            stockBox.addItem(stock.getSymbol());
        }

        quantityField = new JTextField(5);

        JButton buyButton = new JButton("Buy");
        JButton sellButton = new JButton("Sell");

        panel.add(new JLabel("Stock"));
        panel.add(stockBox);

        panel.add(new JLabel("Quantity"));
        panel.add(quantityField);

        panel.add(buyButton);
        panel.add(sellButton);

        add(panel, BorderLayout.NORTH);

        displayArea = new JTextArea();
        displayArea.setEditable(false);

        add(new JScrollPane(displayArea), BorderLayout.CENTER);

        buyButton.addActionListener(e -> buyStock());

        sellButton.addActionListener(e -> sellStock());
    }

    private Stock getSelectedStock() {

        String symbol = (String) stockBox.getSelectedItem();

        for (Stock stock : marketStocks) {
            if (stock.getSymbol().equals(symbol))
                return stock;
        }

        return null;
    }

    private void buyStock() {

        try {

            int qty = Integer.parseInt(quantityField.getText());

            Stock stock = getSelectedStock();

            if (portfolio.buyStock(stock, qty)) {
                JOptionPane.showMessageDialog(this,
                        "Stock purchased successfully.");
            } else {
                JOptionPane.showMessageDialog(this,
                        "Insufficient balance.");
            }

            updateDisplay();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Enter valid quantity.");
        }
    }

    private void sellStock() {

        try {

            int qty = Integer.parseInt(quantityField.getText());

            Stock stock = getSelectedStock();

            if (portfolio.sellStock(stock, qty)) {
                JOptionPane.showMessageDialog(this,
                        "Stock sold successfully.");
            } else {
                JOptionPane.showMessageDialog(this,
                        "Not enough shares.");
            }

            updateDisplay();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Enter valid quantity.");
        }
    }

    private void updateDisplay() {

        StringBuilder sb = new StringBuilder();

        sb.append("===== MARKET DATA =====\n");

        for (Stock stock : marketStocks) {
            sb.append(stock.getSymbol())
              .append(" : $")
              .append(stock.getPrice())
              .append("\n");
        }

        sb.append("\n===== PORTFOLIO =====\n");

        sb.append("Cash Balance: $")
          .append(String.format("%.2f", portfolio.getBalance()))
          .append("\n\n");

        for (Holding h : portfolio.getHoldings()) {

            sb.append(h.getStock().getSymbol())
              .append(" - Shares: ")
              .append(h.getQuantity())
              .append(" | Value: $")
              .append(String.format("%.2f",
                      h.getQuantity() * h.getStock().getPrice()))
              .append("\n");
        }

        sb.append("\nPortfolio Value: $")
          .append(String.format("%.2f",
                  portfolio.getPortfolioValue()));

        displayArea.setText(sb.toString());
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() ->
                new StockTradingPlatform().setVisible(true));
    }
}