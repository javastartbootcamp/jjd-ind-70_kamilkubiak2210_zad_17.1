package pl.javastart.streamsexercise;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

public class Payment {

    private final User user;
    private final ZonedDateTime paymentDate;
    private final List<PaymentItem> paymentItems;

    public Payment(User user, ZonedDateTime paymentDate, List<PaymentItem> paymentItems) {
        this.user = user;
        this.paymentDate = paymentDate;
        this.paymentItems = paymentItems;
    }

    public BigDecimal paymentItemsValue() {
        BigDecimal value = new BigDecimal(0);
        List<PaymentItem> paymentItems1 = getPaymentItems();
        for (PaymentItem paymentItem : paymentItems1) {
            BigDecimal finalPrice = paymentItem.getFinalPrice();
            value = value.add(finalPrice);
        }

        return value;
    }

    public User getUser() {
        return user;
    }

    public ZonedDateTime getPaymentDate() {
        return paymentDate;
    }

    public List<PaymentItem> getPaymentItems() {
        return paymentItems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Payment payment = (Payment) o;
        return Objects.equals(user, payment.user) && Objects.equals(paymentDate, payment.paymentDate) && Objects.equals(paymentItems, payment.paymentItems);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, paymentDate, paymentItems);
    }
}