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

public class Lab2 {
	public static void main(String[] args) {
//		System.out.println(new Prize(new int[] {4,4,4,4,1,1}).
//				ZhuangYuan_isGreaterThan(new Prize(new int[] {2,2,2,2,2,4})));
		new Gaming(6);
	}
}


class Prize {
	private int dice_repeat[] = new int[6];// save repeat counts for each point(1~6)
	private int max_repeat_time = 0;// the max repeat time for each num
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
	private static Map<PrizeType, String> PrizeToChinese = new HashMap<PrizeType, String>();
	static {
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
	}

	Prize() {
		Random r = new Random();
		int dice[] = new int[6];
		for (int i = 0; i < 6; ++i)// Summon random points
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
		for (int i = 0; i < 6; ++i)
			System.out.print(Integer.toString(dice[i]) + (i == 5 ? "\n" : " "));
	}

	private void Judge_Prize(int dice[]) {
		for (int i = 0; i < 6; ++i) {
			++this.dice_repeat[dice[i] - 1];
			this.pointsum += dice[i];
		}
		for (int i = 0; i < 6; ++i)
			if (this.dice_repeat[i] > this.max_repeat_time)
				this.max_repeat_time = this.dice_repeat[i];
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
				return this.pointsum > cmp.pointsum;// Self is Smaller when equal
			else if (this.prize_type == PrizeType.BianDiJin || this.prize_type == PrizeType.LiuBeiHong
					|| this.prize_type == PrizeType.ChaJinHua)
				return false;// Self is Smaller when equal
			else {// CMP between WuZi
				int this_appear_once = 0, this_appear_fifth = 0, cmp_appear_once = 0, cmp_appear_fifth = 0;
				for (int i = 0; i < 6; ++i) {
					if (this.dice_repeat[i] == 1)
						this_appear_once = i;
					else if (this.dice_repeat[i] == 5)
						this_appear_fifth = i;
					if (cmp.dice_repeat[i] == 1)
						cmp_appear_once = i;
					else if (cmp.dice_repeat[i] == 5)
						cmp_appear_fifth = i;
				}
				if (this_appear_once == cmp_appear_once)
					return this_appear_fifth > cmp_appear_fifth;
				else
					return this_appear_once > cmp_appear_once;
			}
		}
	}

	public PrizeType getPrizeType() {
		return this.prize_type;
	}

	public String toString() {// return Chinese Name(String)
		return PrizeToChinese.get(this.prize_type);
	}
}

class Player {
//	private Map<Prize, Integer> WonPrize = new HashMap<Prize, Integer>();
//	private Map<Prize, Integer> OverFlowPrize = new HashMap<Prize, Integer>();
	public int PrizeNumCount[] = new int[5]; // Prize Num Count (YiXiu, ErJu, SiJin, SanHong, DuiTang)
	Gaming gaming;

	Player(Gaming gaming) {
		this.gaming = gaming;
	}

	private static Map<PrizeType, PrizeClassified> PrizeClassify = new HashMap<PrizeType, PrizeClassified>();
	static {// 初始化map
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
		System.out.println(newPrize.getPrizeType());
		PrizeClassified newPrizeType = ClassifyPrize(newPrize.getPrizeType());
		// System.out.println(newPrizeType);
		if (newPrizeType == PrizeClassified.Nothing)
			return;
		else if (newPrizeType == PrizeClassified.YiXiu) {
			if (this.gaming.isPrizeFull(0))
				return;
			AddPrize(0);
		} else if (newPrizeType == PrizeClassified.ErJu) {
			if (this.gaming.isPrizeFull(1))
				return;
			AddPrize(1);
		} else if (newPrizeType == PrizeClassified.SiJin) {
			if (this.gaming.isPrizeFull(2))
				return;
			AddPrize(2);
		} else if (newPrizeType == PrizeClassified.SanHong) {
			if (this.gaming.isPrizeFull(3))
				return;
			AddPrize(3);
		} else if (newPrizeType == PrizeClassified.DuiTang) {
			if (this.gaming.isPrizeFull(4))
				return;
			AddPrize(4);
		} else if (newPrizeType == PrizeClassified.SiJinPlusYiXiu) {
			if (!this.gaming.isPrizeFull(0))
				AddPrize(0);
			if (!this.gaming.isPrizeFull(2))
				AddPrize(2);
		} else if (newPrizeType == PrizeClassified.SiJinPlusErJu) {
			if (!this.gaming.isPrizeFull(1))
				AddPrize(1);
			if (!this.gaming.isPrizeFull(2))
				AddPrize(2);
		} else if (newPrizeType == PrizeClassified.WuZiPlusYiXiu) {
			if (!this.gaming.isPrizeFull(0))
				AddPrize(0);
			this.gaming.AddZhuangYuan(newPrize);
		} else
			this.gaming.AddZhuangYuan(newPrize);
	}

	private void AddPrize(int PrizeIndex) {
		++this.PrizeNumCount[PrizeIndex];
		this.gaming.AddPrize(PrizeIndex);
	}

	static PrizeClassified ClassifyPrize(PrizeType prizetype) {// return classified result
		return PrizeClassify.get(prizetype);
	}
}

class Gaming {
	private Player players[];
	public final int PrizeNumSet[] = new int[] { 32, 16, 8, 4, 2 }; // Prize Num Set (YiXiu, ErJu, SiJin, SanHong,
																	// DuiTang)
	public int PrizeNumCount[] = new int[5]; // Prize Num Count
	int CurrentPlayerIndex = 0;
	int ZhuangYuanBelongIndex = -1;
	private Prize ZhuangYuan = null;

	Gaming(int PlayerNum) {
		this.players = new Player[PlayerNum];
		for (int i = 0; i < PlayerNum; ++i)
			this.players[i] = new Player(this);
		this.Start_Game();
	}

	private void Start_Game() {
		int count = 0;
		while (this.CurrentPlayerIndex < this.players.length) {
			this.players[this.CurrentPlayerIndex].Draw();
			if (this.isAllPrizeFull())
				break;
			++this.CurrentPlayerIndex;
			this.CurrentPlayerIndex = this.CurrentPlayerIndex == this.players.length ? 0 : this.CurrentPlayerIndex;
			++count;
		}
		System.out.println("总次数:" + count);
		this.End_Game();
	}

	private void End_Game() {
		for (int i = 0; i < this.players.length; ++i) {

		}
		System.out.println("最终状元为" + this.ZhuangYuanBelongIndex + "号玩家，所得状元为：" + this.ZhuangYuan);
	}

	public boolean isPrizeFull(int PrizeIndex) {
		return PrizeNumCount[PrizeIndex] == PrizeNumSet[PrizeIndex];
	}

	private boolean isAllPrizeFull() {
		boolean result = true;
		for (int i = 0; i < 5; ++i)
			result &= this.isPrizeFull(i);
		result &= this.ZhuangYuan != null;
		return result;
	}

	public void AddPrize(int PrizeIndex) {
		++PrizeNumCount[PrizeIndex];
	}

	public void AddZhuangYuan(Prize ZhuangYuan) {
		if (this.ZhuangYuanBelongIndex == -1 || // if ZhuangYuan not appear
				ZhuangYuan.ZhuangYuan_isGreaterThan(this.ZhuangYuan)) {// if New ZhuangYuan Greater
			this.ZhuangYuanBelongIndex = this.CurrentPlayerIndex;
			this.ZhuangYuan = ZhuangYuan;
		}
	}
}