package it.unibo.nestedenum;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.Locale;
// import java.util.Objects;
import java.util.List;

/**
 * Implementation of {@link MonthSorter}.
 */
public final class MonthSorterNested implements MonthSorter {

    private static final int FIRST_ELEMENT_IN_LIST = 0;
    private static final int FIRST_MATCHED_MONTH = 0;
    private static final int SECOND_MATCHED_MONTH = 1;

    private enum Month {
        // CHECKSTYLE: MagicNumber OFF
        JANUARY(31),
        FEBRUARY(28),
        MARCH(31),
        APRIL(30), 
        MAY(31),
        JUNE(30),
        JULY(31),
        AUGUST(31),
        SEPTEMBER(30),
        OCTOBER(31),
        NOVEMBER(30),
        DECEMBER(31);
        // CHECKSTYLE: MagicNumber ON

        private final int daysInAMonth;

        Month(final int days) {
            this.daysInAMonth = days;
        }

        int getDaysInAMonth() {
            return daysInAMonth;
        }

        private static Month checkConformityChars(final String string) {
            final String testingString = string.toUpperCase(Locale.ROOT);
            final List<Month> associatedMonth = new LinkedList<>();
            for (final Month month : values()) {
                if (month.name().contains(testingString)) {
                    associatedMonth.addFirst(month);
                }
            }
            return associatedMonth.size() > 1 || associatedMonth.isEmpty()
                ? null
                : associatedMonth.get(FIRST_ELEMENT_IN_LIST);
        }

        private static String monthExceptionMessage(final String month) {
            return month + " doesn't match with any type of month";
        }

        private static List<Month> sorterSetter(final String month1, final String month2) {
            final Month firstMatchedMonth = checkConformityChars(month1);
            final Month secondMatchedMonth = checkConformityChars(month2);
            if (firstMatchedMonth == null) {
                throw new IllegalArgumentException(monthExceptionMessage(month1));
            }
            if (secondMatchedMonth == null) {
                throw new IllegalArgumentException(monthExceptionMessage(month2));
            }
            return List.of(firstMatchedMonth, secondMatchedMonth);
        }

        static Comparator<String> comparingByDays() {
            return new Comparator<>() {
                @Override
                public int compare(final String month1, final String month2) {
                    final List<Month> months = sorterSetter(month1, month2);
                    return Integer.compare(
                        months.get(FIRST_MATCHED_MONTH).getDaysInAMonth(),
                        months.get(SECOND_MATCHED_MONTH).getDaysInAMonth()
                    );
                }
            };
        }

        static Comparator<String> comparingByOrder() {
            return new Comparator<>() {
                @Override
                public int compare(final String month1, final String month2) {
                    final List<Month> months = sorterSetter(month1, month2);
                    return months.get(FIRST_MATCHED_MONTH).compareTo(months.get(SECOND_MATCHED_MONTH));
                }
            };
        }
    }

    @Override
    public Comparator<String> sortByDays() {
        return MonthSorterNested.Month.comparingByDays();
    }

    @Override
    public Comparator<String> sortByOrder() {
        return MonthSorterNested.Month.comparingByOrder();
    }
}
