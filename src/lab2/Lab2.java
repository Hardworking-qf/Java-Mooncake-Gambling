package lab2;

import java.util.Random;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;

enum PrizeType {// Prize Types
	Nothing, // 白给
	YiXiu, // 一秀 一个四
	ErJu, // 二举 两个四
	SiJin, // 四进 四个相同（除四外）
	SanHong, // 三红 三个四
	DuiTang, // 对堂 123456
	SiHong, // 四红 四个四
	WuZiDengKe, // 五子登科 五个相同（除四外）
	WuHong, // 五红 五个四
	LiuBeiHei, // 六杯黑 六个相同（除四和一外）
	BianDiJin, // 遍地锦 六个一
	LiuBeiHong, // 六杯红 六个四
	ChaJinHua, // 插金花 114444
	// 复合型
	SiJinPlusYiXiu, // 四进带一秀
	SiJinPlusErJu, // 四进带二举
	WuZiPlusYiXiu// 五子带一秀
}// Total 16 Types

//Prize To PrizeClassified: 1 key to 1 value, can use Map

enum PrizeClassified {// Prize Types Classified, intermediate enum of Prize and PrizeClassified2
	Nothing, // 白给
	YiXiu, // 一秀
	ErJu, // 二举
	SiJin, // 四进
	SanHong, // 三红
	DuiTang, // 对堂
	ZhuangYuan, // 状元
	// 复合型
	SiJinPlusYiXiu, // 四进带一秀
	SiJinPlusErJu, // 四进带二举
	WuZiPlusYiXiu// 五子带一秀
}// Total 10 Types

//PrizeClassified to PrizeClassified2: 1 key to 1 or 2 value, can't use Map, use switch...case

enum PrizeClassified2 {// Prize Types Classified 2, for storage 6 types of prize
	YiXiu, // 一秀
	ErJu, // 二举
	SiJin, // 四进
	SanHong, // 三红
	DuiTang, // 对堂
	ZhuangYuan// 状元
}

public class Lab2 {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		while (true) {
//			new Mooncake_Gambling(new int[] { 2, 2, 2, 2, 2, 4 });// cheat
			new Mooncake_Gambling();
			String i = scanner.nextLine();
			if (i.contains("q") || i.contains("Q"))
				break;
		}
		System.out.println("See you next time!");
		scanner.close();
	}
}

class Mooncake_Gambling {
	private int dice[] = new int[6];

	Mooncake_Gambling() {// constructor
		Random r = new Random();
		for (int i = 0; i < 6; i++)// Summon random points
			dice[i] = r.nextInt(6) + 1;
		print_points();
		print_result(judge_points());
	}

	Mooncake_Gambling(int[] cheat) {// cheat
		dice = cheat;
		print_points();
		print_result(judge_points());
	}

	void print_points() {
		System.out.print("Points of dices:");
		for (int i = 0; i < 6; i++)
			System.out.print(Integer.toString(dice[i]) + (i == 5 ? "\n" : " "));
	}

	PrizeType judge_points() {
		int repeat[] = new int[6];
		for (int i = 0; i < 6; i++)
			repeat[dice[i] - 1]++;
		int max_repeat_times = 0;
		for (int i = 0; i < 6; i++)
			max_repeat_times = Math.max(max_repeat_times, repeat[i]);
		if (max_repeat_times == 1) {
			return PrizeType.DuiTang;
		} else if (max_repeat_times == 2 || max_repeat_times == 3) {
			if (repeat[3] == 0)
				return PrizeType.Nothing;
			else if (repeat[3] == 1)
				return PrizeType.YiXiu;
			else if (repeat[3] == 2)
				return PrizeType.ErJu;
			else if (repeat[3] == 3)
				return PrizeType.SanHong;
		} else if (max_repeat_times == 4) {
			if (repeat[3] == 4) {
				if (repeat[0] == 2)
					return PrizeType.ChaJinHua;
				else
					return PrizeType.SiHong;
			} else {
				if (repeat[3] == 1)
					return PrizeType.SiJinPlusYiXiu;
				else if (repeat[3] == 2)
					return PrizeType.SiJinPlusErJu;
				else
					return PrizeType.SiJin;
			}
		} else if (max_repeat_times == 5) {
			if (repeat[3] == 5)
				return PrizeType.WuHong;
			else {
				if (repeat[3] == 1)
					return PrizeType.WuZiPlusYiXiu;
				else
					return PrizeType.WuZiDengKe;
			}
		} else if (max_repeat_times == 6) {
			if (repeat[0] == 6)
				return PrizeType.BianDiJin;
			else if (repeat[3] == 6)
				return PrizeType.LiuBeiHong;
			else
				return PrizeType.LiuBeiHei;
		}
		return null;
	}

	void print_result(PrizeType result) {
		if (result == PrizeType.Nothing)
			System.out.println("You got Nothing! Wish you good luck next time!");
		else
			System.out.println("Congratulations! You got a(n) " + result);
	}
}

class Player {
//	private Map<Prize, Integer> WonPrize = new HashMap<Prize, Integer>();
//	private Map<Prize, Integer> OverFlowPrize = new HashMap<Prize, Integer>();
	private int WonPrize[] = new int[PrizeType.values().length];
	private int WonPrizeClassified2[] = new int[PrizeClassified2.values().length];
	private int OverFlowPrize[] = new int[PrizeType.values().length];
	private Prize ZhuangYuan;

	void WonPrize(Prize prize) {

	}

	int getPrizeNum(Prize prize) {
		return 0;
	}
}

class Prize {
	private int dice_repeat[] = new int[6];// save repeat counts for each point(1~6)
	private int max_repeat_time = 0;// the max repeat time for each num
	private int max_repeat_num = 0;// the first num that repeats most
	private int pointsum = 0;
	private PrizeType prize_type = null;

	private static Map<PrizeType, Integer> ZhuangYuanWeight = new HashMap<PrizeType, Integer>();
	static {
		ZhuangYuanWeight.put(PrizeType.SiHong, 0);
		ZhuangYuanWeight.put(PrizeType.WuZiDengKe, 1);
		ZhuangYuanWeight.put(PrizeType.WuZiPlusYiXiu, 1);
		ZhuangYuanWeight.put(PrizeType.WuHong, 2);
		ZhuangYuanWeight.put(PrizeType.LiuBeiHei, 3);
		ZhuangYuanWeight.put(PrizeType.BianDiJin, 4);
		ZhuangYuanWeight.put(PrizeType.LiuBeiHong, 5);
		ZhuangYuanWeight.put(PrizeType.ChaJinHua, 6);
	}

	Prize(int dice[]) {
		for (int i = 0; i < 6; i++) {
			this.dice_repeat[dice[i] - 1]++;
			this.pointsum += dice[i];
		}
		for (int i = 0; i < 6; i++) {
			if (this.dice_repeat[i] > this.max_repeat_time) {
				this.max_repeat_time = this.dice_repeat[i];
				this.max_repeat_num = i + 1;
			}
		}
		// prize judge
		if (max_repeat_time == 1) {// all appear once
			this.prize_type = PrizeType.DuiTang;
		} else if (max_repeat_time == 2 || max_repeat_time == 3) {// point 4 repeat 2 or 3 times
			if (dice_repeat[3] == 0)
				this.prize_type = PrizeType.Nothing;
			else if (dice_repeat[3] == 1)
				this.prize_type = PrizeType.YiXiu;
			else if (dice_repeat[3] == 2)
				this.prize_type = PrizeType.ErJu;
			else if (dice_repeat[3] == 3)
				this.prize_type = PrizeType.SanHong;
		} else if (max_repeat_time == 4) {// something repeat 4 times
			if (dice_repeat[3] == 4) {
				if (dice_repeat[0] == 2)
					this.prize_type = PrizeType.ChaJinHua;
				else
					this.prize_type = PrizeType.SiHong;
			} else {// SiJin Plus sth or nothing
				if (dice_repeat[3] == 1)
					this.prize_type = PrizeType.SiJinPlusYiXiu;
				else if (dice_repeat[3] == 2)
					this.prize_type = PrizeType.SiJinPlusErJu;
				else
					this.prize_type = PrizeType.SiJin;
			}
		} else if (max_repeat_time == 5) {// sth repeat 5 times
			if (dice_repeat[3] == 5)
				this.prize_type = PrizeType.WuHong;
			else {// Wuzi Plus sth or nothing
				if (dice_repeat[3] == 1)
					this.prize_type = PrizeType.WuZiPlusYiXiu;
				else
					this.prize_type = PrizeType.WuZiDengKe;
			}
		} else if (max_repeat_time == 6) {// sth repeat 6 times
			if (dice_repeat[0] == 6)
				this.prize_type = PrizeType.BianDiJin;
			else if (dice_repeat[3] == 6)
				this.prize_type = PrizeType.LiuBeiHong;
			else
				this.prize_type = PrizeType.LiuBeiHei;
		}
	}

	public boolean ZhuangYuan_isGreaterThan(Prize cmp) {// Compare between ZhuangYuan
		if (ZhuangYuanWeight.get(this.prize_type) > ZhuangYuanWeight.get(cmp.prize_type))
			return true;
		else if (ZhuangYuanWeight.get(this.prize_type) < ZhuangYuanWeight.get(cmp.prize_type))
			return false;
		else {
			if (this.prize_type == PrizeType.SiHong || this.prize_type == PrizeType.WuHong
					|| this.prize_type == PrizeType.LiuBeiHei)// Only need to CMP SUM
				return this.pointsum >= cmp.pointsum;// Self is Greater when equal
			else if (this.prize_type == PrizeType.BianDiJin || this.prize_type == PrizeType.LiuBeiHong
					|| this.prize_type == PrizeType.ChaJinHua)
				return true;// Self is Greater when equal
			else {// CMP between WuZi
				int this_appear_once = 0, this_appear_fifth = 0, cmp_appear_once = 0, cmp_appear_fifth = 0;
				for (int i = 0; i < 6; i++) {
					if (this.dice_repeat[i] == 1)
						this_appear_once = i;
					else if (this.dice_repeat[i] == 5)
						this_appear_once = i;
					if (cmp.dice_repeat[i] == 1)
						cmp_appear_once = i;
					else if (cmp.dice_repeat[i] == 5)
						cmp_appear_once = i;
				}
				if (this_appear_once != cmp_appear_once)
					return this_appear_fifth >= cmp_appear_fifth;
				else
					return this_appear_once > cmp_appear_once;
			}
		}
	}
}

class Gaming {
	private Player players[];
	// Prize Num Set
	private final int ZhuangYuanNumSet = 1;
	private final int DuiTangNumSet = 2;
	private final int SanHongNumSet = 4;
	private final int SiJinNumSet = 8;
	private final int ErJuNumSet = 16;
	private final int YiXiuNumSet = 32;

	// Prize Num Count
	private int ZhuangYuanNumCount = 0;
	private int DuiTangNumCount = 0;
	private int SanHongNumCount = 0;
	private int SiJinNumCount = 0;
	private int ErJuNumCount = 0;
	private int YiXiuNumCount = 0;

	private static Map<PrizeType, String> PrizeToChinese = new HashMap<PrizeType, String>();
	private static Map<PrizeType, PrizeClassified> PrizeClassify = new HashMap<PrizeType, PrizeClassified>();
	static {// 初始化map
		PrizeToChinese.put(PrizeType.Nothing, "无");
		PrizeToChinese.put(PrizeType.YiXiu, "一秀");
		PrizeToChinese.put(PrizeType.ErJu, "二举");
		PrizeToChinese.put(PrizeType.SiJin, "四进");
		PrizeToChinese.put(PrizeType.SanHong, "三红");
		PrizeToChinese.put(PrizeType.DuiTang, "对堂");
		PrizeToChinese.put(PrizeType.SiHong, "四红");
		PrizeToChinese.put(PrizeType.WuZiDengKe, "五子登科");
		PrizeToChinese.put(PrizeType.WuHong, "五红");
		PrizeToChinese.put(PrizeType.LiuBeiHei, "六杯黑");
		PrizeToChinese.put(PrizeType.BianDiJin, "遍地锦");
		PrizeToChinese.put(PrizeType.LiuBeiHong, "六杯红");
		PrizeToChinese.put(PrizeType.ChaJinHua, "插金花");
		PrizeToChinese.put(PrizeType.SiJinPlusYiXiu, "四进带一秀");
		PrizeToChinese.put(PrizeType.SiJinPlusErJu, "四进带二举");
		PrizeToChinese.put(PrizeType.WuZiPlusYiXiu, "五子登科带一秀");
		// Map<Prize,PrizeClassified> PrizeClassify
		PrizeClassify.put(PrizeType.Nothing, PrizeClassified.Nothing);
		PrizeClassify.put(PrizeType.YiXiu, PrizeClassified.YiXiu);
		PrizeClassify.put(PrizeType.ErJu, PrizeClassified.ErJu);
		PrizeClassify.put(PrizeType.SiJin, PrizeClassified.SiJin);
		PrizeClassify.put(PrizeType.SanHong, PrizeClassified.SanHong);
		PrizeClassify.put(PrizeType.DuiTang, PrizeClassified.DuiTang);
		PrizeClassify.put(PrizeType.SiHong, PrizeClassified.ZhuangYuan);
		PrizeClassify.put(PrizeType.WuZiDengKe, PrizeClassified.ZhuangYuan);
		PrizeClassify.put(PrizeType.WuHong, PrizeClassified.ZhuangYuan);
		PrizeClassify.put(PrizeType.LiuBeiHei, PrizeClassified.ZhuangYuan);
		PrizeClassify.put(PrizeType.BianDiJin, PrizeClassified.ZhuangYuan);
		PrizeClassify.put(PrizeType.LiuBeiHong, PrizeClassified.ZhuangYuan);
		PrizeClassify.put(PrizeType.ChaJinHua, PrizeClassified.ZhuangYuan);
		PrizeClassify.put(PrizeType.SiJinPlusYiXiu, PrizeClassified.SiJinPlusYiXiu);
		PrizeClassify.put(PrizeType.SiJinPlusErJu, PrizeClassified.SiJinPlusErJu);
		PrizeClassify.put(PrizeType.WuZiPlusYiXiu, PrizeClassified.WuZiPlusYiXiu);
		// Map<Prize,Integer> ZhuangYuanWeight

	}

	Gaming(int PlayerNum) {
		this.players = new Player[PlayerNum];

	}

	static String GetChineseName(Prize prize) {// return Chinese Name(String)
		return PrizeToChinese.get(prize);
	}

	static PrizeClassified ClassifyPrize(Prize prize) {// return classified result
		return PrizeClassify.get(prize);
//	}
//	
//	static bool IsGreater() {

	}
}
