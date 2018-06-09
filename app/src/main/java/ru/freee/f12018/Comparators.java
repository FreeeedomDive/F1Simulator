package ru.freee.f12018;

import java.util.Comparator;

public class Comparators {

    public static class NamesComparator implements Comparator<Driver>{

        @Override
        public int compare(Driver driver, Driver comp) {
            return driver.name.compareTo(comp.name);
        }
    }

    public static class PointsComparator implements Comparator<Driver>{

        @Override
        public int compare(Driver driver, Driver comp) {
            if (driver.points > comp.points)
                return -1;
            if (driver.points < comp.points)
                return 1;
            if (driver.wins > comp.wins)
                return -1;
            if (driver.wins < comp.wins)
                return 1;
            if (driver.summaryPositions < comp.summaryPositions)
                return -1;
            return driver.summaryPositions > comp.summaryPositions ? 1 : 0;
        }
    }

    public static class WinsComparator implements Comparator<Driver>{

        @Override
        public int compare(Driver driver, Driver comp) {
            if (driver.wins > comp.wins)
                return -1;
            return driver.wins < comp.wins ? 1 : 0;
        }
    }

    public static class PolesComparator implements Comparator<Driver>{

        @Override
        public int compare(Driver driver, Driver comp) {
            if (driver.poles > comp.poles)
                return -1;
            return driver.poles < comp.poles ? 1 : 0;
        }
    }

    public static class RetiresComparator implements Comparator<Driver>{

        @Override
        public int compare(Driver driver, Driver comp) {
            if (driver.retires < comp.retires)
                return -1;
            return driver.retires > comp.retires ? 1 : 0;
        }
    }

    public static class TeamNamesComparator implements Comparator<Team>{

        @Override
        public int compare(Team team, Team comp) {
            return team.name.compareTo(comp.name);
        }
    }

    public static class TeamPointsComparator implements Comparator<Team>{

        @Override
        public int compare(Team team, Team comp) {
            if (team.points > comp.points)
                return -1;
            if (team.points < comp.points)
                return 1;
            if (team.wins > comp.wins)
                return -1;
            return team.wins < comp.wins ? 1 : 0;
        }
    }

    public static class TeamWinsComparator implements Comparator<Team>{

        @Override
        public int compare(Team team, Team comp) {
            if (team.wins > comp.wins)
                return -1;
            return team.wins < comp.wins ? 1 : 0;
        }
    }

    public static class TeamPolesComparator implements Comparator<Team>{

        @Override
        public int compare(Team team, Team comp) {
            if (team.poles > comp.poles)
                return -1;
            return team.poles < comp.poles ? 1 : 0;
        }
    }

    public static class TeamRetiresComparator implements Comparator<Team>{

        @Override
        public int compare(Team team, Team comp) {
            if (team.retires < comp.retires)
                return -1;
            return team.retires > comp.retires ? 1 : 0;
        }
    }
}
