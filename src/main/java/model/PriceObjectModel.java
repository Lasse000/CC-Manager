package model;

import java.sql.Date;

public class PriceObjectModel {

    private int cards_nr;
    private String source;
    private String currency;
    private String finish;
    private String price_type;
    private int amount;          // Preis in Cent (z.B. 1,29 € -> 129)
    private Date priceDate;      // java.sql.Date für die DB

    public PriceObjectModel() {
    }

    public PriceObjectModel(int cards_nr, String source, String currency, String finish, String price_type, int amount, Date priceDate) {
        this.cards_nr = cards_nr;
        this.source = source;
        this.currency = currency;
        this.finish = finish;
        this.price_type = price_type;
        this.amount = amount;
        this.priceDate = priceDate;
    }

    // Getter und Setter
    public int getCards_nr() {
        return cards_nr;
    }

    public void setCards_nr(int cards_nr) {
        this.cards_nr = cards_nr;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getFinish() {
        return finish;
    }

    public void setFinish(String finish) {
        this.finish = finish;
    }

    public String getPrice_type() {
        return price_type;
    }

    public void setPrice_type(String price_type) {
        this.price_type = price_type;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Date getPriceDate() {
        return priceDate;
    }

    public void setPriceDate(Date priceDate) {
        this.priceDate = priceDate;
    }
}