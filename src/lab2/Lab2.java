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

enum PrizeClassified2 {// Except ZhuangYuan
	YiXiu, ErJu, SiJin, SanHong, DuiTang
}

public class Lab2 {
	public static void main(String[] args) {
		/*
		 * Scanner scanner = new Scanner(System.in); while (true) { // new
		 * Mooncake_Gambling(new int[] { 2, 2, 2, 2, 2, 4 });// cheat new
		 * Mooncake_Gambling(); String i = scanner.nextLine(); if (i.contains("q") ||
		 * i.contains("Q")) break; } System.out.println("See you next time!");
		 * scanner.close();
		 */
		Player p = new Player();
		p.Draw();

	}
}

class Mooncake_Gambling {
	private int dice[] = new int[6];

	void print_result(PrizeType result) {
		if (result == PrizeType.Nothing)
			System.out.println("You got Nothing! Wish you good luck next time!");
		else
			System.out.println("Congratulations! You got a(n) " + result);
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

	Prize() {
		Random r = new Random();
		int dice[] = new int[6];
		for (int i = 0; i < 6; i++)// Summon random points
			dice[i] = r.nextInt(6) + 1;
		print_points(dice);
		Judge_Prize(dice);
	}

	Prize(int dice[]) {
		print_points(dice);
		Judge_Prize(dice);
	}

	void print_points(int dice[]) {
		System.out.print("Points of dices:");
		for (int i = 0; i < 6; i++)
			System.out.print(Integer.toString(dice[i]) + (i == 5 ? "\n" : " "));
	}

	private void Judge_Prize(int dice[]) {
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

	public boolean isZhuangYuan() {
		return this.prize_type != PrizeType.Nothing && this.prize_type != PrizeType.YiXiu
				&& this.prize_type != PrizeType.ErJu && this.prize_type != PrizeType.SiJin
				&& this.prize_type != PrizeType.SanHong && this.prize_type != PrizeType.DuiTang;
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

	public PrizeType getPrizeType() {
		return this.prize_type;
	}
}

class Player {
//	private Map<Prize, Integer> WonPrize = new HashMap<Prize, Integer>();
//	private Map<Prize, Integer> OverFlowPrize = new HashMap<Prize, Integer>();
	public int PrizeNumCount[] = new int[5]; // Prize Num Count
	private Prize ZhuangYuan = null;
	Gaming gaming;

	Player(Gaming gaming) {
		this.gaming = gaming;
	}

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
	}

	void Draw() {
		Prize newPrize = new Prize();
		PrizeClassified newPrizeType = ClassifyPrize(newPrize.getPrizeType());
		if (newPrizeType == PrizeClassified.Nothing)
			return;
		else if (newPrizeType == PrizeClassified.YiXiu && !this.gaming.isPrizeFull(0))
			AddPrize(0);
		else if (newPrizeType == PrizeClassified.ErJu && !this.gaming.isPrizeFull(1))
			AddPrize(1);
		else if (newPrizeType == PrizeClassified.SiJin && !this.gaming.isPrizeFull(2))
			AddPrize(2);
		else if (newPrizeType == PrizeClassified.SanHong && !this.gaming.isPrizeFull(3))
			AddPrize(3);
		else if (newPrizeType == PrizeClassified.DuiTang && !this.gaming.isPrizeFull(4))
			AddPrize(4);

	}

	void AddPrize(int PrizeIndex) {
		++this.PrizeNumCount[PrizeIndex];
		this.gaming.AddPrize(PrizeIndex);
	}

	static String GetChineseName(PrizeType prizetype) {// return Chinese Name(String)
		return PrizeToChinese.get(prizetype);
	}

	static PrizeClassified ClassifyPrize(PrizeType prizetype) {// return classified result
		return PrizeClassify.get(prizetype);
//	}
	}

	class Gaming {
		private Player players[];
		public final int PrizeNumSet[] = new int[] { 32, 16, 8, 4, 2 }; // Prize Num Set
		public int PrizeNumCount[] = new int[5]; // Prize Num Count
		public Prize ZhuangYuan = null;

		Gaming(int PlayerNum) {
			this.players = new Player[PlayerNum];

		}

		public void Start_Game() {
			for (int i = 0; i < this.players.length; i++) {

				i = i == this.players.length ? 0 : i;
			}
		}

		public boolean isPrizeFull(int PrizeIndex) {
			return PrizeNumCount[PrizeIndex] < PrizeNumSet[PrizeIndex];
		}

		public void AddPrize(int PrizeIndex) {
			++PrizeNumCount[PrizeIndex];
		}

//	
//	static bool IsGreater() {

	}
}
