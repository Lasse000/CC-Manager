package model;

public class PriceObjectModel {

    private int cards_nr;
    private String source;
    private String currency;
    private String price_type;
    private int amount;


    public PriceObjectModel(){

    }
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

}
