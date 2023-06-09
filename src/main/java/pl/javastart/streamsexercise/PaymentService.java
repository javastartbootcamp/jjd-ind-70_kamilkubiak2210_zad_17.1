package pl.javastart.streamsexercise;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.math.BigDecimal.*;

class PaymentService {

    private final PaymentRepository paymentRepository;
    private final DateTimeProvider dateTimeProvider;

    List<Payment> allPaymentsList() {
        return paymentRepository.findAll();
    }

    PaymentService(PaymentRepository paymentRepository, DateTimeProvider dateTimeProvider) {
        this.paymentRepository = paymentRepository;
        this.dateTimeProvider = dateTimeProvider;
    }

    /*
    Znajdź i zwróć płatności posortowane po dacie rosnąco
     */
    List<Payment> findPaymentsSortedByDateAsc() {
        return allPaymentsList().stream().sorted(Comparator.comparing(Payment::getPaymentDate)).toList();
    }

    /*
    Znajdź i zwróć płatności posortowane po dacie malejąco
     */
    List<Payment> findPaymentsSortedByDateDesc() {
        return findPaymentsSortedByDateAsc().stream().sorted(Comparator.comparing(Payment::getPaymentDate).reversed()).toList();
    }

    /*
    Znajdź i zwróć płatności posortowane po liczbie elementów rosnąco
     */

    List<Payment> findPaymentsSortedByItemCountAsc() {
        return allPaymentsList().stream().sorted(Comparator.comparing(payment -> payment.getPaymentItems().size())).toList();
    }

    /*
    Znajdź i zwróć płatności posortowane po liczbie elementów malejąco
     */
    List<Payment> findPaymentsSortedByItemCountDesc() {
        return findPaymentsSortedByItemCountAsc().stream().sorted((a, b) -> -1).toList();
    }

    /*
    Znajdź i zwróć płatności dla wskazanego miesiąca
     */
    List<Payment> findPaymentsForGivenMonth(YearMonth yearMonth) {
        return allPaymentsList().stream().filter(payment -> payment.getPaymentDate().getMonthValue() == yearMonth.getMonthValue()
                && payment.getPaymentDate().getYear() == yearMonth.getYear())
                .toList();
    }

    /*
    Znajdź i zwróć płatności dla aktualnego miesiąca
     */
    List<Payment> findPaymentsForCurrentMonth() {
        return findPaymentsForGivenMonth(dateTimeProvider.yearMonthNow());
    }

    /*
    Znajdź i zwróć płatności dla ostatnich X dni
     */
    List<Payment> findPaymentsForGivenLastDays(int days) {
        ZonedDateTime dateDaysAgo = dateTimeProvider.zonedDateTimeNow().minusDays(days);
        return allPaymentsList().stream()
                .filter(payment -> payment.getPaymentDate().isAfter(dateDaysAgo) && payment.getPaymentDate().isBefore(dateTimeProvider.zonedDateTimeNow()))
                .toList();
    }

    /*
    Znajdź i zwróć płatności z jednym elementem
     */
    Set<Payment> findPaymentsWithOnePaymentItem() {
        return allPaymentsList().stream()
                .filter(payment -> payment.getPaymentItems().size() == 1)
                .collect(Collectors.toSet());
    }

    /*
    Znajdź i zwróć nazwy produktów sprzedanych w aktualnym miesiącu
     */

    Set<String> findProductsSoldInCurrentMonth() {
        return findPaymentsForCurrentMonth().stream()
                .map(Payment::getPaymentItems)
                .flatMap(List::stream)
                .map(PaymentItem::getName)
                .collect(Collectors.toSet());
    }

    /*
    Policz i zwróć sumę sprzedaży dla wskazanego miesiąca
     */
    BigDecimal sumTotalForGivenMonth(YearMonth yearMonth) {
        return findPaymentsForGivenMonth(yearMonth).stream()
                .map(Payment::getPaymentItems)
                .flatMap(List::stream).map(PaymentItem::getFinalPrice)
                .reduce(ZERO, BigDecimal::add);
    }

    /*
    Policz i zwróć sumę przyznanych rabatów dla wskazanego miesiąca
     */
    BigDecimal sumDiscountForGivenMonth(YearMonth yearMonth) {
        return findPaymentsForGivenMonth(yearMonth).stream()
                .map(Payment::getPaymentItems)
                .flatMap(List::stream)
                .map(paymentItem -> paymentItem.getRegularPrice().subtract(paymentItem.getFinalPrice()))
                .reduce(ZERO, BigDecimal::add);
    }

    /*
    Znajdź i zwróć płatności dla użytkownika z podanym mailem
     */

    List<PaymentItem> getPaymentsForUserWithEmail(String userEmail) {
        return allPaymentsList().stream()
                .filter(payment -> payment.getUser().getEmail().equals(userEmail))
                .map(Payment::getPaymentItems)
                .flatMap(List::stream)
                .toList();
    }

    /*
    Znajdź i zwróć płatności, których wartość przekracza wskazaną granicę
     */

    Set<Payment> findPaymentsWithValueOver(int value) {
        return allPaymentsList().stream()
                .filter(payment -> payment.paymentItemsValue().compareTo(BigDecimal.valueOf(value)) > 0)
                .collect(Collectors.toSet());
    }
}