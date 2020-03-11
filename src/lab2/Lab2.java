package lab2;

import java.util.Random;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;

enum PrizeType {// ����ϸ��
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
}// ��16��

enum PrizeClassified {// ��������
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
}// ��10��

public class Lab2 {
	public static void main(String[] args) {
		int PlayerNum;
		try (Scanner scanner = new Scanner(System.in)) {
			System.out.print("��������Ҹ���(6 ~ 10):");
			PlayerNum = scanner.nextInt();
			if (6 <= PlayerNum && PlayerNum <= 10)
				new Gaming(PlayerNum);
			else
				System.out.println("��������Ϊ6 ~ 10��");
		}
//		System.out.println(new Prize(new int[] { 4, 4, 4, 4, 1, 1 })
//				.ZhuangYuan_isGreaterThan(new Prize(new int[] { 2, 2, 2, 2, 2, 4 })));// ������
	}
}

class Prize {
	private int dice_repeat[] = new int[6];// �����ظ�����
	private int max_repeat_time = 0;// ��������ظ�����
	private int pointsum = 0;
	private PrizeType prize_type = null;

	private static Map<PrizeType, Integer> ZhuangYuanWeight = new HashMap<PrizeType, Integer>();// ״ԪȨ��
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
	private static Map<PrizeType, String> PrizeToChinese = new HashMap<PrizeType, String>();// �������
	static {
		PrizeToChinese.put(PrizeType.Nothing, "��");
		PrizeToChinese.put(PrizeType.YiXiu, "һ��");
		PrizeToChinese.put(PrizeType.ErJu, "����");
		PrizeToChinese.put(PrizeType.SiJin, "�Ľ�");
		PrizeToChinese.put(PrizeType.SanHong, "����");
		PrizeToChinese.put(PrizeType.DuiTang, "����");
		PrizeToChinese.put(PrizeType.SiHong, "�ĺ�");
		PrizeToChinese.put(PrizeType.WuZiDengKe, "���ӵǿ�");
		PrizeToChinese.put(PrizeType.WuHong, "���");
		PrizeToChinese.put(PrizeType.LiuBeiHei, "������");
		PrizeToChinese.put(PrizeType.BianDiJin, "��ؽ�");
		PrizeToChinese.put(PrizeType.LiuBeiHong, "������");
		PrizeToChinese.put(PrizeType.ChaJinHua, "״Ԫ���");
		PrizeToChinese.put(PrizeType.SiJinPlusYiXiu, "�Ľ�(��һ��)");
		PrizeToChinese.put(PrizeType.SiJinPlusErJu, "�Ľ�(������)");
		PrizeToChinese.put(PrizeType.WuZiPlusYiXiu, "���ӵǿ�(��һ��)");
	}

	Prize() {// ���캯��
		Random r = new Random();
		int dice[] = new int[6];
		for (int i = 0; i < 6; ++i)// ���������
			dice[i] = r.nextInt(6) + 1;
		print_points(dice);
		Judge_Prize(dice);
	}

	Prize(int dice[]) {// ���캯��(�������)
		print_points(dice);
		Judge_Prize(dice);
	}

	void print_points(int dice[]) {// �������
		System.out.print("Points of dices:");
		for (int i = 0; i < 6; ++i)
			System.out.print(Integer.toString(dice[i]) + (i == 5 ? "\n" : " "));
	}

	private void Judge_Prize(int dice[]) {// �жϽ���
		for (int i = 0; i < 6; ++i) {// ͳ��
			++this.dice_repeat[dice[i] - 1];
			this.pointsum += dice[i];
		}
		for (int i = 0; i < 6; ++i)// �ҳ�����ظ���
			if (this.dice_repeat[i] > this.max_repeat_time)
				this.max_repeat_time = this.dice_repeat[i];
		if (max_repeat_time == 1) {// ���ݳ���ԭ�����е�������һ�α�Ϊ״Ԫ
			this.prize_type = PrizeType.DuiTang;
		} else if (max_repeat_time == 2 || max_repeat_time == 3) {// ����ظ�����Ϊ2��3��
			if (dice_repeat[3] == 0)// �ظ����ֲ�Ϊ4
				this.prize_type = PrizeType.Nothing;
			else if (dice_repeat[3] == 1)// 4����1��
				this.prize_type = PrizeType.YiXiu;
			else if (dice_repeat[3] == 2)// 4����2��
				this.prize_type = PrizeType.ErJu;
			else if (dice_repeat[3] == 3)// 4����3��
				this.prize_type = PrizeType.SanHong;
		} else if (max_repeat_time == 4) {// ����ظ�����Ϊ4��
			if (dice_repeat[3] == 4) {// 4�ظ�����4��
				if (dice_repeat[0] == 2)// 444411
					this.prize_type = PrizeType.ChaJinHua;
				else// ��444411
					this.prize_type = PrizeType.SiHong;
			} else {// ��4�ظ�����4��
				if (dice_repeat[3] == 1)// ��һ��
					this.prize_type = PrizeType.SiJinPlusYiXiu;
				else if (dice_repeat[3] == 2)// ������
					this.prize_type = PrizeType.SiJinPlusErJu;
				else// ɶҲ����
					this.prize_type = PrizeType.SiJin;
			}
		} else if (max_repeat_time == 5) {// ����ظ�����Ϊ5��
			if (dice_repeat[3] == 5)// 4����5��
				this.prize_type = PrizeType.WuHong;
			else {// ���ӵǿ�
				if (dice_repeat[3] == 1)// ��һ��
					this.prize_type = PrizeType.WuZiPlusYiXiu;
				else// ɶҲ����
					this.prize_type = PrizeType.WuZiDengKe;
			}
		} else if (max_repeat_time == 6) {// ����ظ�����Ϊ6��
			if (dice_repeat[0] == 6)// 111111
				this.prize_type = PrizeType.BianDiJin;
			else if (dice_repeat[3] == 6)// 444444
				this.prize_type = PrizeType.LiuBeiHong;
			else// ����
				this.prize_type = PrizeType.LiuBeiHei;
		}
	}

	public boolean isZhuangYuan() {// �ж��Ƿ�Ϊ״Ԫ
		return this.prize_type != PrizeType.Nothing && this.prize_type != PrizeType.YiXiu
				&& this.prize_type != PrizeType.ErJu && this.prize_type != PrizeType.SiJin
				&& this.prize_type != PrizeType.SanHong && this.prize_type != PrizeType.DuiTang;
	}

	public boolean ZhuangYuan_isGreaterThan(Prize cmp) {// �Ƚ�״Ԫ��С
		if (ZhuangYuanWeight.get(this.prize_type) > ZhuangYuanWeight.get(cmp.prize_type))
			return true;
		else if (ZhuangYuanWeight.get(this.prize_type) < ZhuangYuanWeight.get(cmp.prize_type))// �ȱȽ�һ��Ȩ��
			return false;
		else {// ͬȨ�ضԱ�
			if (this.prize_type == PrizeType.SiHong || this.prize_type == PrizeType.WuHong
					|| this.prize_type == PrizeType.LiuBeiHei)// �ĺ졢��졢������ֻ��Ҫ�Ƚ��ܺ�
				return this.pointsum > cmp.pointsum;// ��ȵ��������false
			else if (this.prize_type == PrizeType.BianDiJin || this.prize_type == PrizeType.LiuBeiHong
					|| this.prize_type == PrizeType.ChaJinHua)// ��ؽ��������졢״Ԫ���ֻ�й̶�������
				return false;// �ȵ��ȵã�����false
			else {// ���ӵǿƼ�Ƚ�
				int this_appear_once = 0, this_appear_fifth = 0, cmp_appear_once = 0, cmp_appear_fifth = 0;
				for (int i = 0; i < 6; ++i) {// ���forѭ����ɸѡ���ֱ��ظ�1�κ�5�εĵ���
					if (this.dice_repeat[i] == 1)
						this_appear_once = i;
					else if (this.dice_repeat[i] == 5)
						this_appear_fifth = i;
					if (cmp.dice_repeat[i] == 1)
						cmp_appear_once = i;
					else if (cmp.dice_repeat[i] == 5)
						cmp_appear_fifth = i;
				}
				if (this_appear_once != cmp_appear_once)// ����ظ�һ�εĲ����
					return this_appear_once > cmp_appear_once;// ���ȱȽ��ظ�1�ε�
				else
					return this_appear_fifth > cmp_appear_fifth;// ����Ƚ��ظ�5�ε�
			}
		}
	}

	public PrizeType getPrizeType() {// PrizeType��getter
		return this.prize_type;
	}

	public String toString() {// ��дtoString
		return PrizeToChinese.get(this.prize_type);
	}
}

class Player {
	private int PrizeNumCount[] = new int[5]; // �洢��һ�ý�����(һ�㣬���٣��Ľ������졢����)
	Gaming gaming;// ָ��gaming���������

	Player(Gaming gaming) {// ��ʼ��
		this.gaming = gaming;
	}

	void Draw() {// �齱
		Prize newPrize = new Prize();
		System.out.println(newPrize.getPrizeType());
//		System.out.println(newPrizeType);// ������
		// ���¸����жϣ�Ŀ���Ǽ����Ƿ�����������δ�����¼����
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

	private void AddPrize(int PrizeIndex) {// ���ӽ����¼��״Ԫ���⣩
		++this.PrizeNumCount[PrizeIndex];
		this.gaming.AddPrize(PrizeIndex);
	}

	public int[] getPrizeNumCount() {// PrizeNumCount��getter
		return this.PrizeNumCount;
	}
}

class Gaming {
	private Player players[];
	public final int PrizeNumSet[] = new int[] { 32, 16, 8, 4, 2 }; // һ�㣬���٣��Ľ������졢���ø����������ƣ�״Ԫֻ��һ����
	public int PrizeNumCount[] = new int[5]; // ��������ͳ��
	int CurrentPlayerIndex = 0;// ��ǰ��ұ��
	int ZhuangYuanBelongIndex = -1;// ״Ԫ����
	private Prize ZhuangYuan = null;// ��¼��ǰ״Ԫ

	Gaming(int PlayerNum) {
		this.players = new Player[PlayerNum];
		for (int i = 0; i < PlayerNum; ++i)
			this.players[i] = new Player(this);// ��ʼ��player
		this.Start_Game();// ��ʼ��Ϸ
	}

	private void Start_Game() {// ��ʼ��Ϸ
		int count = 0;// ����ͳ����Ϸ���д���
		while (this.CurrentPlayerIndex < this.players.length) {
			this.players[this.CurrentPlayerIndex].Draw();
			if (this.isAllPrizeFull())// ���н�����������״Ԫ���������Ϸ
				break;
			++this.CurrentPlayerIndex;
			this.CurrentPlayerIndex = this.CurrentPlayerIndex == this.players.length ? 0 : this.CurrentPlayerIndex;// ѭ��
			++count;
		}
		System.out.println("�ܴ���:" + count);
		this.End_Game();// ������Ϸ
	}

	private void End_Game() {// ������Ϸ�Ľ���
		for (int i = 0; i < this.players.length; ++i) {// ѭ�����������ҽ���
			System.out.println("��" + i + "���������ý���:");
			System.out.println("һ��" + this.players[i].getPrizeNumCount()[0] + "��");
			System.out.println("����" + this.players[i].getPrizeNumCount()[1] + "��");
			System.out.println("�Ľ�" + this.players[i].getPrizeNumCount()[2] + "��");
			System.out.println("����" + this.players[i].getPrizeNumCount()[3] + "��");
			System.out.println("����" + this.players[i].getPrizeNumCount()[4] + "��");
			if (i == this.ZhuangYuanBelongIndex)
				System.out.println("����״Ԫ����������״ԪΪ��" + this.ZhuangYuan);
			System.out.println();
		}
		System.out.println("����״ԪΪ" + this.ZhuangYuanBelongIndex + "����ң�����״ԪΪ��" + this.ZhuangYuan);// ������״Ԫ
	}

	public boolean isPrizeFull(int PrizeIndex) {// �ж�ĳһ�������Ƿ�����
		return PrizeNumCount[PrizeIndex] == PrizeNumSet[PrizeIndex];
	}

	private boolean isAllPrizeFull() {// �ж��Ƿ����н�������
		boolean result = true;
		for (int i = 0; i < 5; ++i)
			result &= this.isPrizeFull(i);
		result &= this.ZhuangYuan != null;
		return result;
	}

	public void AddPrize(int PrizeIndex) {// PrizeNumCount��setter
		++PrizeNumCount[PrizeIndex];
	}

	public void AddZhuangYuan(Prize ZhuangYuan) {
		if (this.ZhuangYuanBelongIndex == -1 || // ״Ԫδ����
				ZhuangYuan.ZhuangYuan_isGreaterThan(this.ZhuangYuan)) {// ��״Ԫ����
			this.ZhuangYuanBelongIndex = this.CurrentPlayerIndex;
			this.ZhuangYuan = ZhuangYuan;// ����״Ԫ��Ϣ
		}
	}
}