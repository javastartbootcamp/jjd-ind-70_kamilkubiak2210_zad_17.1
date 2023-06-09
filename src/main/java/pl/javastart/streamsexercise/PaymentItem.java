package pl.javastart.streamsexercise;

import java.math.BigDecimal;
import java.util.Objects;

public class PaymentItem {

    private final String name;
    private final BigDecimal regularPrice;
    private final BigDecimal finalPrice;

    public PaymentItem(String name, BigDecimal regularPrice, BigDecimal finalPrice) {
        this.name = name;
        this.regularPrice = regularPrice;
        this.finalPrice = finalPrice;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getRegularPrice() {
        return regularPrice;
    }

    public BigDecimal getFinalPrice() {
        return finalPrice;
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