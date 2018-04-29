package ru.freee.f12018;

import java.util.Comparator;

public class Comparators {

    public class NamesComparator implements Comparator<Driver>{

        @Override
        public int compare(Driver driver, Driver comp) {
            return driver.name.compareTo(comp.name);
        }
    }

    public class PointsComparator implements Comparator<Driver>{

        @Override
        public int compare(Driver driver, Driver comp) {
            if (driver.points > comp.points)
                return -1;
            return driver.points < comp.points ? 1 : 0;
        }
    }

    public class WinsComparator implements Comparator<Driver>{

        @Override
        public int compare(Driver driver, Driver comp) {
            if (driver.wins > comp.wins)
                return -1;
            return driver.wins < comp.wins ? 1 : 0;
        }
    }

}
