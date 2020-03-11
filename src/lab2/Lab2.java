package lab2;

import java.util.Random;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;

enum PrizeType {// 奖项细分
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
}// 共16种

enum PrizeClassified {// 奖项整理
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
}// 共10种

public class Lab2 {
	public static void main(String[] args) {
		int PlayerNum;
		try (Scanner scanner = new Scanner(System.in)) {
			System.out.print("请输入玩家个数(6 ~ 10):");
			PlayerNum = scanner.nextInt();
			if (6 <= PlayerNum && PlayerNum <= 10)
				new Gaming(PlayerNum);
			else
				System.out.println("人数必须为6 ~ 10人");
		}
//		System.out.println(new Prize(new int[] { 4, 4, 4, 4, 1, 1 })
//				.ZhuangYuan_isGreaterThan(new Prize(new int[] { 2, 2, 2, 2, 2, 4 })));// 测试用
	}
}

class Prize {
	private int dice_repeat[] = new int[6];// 骰子重复次数
	private int max_repeat_time = 0;// 骰子最大重复次数
	private int pointsum = 0;
	private PrizeType prize_type = null;

	private static Map<PrizeType, Integer> ZhuangYuanWeight = new HashMap<PrizeType, Integer>();// 状元权重
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
	private static Map<PrizeType, String> PrizeToChinese = new HashMap<PrizeType, String>();// 中文输出
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
		PrizeToChinese.put(PrizeType.ChaJinHua, "状元插金花");
		PrizeToChinese.put(PrizeType.SiJinPlusYiXiu, "四进(带一秀)");
		PrizeToChinese.put(PrizeType.SiJinPlusErJu, "四进(带二举)");
		PrizeToChinese.put(PrizeType.WuZiPlusYiXiu, "五子登科(带一秀)");
	}

	Prize() {// 构造函数
		Random r = new Random();
		int dice[] = new int[6];
		for (int i = 0; i < 6; ++i)// 随机数生成
			dice[i] = r.nextInt(6) + 1;
		print_points(dice);
		Judge_Prize(dice);
	}

	Prize(int dice[]) {// 构造函数(调试入口)
		print_points(dice);
		Judge_Prize(dice);
	}

	void print_points(int dice[]) {// 输出点数
		System.out.print("Points of dices:");
		for (int i = 0; i < 6; ++i)
			System.out.print(Integer.toString(dice[i]) + (i == 5 ? "\n" : " "));
	}

	private void Judge_Prize(int dice[]) {// 判断奖项
		for (int i = 0; i < 6; ++i) {// 统计
			++this.dice_repeat[dice[i] - 1];
			this.pointsum += dice[i];
		}
		for (int i = 0; i < 6; ++i)// 找出最大重复数
			if (this.dice_repeat[i] > this.max_repeat_time)
				this.max_repeat_time = this.dice_repeat[i];
		if (max_repeat_time == 1) {// 根据抽屉原理，所有点数出现一次必为状元
			this.prize_type = PrizeType.DuiTang;
		} else if (max_repeat_time == 2 || max_repeat_time == 3) {// 最大重复出现为2或3次
			if (dice_repeat[3] == 0)// 重复数字不为4
				this.prize_type = PrizeType.Nothing;
			else if (dice_repeat[3] == 1)// 4出现1次
				this.prize_type = PrizeType.YiXiu;
			else if (dice_repeat[3] == 2)// 4出现2次
				this.prize_type = PrizeType.ErJu;
			else if (dice_repeat[3] == 3)// 4出现3次
				this.prize_type = PrizeType.SanHong;
		} else if (max_repeat_time == 4) {// 最大重复出现为4次
			if (dice_repeat[3] == 4) {// 4重复出现4次
				if (dice_repeat[0] == 2)// 444411
					this.prize_type = PrizeType.ChaJinHua;
				else// 非444411
					this.prize_type = PrizeType.SiHong;
			} else {// 非4重复出现4次
				if (dice_repeat[3] == 1)// 带一秀
					this.prize_type = PrizeType.SiJinPlusYiXiu;
				else if (dice_repeat[3] == 2)// 带二举
					this.prize_type = PrizeType.SiJinPlusErJu;
				else// 啥也不带
					this.prize_type = PrizeType.SiJin;
			}
		} else if (max_repeat_time == 5) {// 最大重复出现为5次
			if (dice_repeat[3] == 5)// 4出现5次
				this.prize_type = PrizeType.WuHong;
			else {// 五子登科
				if (dice_repeat[3] == 1)// 带一秀
					this.prize_type = PrizeType.WuZiPlusYiXiu;
				else// 啥也不带
					this.prize_type = PrizeType.WuZiDengKe;
			}
		} else if (max_repeat_time == 6) {// 最大重复出现为6次
			if (dice_repeat[0] == 6)// 111111
				this.prize_type = PrizeType.BianDiJin;
			else if (dice_repeat[3] == 6)// 444444
				this.prize_type = PrizeType.LiuBeiHong;
			else// 其它
				this.prize_type = PrizeType.LiuBeiHei;
		}
	}

	public boolean isZhuangYuan() {// 判断是否为状元
		return this.prize_type != PrizeType.Nothing && this.prize_type != PrizeType.YiXiu
				&& this.prize_type != PrizeType.ErJu && this.prize_type != PrizeType.SiJin
				&& this.prize_type != PrizeType.SanHong && this.prize_type != PrizeType.DuiTang;
	}

	public boolean ZhuangYuan_isGreaterThan(Prize cmp) {// 比较状元大小
		if (ZhuangYuanWeight.get(this.prize_type) > ZhuangYuanWeight.get(cmp.prize_type))
			return true;
		else if (ZhuangYuanWeight.get(this.prize_type) < ZhuangYuanWeight.get(cmp.prize_type))// 先比较一波权重
			return false;
		else {// 同权重对比
			if (this.prize_type == PrizeType.SiHong || this.prize_type == PrizeType.WuHong
					|| this.prize_type == PrizeType.LiuBeiHei)// 四红、五红、六杯黑只需要比较总和
				return this.pointsum > cmp.pointsum;// 相等的情况返回false
			else if (this.prize_type == PrizeType.BianDiJin || this.prize_type == PrizeType.LiuBeiHong
					|| this.prize_type == PrizeType.ChaJinHua)// 遍地锦、六杯红、状元插金花只有固定点数型
				return false;// 先到先得，返回false
			else {// 五子登科间比较
				int this_appear_once = 0, this_appear_fifth = 0, cmp_appear_once = 0, cmp_appear_fifth = 0;
				for (int i = 0; i < 6; ++i) {// 这段for循环先筛选出分别重复1次和5次的点数
					if (this.dice_repeat[i] == 1)
						this_appear_once = i;
					else if (this.dice_repeat[i] == 5)
						this_appear_fifth = i;
					if (cmp.dice_repeat[i] == 1)
						cmp_appear_once = i;
					else if (cmp.dice_repeat[i] == 5)
						cmp_appear_fifth = i;
				}
				if (this_appear_once != cmp_appear_once)// 如果重复一次的不相等
					return this_appear_once > cmp_appear_once;// 优先比较重复1次的
				else
					return this_appear_fifth > cmp_appear_fifth;// 而后比较重复5次的
			}
		}
	}

	public PrizeType getPrizeType() {// PrizeType的getter
		return this.prize_type;
	}

	public String toString() {// 重写toString
		return PrizeToChinese.get(this.prize_type);
	}
}

class Player {
	private int PrizeNumCount[] = new int[5]; // 存储玩家获得奖项数(一秀，二举，四进、三红、对堂)
	Gaming gaming;// 指向gaming对象的引用

	Player(Gaming gaming) {// 初始化
		this.gaming = gaming;
	}

	void Draw() {// 抽奖
		Prize newPrize = new Prize();
		System.out.println(newPrize.getPrizeType());
//		System.out.println(newPrizeType);// 测试用
		// 以下各种判断，目的是检验是否奖项已满，若未满则记录奖项
		switch (newPrize.getPrizeType()) {
		case Nothing:
			return;
		case YiXiu:
			if (this.gaming.isPrizeFull(0))
				return;
			AddPrize(0);
			break;
		case ErJu:
			if (this.gaming.isPrizeFull(1))
				return;
			AddPrize(1);
			break;
		case SiJin:
			if (this.gaming.isPrizeFull(2))
				return;
			AddPrize(2);
			break;
		case SanHong:
			if (this.gaming.isPrizeFull(3))
				return;
			AddPrize(3);
			break;
		case DuiTang:
			if (this.gaming.isPrizeFull(4))
				return;
			AddPrize(4);
			break;
		case SiJinPlusYiXiu:
			if (!this.gaming.isPrizeFull(0))
				AddPrize(0);
			if (!this.gaming.isPrizeFull(2))
				AddPrize(2);
			break;
		case SiJinPlusErJu:
			if (!this.gaming.isPrizeFull(1))
				AddPrize(1);
			if (!this.gaming.isPrizeFull(2))
				AddPrize(2);
			break;
		case WuZiPlusYiXiu:
			if (!this.gaming.isPrizeFull(0))
				AddPrize(0);
			this.gaming.AddZhuangYuan(newPrize);
			break;
		default:
			this.gaming.AddZhuangYuan(newPrize);
			break;
		}
	}

	private void AddPrize(int PrizeIndex) {// 增加奖项记录（状元以外）
		++this.PrizeNumCount[PrizeIndex];
		this.gaming.AddPrize(PrizeIndex);
	}

	public int[] getPrizeNumCount() {// PrizeNumCount的getter
		return this.PrizeNumCount;
	}
}

class Gaming {
	private Player players[];
	public final int PrizeNumSet[] = new int[] { 32, 16, 8, 4, 2 }; // 一秀，二举，四进、三红、对堂个数上限限制（状元只有一个）
	public int PrizeNumCount[] = new int[5]; // 奖项数量统计
	int CurrentPlayerIndex = 0;// 当前玩家编号
	int ZhuangYuanBelongIndex = -1;// 状元归属
	private Prize ZhuangYuan = null;// 记录当前状元

	Gaming(int PlayerNum) {
		this.players = new Player[PlayerNum];
		for (int i = 0; i < PlayerNum; ++i)
			this.players[i] = new Player(this);// 初始化player
		this.Start_Game();// 开始游戏
	}

	private void Start_Game() {// 开始游戏
		int count = 0;// 用于统计游戏进行次数
		while (this.CurrentPlayerIndex < this.players.length) {
			this.players[this.CurrentPlayerIndex].Draw();
			if (this.isAllPrizeFull())// 所有奖项满（包含状元）则结束游戏
				break;
			++this.CurrentPlayerIndex;
			this.CurrentPlayerIndex = this.CurrentPlayerIndex == this.players.length ? 0 : this.CurrentPlayerIndex;// 循环
			++count;
		}
		System.out.println("总次数:" + count);
		this.End_Game();// 结束游戏
	}

	private void End_Game() {// 结束游戏的结算
		for (int i = 0; i < this.players.length; ++i) {// 循环输出所有玩家奖项
			System.out.println("第" + i + "号玩家所获得奖项:");
			System.out.println("一秀" + this.players[i].getPrizeNumCount()[0] + "个");
			System.out.println("二举" + this.players[i].getPrizeNumCount()[1] + "个");
			System.out.println("四进" + this.players[i].getPrizeNumCount()[2] + "个");
			System.out.println("三红" + this.players[i].getPrizeNumCount()[3] + "个");
			System.out.println("对堂" + this.players[i].getPrizeNumCount()[4] + "个");
			if (i == this.ZhuangYuanBelongIndex)
				System.out.println("最终状元得主，所得状元为：" + this.ZhuangYuan);
			System.out.println();
		}
		System.out.println("最终状元为" + this.ZhuangYuanBelongIndex + "号玩家，所得状元为：" + this.ZhuangYuan);// 最后输出状元
	}

	public boolean isPrizeFull(int PrizeIndex) {// 判断某一个奖项是否已满
		return PrizeNumCount[PrizeIndex] == PrizeNumSet[PrizeIndex];
	}

	private boolean isAllPrizeFull() {// 判断是否所有奖项已满
		boolean result = true;
		for (int i = 0; i < 5; ++i)
			result &= this.isPrizeFull(i);
		result &= this.ZhuangYuan != null;
		return result;
	}

	public void AddPrize(int PrizeIndex) {// PrizeNumCount的setter
		++PrizeNumCount[PrizeIndex];
	}

	public void AddZhuangYuan(Prize ZhuangYuan) {
		if (this.ZhuangYuanBelongIndex == -1 || // 状元未出现
				ZhuangYuan.ZhuangYuan_isGreaterThan(this.ZhuangYuan)) {// 新状元更大
			this.ZhuangYuanBelongIndex = this.CurrentPlayerIndex;
			this.ZhuangYuan = ZhuangYuan;// 更新状元信息
		}
	}
}