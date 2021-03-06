package test.com.github.springaki.codersation;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.github.springaki.codersation.Money;
import com.github.springaki.codersation.VendingMachine;

public class VendingMachineTest {

	VendingMachine sut = new VendingMachine();

	@Test
	public void testInsertMoney() {
		sut.insertMoney(Money.Ten);
	}

	@Test
	public void _10円玉_50円玉_100円玉_500円玉_1000円札を１つずつ投入できる() {
		sut.insertMoney(Money.Ten);
		sut.insertMoney(Money.Fifty);		
		sut.insertMoney(Money.OneHundred);		
		sut.insertMoney(Money.FiveHundred);		
		sut.insertMoney(Money.OneThousand);		
	}

	@Test
	public void 投入は複数回できる() {
		sut.insertMoney(Money.Ten);
		sut.insertMoney(Money.Ten);
	}

	@Test
	public void 投入金額の総計を取得できる() {
		assertEquals(0, sut.getTotalAmount());		
		sut.insertMoney(Money.Ten);
		sut.insertMoney(Money.Ten);
		assertEquals(20, sut.getTotalAmount());		
	}
	
	@Test
	public void 払い戻し操作を行うと投入金額の総計を釣り銭として出力する() {
		assertEquals(0, sut.calcel());
		sut.insertMoney(Money.Ten);
		sut.insertMoney(Money.Ten);
		assertEquals(20, sut.calcel());
		assertEquals(0, sut.calcel());
	}

}
