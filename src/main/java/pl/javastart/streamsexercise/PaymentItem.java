package pl.javastart.streamsexercise;

import java.math.BigDecimal;
import java.util.Objects;

public class PaymentItem {

    private String name;
    private BigDecimal regularPrice;
    private BigDecimal finalPrice;

    public PaymentItem(String name, BigDecimal regularPrice, BigDecimal finalPrice) {
        this.name = name;
        this.regularPrice = regularPrice;
        this.finalPrice = finalPrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getRegularPrice() {
        return regularPrice;
    }

    public void setRegularPrice(BigDecimal regularPrice) {
        this.regularPrice = regularPrice;
    }

    public  BigDecimal getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(BigDecimal finalPrice) {
        this.finalPrice = finalPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PaymentItem that = (PaymentItem) o;
        return Objects.equals(name, that.name) && Objects.equals(regularPrice, that.regularPrice) && Objects.equals(finalPrice, that.finalPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, regularPrice, finalPrice);
    }
}
