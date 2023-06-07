package pl.javastart.streamsexercise;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        List<Payment> paymentsSortedByDateAsc = allPaymentsList();
        paymentsSortedByDateAsc.sort(Comparator.comparing(Payment::getPaymentDate));
        return paymentsSortedByDateAsc;
    }

    /*
    Znajdź i zwróć płatności posortowane po dacie malejąco
     */
    List<Payment> findPaymentsSortedByDateDesc() {
        List<Payment> paymentsSortedByDateAsc = findPaymentsSortedByDateAsc();
        paymentsSortedByDateAsc.sort(Comparator.comparing(Payment::getPaymentDate).reversed());
        return paymentsSortedByDateAsc;
    }

    /*
    Znajdź i zwróć płatności posortowane po liczbie elementów rosnąco
     */

    List<Payment> findPaymentsSortedByItemCountAsc() {
        List<Payment> allPaymentsList = allPaymentsList();
        allPaymentsList.sort(Comparator.comparing(payment -> payment.getPaymentItems().size()));
        return allPaymentsList;
    }

    /*
    Znajdź i zwróć płatności posortowane po liczbie elementów malejąco
     */
    List<Payment> findPaymentsSortedByItemCountDesc() {
        List<Payment> paymentsSortedByItemCountAsc = findPaymentsSortedByItemCountAsc();
        return paymentsSortedByItemCountAsc.stream().sorted((a, b) -> -1).toList();
    }

    /*
    Znajdź i zwróć płatności dla wskazanego miesiąca
     */
    List<Payment> findPaymentsForGivenMonth(YearMonth yearMonth) {
        List<Payment> allPaymentsList = allPaymentsList();
        return allPaymentsList.stream().filter(payment -> payment.getPaymentDate().getMonthValue() == yearMonth.getMonthValue()).toList();
    }

    /*
    Znajdź i zwróć płatności dla aktualnego miesiąca
     */
    List<Payment> findPaymentsForCurrentMonth() {
        List<Payment> allPaymentsList = allPaymentsList();
        return allPaymentsList.stream()
                .filter(payment -> payment.getPaymentDate().getMonthValue() == dateTimeProvider.yearMonthNow().getMonthValue()
                        && payment.getPaymentDate().getYear() == dateTimeProvider.yearMonthNow().getYear()).toList();

    }

    /*
    Znajdź i zwróć płatności dla ostatnich X dni
     */
    List<Payment> findPaymentsForGivenLastDays(int days) {
        List<Payment> allPaymentsList = allPaymentsList();
        return allPaymentsList.stream()
                .filter(payment -> payment.getPaymentDate().getDayOfYear() >= dateTimeProvider.zonedDateTimeNow().getDayOfYear() - days
                        && payment.getPaymentDate().getDayOfYear() <= dateTimeProvider.zonedDateTimeNow().getDayOfYear()
                        && payment.getPaymentDate().getYear() == dateTimeProvider.zonedDateTimeNow().getYear()).toList();
    }

    /*
    Znajdź i zwróć płatności z jednym elementem
     */
    Set<Payment> findPaymentsWithOnePaymentItem() {
        List<Payment> allPaymentsList = allPaymentsList();
        return allPaymentsList.stream().filter(payment -> payment.getPaymentItems().size() == 1).collect(Collectors.toSet());
    }

    /*
    Znajdź i zwróć nazwy produktów sprzedanych w aktualnym miesiącu
     */

    Set<String> findProductsSoldInCurrentMonth() {
        List<Payment> allPaymentsList = allPaymentsList();
        Stream<Payment> paymentStream = allPaymentsList.stream()
                .filter(payment -> payment.getPaymentDate().getMonth()
                        .equals(dateTimeProvider.yearMonthNow().getMonth()) && payment.getPaymentDate().getYear() == dateTimeProvider.yearMonthNow().getYear());
        Set<PaymentItem> collect = paymentStream.map(Payment::getPaymentItems).flatMap(List::stream).collect(Collectors.toSet());
        return collect.stream().map(PaymentItem::getName).collect(Collectors.toSet());
    }

    /*
    Policz i zwróć sumę sprzedaży dla wskazanego miesiąca
     */
    BigDecimal sumTotalForGivenMonth(YearMonth yearMonth) {
        List<Payment> allPaymentsList = allPaymentsList();
        Stream<Payment> paymentStream = allPaymentsList.stream()
                .filter(payment -> payment.getPaymentDate().getMonth()
                        .equals(yearMonth.getMonth()) && payment.getPaymentDate().getYear() == yearMonth.getYear());
        return paymentStream.map(Payment::getPaymentItems).flatMap(List::stream).map(PaymentItem::getFinalPrice).reduce(ZERO, BigDecimal::add);
    }

    /*
    Policz i zwróć sumę przyznanych rabatów dla wskazanego miesiąca
     */
    BigDecimal sumDiscountForGivenMonth(YearMonth yearMonth) {
        List<Payment> allPaymentsList = allPaymentsList();
        Stream<PaymentItem> paymentItemStream = allPaymentsList.stream()
                .filter(payment -> payment.getPaymentDate().getMonth()
                        .equals(yearMonth.getMonth()) && payment.getPaymentDate().getYear() == yearMonth.getYear())
                .map(Payment::getPaymentItems).flatMap(List::stream);
        BigDecimal reduce = paymentItemStream.map(PaymentItem::getRegularPrice).reduce(ZERO, BigDecimal::add);
        BigDecimal bigDecimal = sumTotalForGivenMonth(yearMonth);
        return reduce.subtract(bigDecimal);
    }

    /*
    Znajdź i zwróć płatności dla użytkownika z podanym mailem
     */

    List<PaymentItem> getPaymentsForUserWithEmail(String userEmail) {
        List<Payment> allPaymentsList = allPaymentsList();
        return allPaymentsList.stream().filter(payment -> payment.getUser().getEmail().equals(userEmail))
                .map(Payment::getPaymentItems).flatMap(List::stream).toList();
    }

    /*
    Znajdź i zwróć płatności, których wartość przekracza wskazaną granicę
     */

    Set<Payment> findPaymentsWithValueOver(int value) {
        List<Payment> allPaymentsList = allPaymentsList();
        return allPaymentsList.stream()
                .filter(payment -> payment.getPaymentItems().stream().map(paymentItem -> paymentItem.getFinalPrice().plus())
                        .anyMatch(bigDecimal -> bigDecimal.compareTo(BigDecimal.valueOf(value)) > 0)).collect(Collectors.toSet());
    }
}