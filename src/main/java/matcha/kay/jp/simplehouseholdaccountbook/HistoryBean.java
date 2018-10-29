package matcha.kay.jp.simplehouseholdaccountbook;

public class HistoryBean {
    private int id;
    private String bop_name;
    private int money;
    private int year;
    private int month;
    private int dayOfMonth;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBop_name() {
        return bop_name;
    }

    public void setBop_name(String bop_name) {
        this.bop_name = bop_name;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }
}
