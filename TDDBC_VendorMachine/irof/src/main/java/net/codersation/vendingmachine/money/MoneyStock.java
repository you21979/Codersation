package net.codersation.vendingmachine.money;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MoneyStock {

    private List<Money> stock = new ArrayList<>();

    public int getAmount() {
        int amount = 0;
        for (Money c : stock) {
            amount += c.getValue();
        }
        return amount;
    }

    /**
     * 引数のMoneyStockにこのオブジェクトに全てのStockを移す。移されたほうは空になる。
     *
     * @param s 中身を渡されるMoneyStock
     */
    void moveAllMoneyTo(MoneyStock s) {
        s.stock.addAll(stock);
        stock.clear();
    }

    private List<Money> getUseMoneyList(int i) {
        // TODO 指定された金額のお金を出すための見苦しいコード

        List<Money> result = new ArrayList<>();

        Collections.sort(stock);
        for (Money money : stock) {
            if (i <= 0) {
                break;
            }
            result.add(money);
            i -= money.getValue();
        }

        for (Money money : result.toArray(new Money[result.size()])) {
            if (i <= money.getValue() * -1) {
                result.remove(money);
                i += money.getValue();
            }
        }
        return result;

    }

    /**
     * 指定された金額のMoneyStockを取り出す。
     * 取り出された分の金額は減る。
     * TODO 丁度、多め、などは TakeOutRule とかにしたい
     *
     * @param price 取り出したい金額
     * @return 取り出されたMoneyStock
     */
    public MoneyStock takeOut(int price) {
        if (price > getAmount())
            throw new IllegalArgumentException("cannot take out. price:" + price + ", amount " + getAmount());
        MoneyStock res = new MoneyStock();
        res.stock = getUseMoneyList(price);
        for (Money money : res.stock) {
            remove(money);
        }
        return res;
    }

    private void remove(Money e) {
        stock.remove(e);
    }

    public boolean canTakeOut(int amount) {
        int difference = amount;
        for (Money money : getUseMoneyList(amount)) {
            difference -= money.getValue();
        }
        return difference == 0;
    }

    @Override
    public String toString() {
        return getText();
    }

    public void add(Money money, int count) {
        for (int i = 0; i < count; i++) {
            stock.add(money);
        }
    }

    public String getText() {
        Map<Money, Integer> map = new EnumMap<>(Money.class);
        for (Money money : stock) {
            if (!map.containsKey(money)) map.put(money, 0);
            map.put(money, map.get(money) + 1);
        }
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Money, Integer> entry : map.entrySet()) {
            if (sb.length() != 0) sb.append(", ");
            sb.append(String.format("%d(%d)", entry.getKey().getValue(), entry.getValue()));
        }
        return sb.toString();
    }
}
