package lab2;

import java.util.Random;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;

enum Prize {// Prize Types
	Nothing, // �׸�
	YiXiu, // һ�� һ����
	ErJu, // ���� ������
	SiJin, // �Ľ� �ĸ���ͬ�������⣩
	SanHong, // ���� ������
	DuiTang, // ���� 123456
	SiHong, // �ĺ� �ĸ���
	WuZiDengKe, // ���ӵǿ� �����ͬ�������⣩
	WuHong, // ��� �����
	LiuBeiHei, // ������ ������ͬ�����ĺ�һ�⣩
	BianDiJin, // ��ؽ� ����һ
	LiuBeiHong, // ������ ������
	ChaJinHua, // ��� 114444
	// ������
	SiJinPlusYiXiu, // �Ľ���һ��
	SiJinPlusErJu, // �Ľ�������
	WuZiPlusYiXiu// ���Ӵ�һ��
}// Total 16 Types

//Prize To PrizeClassified: 1 key to 1 value, can use Map

enum PrizeClassified {// Prize Types Classified, intermediate enum of Prize and PrizeClassified2
	Nothing, // �׸�
	YiXiu, // һ��
	ErJu, // ����
	SiJin, // �Ľ�
	SanHong, // ����
	DuiTang, // ����
	ZhuangYuan, // ״Ԫ
	// ������
	SiJinPlusYiXiu, // �Ľ���һ��
	SiJinPlusErJu, // �Ľ�������
	WuZiPlusYiXiu// ���Ӵ�һ��
}// Total 10 Types

//PrizeClassified to PrizeClassified2: 1 key to 1 or 2 value, can't use Map, use switch...case

enum PrizeClassified2 {// Prize Types Classified 2, for storage 6 types of prize
	YiXiu, // һ��
	ErJu, // ����
	SiJin, // �Ľ�
	SanHong, // ����
	DuiTang, // ����
	ZhuangYuan// ״Ԫ
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

	Prize judge_points() {
		int repeat[] = new int[6];
		for (int i = 0; i < 6; i++)
			repeat[dice[i] - 1]++;
		int max_repeat_times = 0;
		for (int i = 0; i < 6; i++)
			max_repeat_times = Math.max(max_repeat_times, repeat[i]);
		if (max_repeat_times == 1) {
			return Prize.DuiTang;
		} else if (max_repeat_times == 2 || max_repeat_times == 3) {
			if (repeat[3] == 0)
				return Prize.Nothing;
			else if (repeat[3] == 1)
				return Prize.YiXiu;
			else if (repeat[3] == 2)
				return Prize.ErJu;
			else if (repeat[3] == 3)
				return Prize.SanHong;
		} else if (max_repeat_times == 4) {
			if (repeat[3] == 4) {
				if (repeat[0] == 2)
					return Prize.ChaJinHua;
				else
					return Prize.SiHong;
			} else {
				if (repeat[3] == 1)
					return Prize.SiJinPlusYiXiu;
				else if (repeat[3] == 2)
					return Prize.SiJinPlusErJu;
				else
					return Prize.SiJin;
			}
		} else if (max_repeat_times == 5) {
			if (repeat[3] == 5)
				return Prize.WuHong;
			else {
				if (repeat[3] == 1)
					return Prize.WuZiPlusYiXiu;
				else
					return Prize.WuZiDengKe;
			}
		} else if (max_repeat_times == 6) {
			if (repeat[0] == 6)
				return Prize.BianDiJin;
			else if (repeat[3] == 6)
				return Prize.LiuBeiHong;
			else
				return Prize.LiuBeiHei;
		}
		return null;
	}

	void print_result(Prize result) {
		if (result == Prize.Nothing)
			System.out.println("You got Nothing! Wish you good luck next time!");
		else
			System.out.println("Congratulations! You got a(n) " + result);
	}
}

class Player {
//	private Map<Prize, Integer> WonPrize = new HashMap<Prize, Integer>();
//	private Map<Prize, Integer> OverFlowPrize = new HashMap<Prize, Integer>();
	private int WonPrize[] = new int[Prize.values().length];
	private int WonPrizeClassified2[] = new int[PrizeClassified2.values().length];
	private int OverFlowPrize[] = new int[Prize.values().length];

	void WonPrize(Prize prize) {

	}

	int getPrizeNum(Prize prize) {
		return 0;
	}
}

class Gaming {
	private int PlayerNum;

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

	private static Map<Prize, String> PrizeToChinese = new HashMap<Prize, String>();
	private static Map<Prize, PrizeClassified> PrizeClassify = new HashMap<Prize, PrizeClassified>();
	private static Map<Prize, Integer> ZhuangYuanWeight = new HashMap<Prize, Integer>();
	static {// ��ʼ��map
		PrizeToChinese.put(Prize.Nothing, "��");
		PrizeToChinese.put(Prize.YiXiu, "һ��");
		PrizeToChinese.put(Prize.ErJu, "����");
		PrizeToChinese.put(Prize.SiJin, "�Ľ�");
		PrizeToChinese.put(Prize.SanHong, "����");
		PrizeToChinese.put(Prize.DuiTang, "����");
		PrizeToChinese.put(Prize.SiHong, "�ĺ�");
		PrizeToChinese.put(Prize.WuZiDengKe, "���ӵǿ�");
		PrizeToChinese.put(Prize.WuHong, "���");
		PrizeToChinese.put(Prize.LiuBeiHei, "������");
		PrizeToChinese.put(Prize.BianDiJin, "��ؽ�");
		PrizeToChinese.put(Prize.LiuBeiHong, "������");
		PrizeToChinese.put(Prize.ChaJinHua, "���");
		PrizeToChinese.put(Prize.SiJinPlusYiXiu, "�Ľ���һ��");
		PrizeToChinese.put(Prize.SiJinPlusErJu, "�Ľ�������");
		PrizeToChinese.put(Prize.WuZiPlusYiXiu, "���ӵǿƴ�һ��");
		// Map<Prize,PrizeClassified> PrizeClassify
		PrizeClassify.put(Prize.Nothing, PrizeClassified.Nothing);
		PrizeClassify.put(Prize.YiXiu, PrizeClassified.YiXiu);
		PrizeClassify.put(Prize.ErJu, PrizeClassified.ErJu);
		PrizeClassify.put(Prize.SiJin, PrizeClassified.SiJin);
		PrizeClassify.put(Prize.SanHong, PrizeClassified.SanHong);
		PrizeClassify.put(Prize.DuiTang, PrizeClassified.DuiTang);
		PrizeClassify.put(Prize.SiHong, PrizeClassified.ZhuangYuan);
		PrizeClassify.put(Prize.WuZiDengKe, PrizeClassified.ZhuangYuan);
		PrizeClassify.put(Prize.WuHong, PrizeClassified.ZhuangYuan);
		PrizeClassify.put(Prize.LiuBeiHei, PrizeClassified.ZhuangYuan);
		PrizeClassify.put(Prize.BianDiJin, PrizeClassified.ZhuangYuan);
		PrizeClassify.put(Prize.LiuBeiHong, PrizeClassified.ZhuangYuan);
		PrizeClassify.put(Prize.ChaJinHua, PrizeClassified.ZhuangYuan);
		PrizeClassify.put(Prize.SiJinPlusYiXiu, PrizeClassified.SiJinPlusYiXiu);
		PrizeClassify.put(Prize.SiJinPlusErJu, PrizeClassified.SiJinPlusErJu);
		PrizeClassify.put(Prize.WuZiPlusYiXiu, PrizeClassified.WuZiPlusYiXiu);
		// Map<Prize,Integer> ZhuangYuanWeight
		
	}

	Gaming(int PlayerNum) {
		this.PlayerNum = PlayerNum;
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
