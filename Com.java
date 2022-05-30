package omok;

//��ǻ�� �� ���� 3 �϶� �����α�
//��ǻ�� �� 5ĭ üũ �ؼ� �Ѱ� ��� ������ �α�
import java.awt.Cursor;

import javax.swing.ImageIcon;

public class Com {
	int cPattern4 = 1000;
	int pattern4 = 500;
	int pattern3 = 200;
	int pattern2 = 30;
	int pattern2_1 = 20;
	int[][] comArr = new int[19][19]; // ��ǻ�� �ӽ� ������
	static int[][] comRealArr = new int[19][19]; // ��ǻ�� ���� ������
	static int[][] weight = new int[19][19]; // ����ġ ���� ��
	int l, h, x, y, fL, fH, weightCheck = -500;
	boolean pWin = false;
	boolean weightFixed = false;
	int[][] firstChoice = new int[19][19];
	int stoneCnt;

	public void comChoiceStone() {
		int cnt = 0, cCnt = 0, turnCnt = 0, selNum = OmokClient.selNum, nSelNum = OmokClient.nSelNum, temp;
		stoneCnt = 0;
		// ���� ��ǻ�� ���� �ӽ� �ǿ� ����
		for (int i = 0; i < 19; i++) {
			for (int j = 0; j < 19; j++) {
				if (comRealArr[i][j] != 0) {
					stoneCnt++;
				}
				comArr[i][j] = comRealArr[i][j];
				firstChoice[i][j] = 0;
			}
		}
		for (int i = 0; i < 19; i++) {
			for (int j = 0; j < 19; j++) {
				System.out.printf("%2d", comRealArr[i][j]);
			}
			System.out.println();
		}
		System.out.println("comRealArr");
		while (true) {
			pWin = false;
			weightFixed = false;
			turnCnt++;
			// ����ġ �ʱ�ȭ
			for (int i = 0; i < 19; i++) {
				for (int j = 0; j < 19; j++) {
					if (firstChoice[i][j] != 0) {
						weight[i][j] = firstChoice[i][j];
					} else {
						weight[i][j] = 0;
					}
				}
			}
			// ����ġ ����
			for (int i = 0; i < 19; i++) {
				for (int j = 0; j < 19; j++) {
					if (comArr[i][j] == nSelNum) {
						if (i == 0) {
							if (j == 0 || j == 18) {
								weight[i + 1][j]--;
							} else {
								weight[i + 1][j - 1]--;
								weight[i + 1][j]--;
								weight[i + 1][j + 1]--;
							}
						} else if (i == 18) {
							if (j == 0 || j == 18) {
								if (j == 0 || j == 18) {
									weight[i - 1][j]--;
								} else {
									weight[i - 1][j - 1]--;
									weight[i - 1][j]--;
									weight[i - 1][j + 1]--;
								}
							}
						} else if (j == 0) {
							if (i == 0 || i == 18) {
								weight[i][j + 1]--;
							} else {
								weight[i - 1][j + 1]--;
								weight[i][j + 1]--;
								weight[i + 1][j + 1]--;
							}
						} else if (j == 18) {
							if (i == 0 || i == 18) {
								weight[i][j - 1]--;
							} else {
								weight[i - 1][j - 1]--;
								weight[i][j - 1]--;
								weight[i + 1][j - 1]--;
							}
						} else {
							weight[i - 1][j - 1]--; // ����
							weight[i - 1][j]--; // ��
							weight[i - 1][j + 1]--; // ����
							weight[i][j + 1]--; // ��
							weight[i + 1][j + 1]--; // ����
							weight[i + 1][j]--; // ��
							weight[i + 1][j - 1]--; // �޾�
							weight[i][j - 1]--; // ��
						}
						if (i < 16) { // ���� 4
							cnt = 0;
							y = i;
							x = j;
							for (int k = 0; k < 4; k++) {
								if (comArr[y++][x] == nSelNum) {
									cnt++;
									if (cnt == 4) {
										if (i == 0) {
											weight[y][x] += pattern4;
										} else if (i == 15) {
											weight[i - 1][j] += pattern4;
										} else {
											weight[y][x] += pattern4;
											weight[i - 1][j] += pattern4;
										}
										cnt = 0;
									}
								} else {
									cnt = 0;
								}
							}
						}
						if (j < 16) { // ���� 4
							cnt = 0;
							y = i;
							x = j;
							for (int k = 0; k < 4; k++) {
								if (comArr[y][x++] == nSelNum) {
									cnt++;
									if (cnt == 4) {
										weightFixed = true;
										if (j == 0) {
											weight[y][x] += pattern4;
										} else if (j == 15) {
											weight[i][j - 1] += pattern4;
										} else {
											weight[y][x] += pattern4;
											weight[i][j - 1] += pattern4;
										}
										cnt = 0;
									}
								} else {
									cnt = 0;
								}
							}
						}
						if (i < 16 && j < 16) { // ������ �Ʒ� �밢�� 4
							cnt = 0;
							y = i;
							x = j;
							for (int k = 0; k < 4; k++) {
								if (comArr[y++][x++] == nSelNum) {
									cnt++;
									if (cnt == 4) {
										weightFixed = true;
										if ((j == 0 && i != 15) || (i == 0 && j != 15)) { // ������ �Ʒ��� ����ġ
											weight[y][x] += pattern4;
										} else if ((j == 15 && i != 0) || (i == 15 && j != 0)) { // ���� ���� ����ġ
											weight[i - 1][j - 1] += pattern4;
										} else if (j != 15 && i != 15) {
											weight[y][x] += pattern4;
											weight[i - 1][j - 1] += pattern4;
										}
										cnt = 0;
									}
								} else {
									cnt = 0;
								}
							}
						}
						if (i > 2 && j < 16) { // ������ �� �밢�� 4
							cnt = 0;
							y = i;
							x = j;
							for (int k = 0; k < 4; k++) {
								if (comArr[y--][x++] == nSelNum) {
									cnt++;
									if (cnt == 4) {
										weightFixed = true;
										if ((j == 0 && i != 3) || (i == 18 && j != 15)) { // ������ ���� ����ġ
											weight[y][x] += pattern4;
										} else if ((j == 15 && i != 18) || (j != 0 && i == 3)) { // ���� �Ʒ��� ����ġ
											weight[i + 1][j - 1] += pattern4;
										} else if (j != 15 && i != 18 && j != 0) { // ���� ����ġ
											weight[y][x] += pattern4;
											weight[i + 1][j - 1] += pattern4;
										}
										cnt = 0;
									}
								} else {
									cnt = 0;
								}
							}
						}

						if (i > 0 && i < 16) { // ���� ����3
							cnt = 0;
							y = i;
							x = j;
							for (int k = 0; k < 3; k++) {
								if (comArr[y++][x] == nSelNum) {
									cnt++;
									if (cnt == 3) {
										weightFixed = true;
										if (i == 1 && comArr[y][x] != selNum && comArr[i - 1][j] != selNum) {
											weight[y][x] += pattern3;
										} else if (i == 15 && comArr[y][x] != selNum && comArr[i - 1][j] != selNum) {
											weight[i - 1][j] += pattern3;
										} else if (comArr[i - 1][j] != selNum && comArr[y][x] != selNum) {
											weight[y][x] += pattern3;
											weight[i - 1][j] += pattern3;
										}
										cnt = 0;
									}
								} else {
									cnt = 0;
								}
							}
						}
						if (j > 0 && j < 16) { // ���� ���� 3
							cnt = 0;
							y = i;
							x = j;
							for (int k = 0; k < 3; k++) {
								if (comArr[y][x++] == nSelNum) {
									cnt++;
									if (cnt == 3) {
										weightFixed = true;
										if (j == 1 && comArr[y][x] != selNum && comArr[i][j - 1] != selNum) {
											weight[y][x] += pattern3;
										} else if (j == 15 && comArr[y][x] != selNum && comArr[i][j - 1] != selNum) {
											weight[i][j - 1] += pattern3;
										} else if (comArr[i][j - 1] != selNum && comArr[y][x] != selNum) {
											weight[y][x] += pattern3;
											weight[i][j - 1] += pattern3;
										}
										cnt = 0;
									}
								} else {
									cnt = 0;
								}
							}
						}
						if (i > 0 && i < 16 && j < 16 && j > 0) { // ������ �Ʒ� �밢�� ���� 3
							cnt = 0;
							y = i;
							x = j;
							for (int k = 0; k < 3; k++) {
								if (comArr[y++][x++] == nSelNum) {
									cnt++;
									if (cnt == 3) {
										weightFixed = true;
										if (i != 15 && j == 1 && comArr[i - 1][j - 1] != selNum
												&& comArr[y][x] != selNum) {
											weight[y][x] += pattern3;
										} else if (i != 1 && j == 15 && comArr[i - 1][j - 1] != selNum
												&& comArr[y][x] != selNum) {
											weight[i - 1][j - 1] += pattern3;
										} else if (i == 1 && j != 15 && comArr[i - 1][j - 1] != selNum
												&& comArr[y][x] != selNum) {
											weight[y][x] += pattern3;
										} else if (i == 15 && j != 1 && comArr[i - 1][j - 1] != selNum
												&& comArr[y][x] != selNum) {
											weight[i - 1][j - 1] += pattern3;
										} else if (comArr[i - 1][j - 1] != selNum && comArr[y][x] != selNum) {
											weight[y][x] += pattern3;
											weight[i - 1][j - 1] += pattern3;
										}
										cnt = 0;
									}
								} else {
									cnt = 0;
								}
							}
						}
						if (i > 2 && i < 18 && j < 16 && j > 0) { // ������ �� �밢�� ���� 3
							cnt = 0;
							y = i;
							x = j;
							for (int k = 0; k < 3; k++) {
								if (comArr[y--][x++] == nSelNum) {
									cnt++;
									if (cnt == 3) {
										weightFixed = true;
										if (i != 17 && j == 15 && comArr[i + 1][j - 1] != selNum
												&& comArr[y][x] != selNum) {
											weight[i + 1][j - 1] += pattern3;
										} else if (i == 17 && j != 15 && comArr[i + 1][j - 1] != selNum
												&& comArr[y][x] != selNum) {
											weight[y][x] += pattern3;
										} else if (i == 3 && j != 1 && comArr[i + 1][j - 1] != selNum
												&& comArr[y][x] != selNum) {
											weight[i + 1][j - 1] += pattern3;
										} else if (i != 3 && j == 1 && comArr[i + 1][j - 1] != selNum
												&& comArr[y][x] != selNum) {
											weight[y][x] += pattern3;
										} else if (comArr[i + 1][j - 1] != selNum && comArr[y][x] != selNum) {
											weight[y][x] += pattern3;
											weight[i + 1][j - 1] += pattern3;
										}
										cnt = 0;
									}
								} else {
									cnt = 0;
								}
							}
						}
						if (i < 15) { // ���� 2 1 ����
							cnt = cCnt = 0;
							y = i;
							x = j;
							for (int k = 0; k < 4; k++) {
								if (comArr[y][x] == nSelNum) { // ��� �� ����
									cnt++;
								} else if (k != 3 && comArr[y][x] == 0) { // ��ĭ ����
									cCnt++;
									l = y;
									h = x;
								}
								y++;
								if (cnt == 3 && cCnt == 1) {
									weightFixed = true;
									if (i != 0 && comArr[i - 1][j] != selNum && comArr[y][x] != selNum) {
										weight[l][h] += pattern3 + 20;
									}
								}
							}
							cnt = cCnt = 0;
						}
						if (j < 15) { // ���� 2 1 ����
							y = i;
							x = j;
							for (int k = 0; k < 4; k++) {
								if (comArr[y][x] == nSelNum) { // ��� �� ����
									cnt++;
								} else if (k != 3 && comArr[y][x] == 0) { // ��ĭ ����
									cCnt++;
									l = y;
									h = x;
								}
								x++;
								if (cnt == 3 && cCnt == 1) {
									weightFixed = true;
									if (j != 0 && comArr[i][j - 1] != selNum && comArr[y][x] != selNum) {
										weight[l][h] += pattern3 + 20;
									}
								}
							}
							cnt = cCnt = 0;
						}
						if (i < 15 && j < 15) { // ���� 2 1 ������ �Ʒ� �밢��
							y = i;
							x = j;
							for (int k = 0; k < 4; k++) {
								if (comArr[y][x] == nSelNum) { // ��� �� ����
									cnt++;
								} else if (k != 3 && comArr[y][x] == 0) { // ��ĭ ����
									cCnt++;
									l = y;
									h = x;
								}
								x++;
								y++;
								if (cnt == 3 && cCnt == 1) {
									weightFixed = true;
									if (j != 0 && comArr[i - 1][j + 1] != selNum && comArr[y][x] != selNum) {
										weight[l][h] += pattern3 + 20;
									}
								}
							}
							cnt = cCnt = 0;
						}
						if (i > 3 && j < 15) { // ���� 2 1 ������ �� �밢��
							y = i;
							x = j;
							for (int k = 0; k < 4; k++) {
								if (comArr[y][x] == nSelNum) { // ��� �� ����
									cnt++;
								} else if (k != 3 && comArr[y][x] == 0) { // ��ĭ ����
									cCnt++;
									l = y;
									h = x;
								}
								x++;
								y--;
								if (cnt == 3 && cCnt == 1) {
									weightFixed = true;
									System.out.println("������ �밢��");
									if (i != 18 && comArr[i + 1][j - 1] != selNum && comArr[y][x] != selNum) {
										weight[l][h] += pattern3 + 20;
									}
								}
							}
							cnt = cCnt = 0;
						}
					}
					if (i < 15 && i > 2) { // ���� 2
						cnt = 0;
						y = i;
						x = j;
						for (int k = 0; k < 2; k++) {
							if (comArr[y++][x] == nSelNum) {
								cnt++;
								if (cnt == 2 && comArr[y][x] != selNum && comArr[y + 1][x] != selNum
										&& comArr[i - 1][j] != selNum && comArr[i - 2][j] != selNum) {
									// ���Ʒ� 2ĭ
									weight[y][x] += pattern2;
									weight[y + 1][x] += pattern2_1;
									weight[i - 1][j] += pattern2;
									weight[i - 2][j] += pattern2_1;
									cnt = 0;
								}
							} else {
								cnt = 0;
							}
						}
					}
					if (j < 15 && j > 2) { // ���� 2
						cnt = 0;
						y = i;
						x = j;
						for (int k = 0; k < 2; k++) {
							if (comArr[y][x++] == nSelNum) {
								cnt++;
								if (cnt == 2 && comArr[y][x] != selNum && comArr[y][x + 1] != selNum
										&& comArr[i][j - 1] != selNum && comArr[i][j - 2] != selNum) {
									// �翷 2ĭ
									weight[y][x] += pattern2;
									weight[y][x + 1] += pattern2_1;
									weight[i][j - 1] += pattern2;
									weight[i][j - 2] += pattern2_1;
									cnt = 0;
								}
							} else {
								cnt = 0;
							}
						}
					}
					if (i > 2 && i < 15 && j < 15 && j > 2) { // ������ �Ʒ� �밢�� 2
						cnt = 0;
						y = i;
						x = j;
						for (int k = 0; k < 2; k++) {
							if (comArr[y++][x++] == nSelNum) {
								cnt++;
								if (cnt == 2 && comArr[y][x] != selNum && comArr[y + 1][x + 1] != selNum
										&& comArr[i - 1][j - 1] != selNum && comArr[i - 2][j - 2] != selNum) {
									// ���� �Ʒ� 2ĭ, ���� �� 2ĭ
									weight[y][x] += pattern2;
									weight[y + 1][x + 1] += pattern2_1;
									weight[i - 1][j - 1] += pattern2;
									weight[i - 2][j - 2] += pattern2_1;
									cnt = 0;
								}
							} else {
								cnt = 0;
							}
						}
					}
					if (i < 16 && i > 3 && j < 15 && j > 2) { // ������ �� �밢�� 2
						cnt = 0;
						y = i;
						x = j;
						for (int k = 0; k < 2; k++) {
							if (comArr[y--][x++] == nSelNum) {
								cnt++;
								if (cnt == 2 && comArr[y][x] != selNum && comArr[y - 1][x + 1] != selNum
										&& comArr[i + 1][j - 1] != selNum && comArr[i + 2][j - 2] != selNum) {
									// ���� �� �밢 2ĭ, �� �Ʒ� 2ĭ
									weight[y][x] += pattern2;
									weight[y - 1][x + 1] += pattern2_1;
									weight[i + 1][j - 1] += pattern2;
									weight[i + 2][j - 2] += pattern2_1;
									cnt = 0;
								}
							} else {
								cnt = 0;
							}
						}
					}
					if (comArr[i][j] == selNum) { // ��ǻ�� �� �϶�
						if (i == 0) {
							if (j == 0 || j == 18) {
								weight[i + 1][j]++;
							} else {
								weight[i + 1][j - 1]++;
								weight[i + 1][j]++;
								weight[i + 1][j + 1]++;
							}
						} else if (i == 18) {
							if (j == 0 || j == 18) {
								if (j == 0 || j == 18) {
									weight[i - 1][j]++;
								} else {
									weight[i - 1][j - 1]++;
									weight[i - 1][j]++;
									weight[i - 1][j + 1]++;
								}
							}
						} else if (j == 0) {
							if (i == 0 || i == 18) {
								weight[i][j + 1]++;
							} else {
								weight[i - 1][j + 1]++;
								weight[i][j + 1]++;
								weight[i + 1][j + 1]++;
							}
						} else if (j == 18) {
							if (i == 0 || i == 18) {
								weight[i][j - 1]++;
							} else {
								weight[i - 1][j - 1]++;
								weight[i][j - 1]++;
								weight[i + 1][j - 1]++;
							}
						} else {
							weight[i - 1][j - 1]++; // ����
							weight[i - 1][j]++; // ��
							weight[i - 1][j + 1]++; // ����
							weight[i][j + 1]++; // ��
							weight[i + 1][j + 1]++; // ����
							weight[i + 1][j]++; // ��
							weight[i + 1][j - 1]++; // �޾�
							weight[i][j - 1]++; // ��
						}
						if (i < 16) { // ���� 4
							cnt = 0;
							y = i;
							x = j;
							for (int k = 0; k < 4; k++) {
								if (comArr[y++][x] == selNum) {
									cnt++;
									if (cnt == 4) {
										weightFixed = true;
										if (i == 0) {
											weight[y][x] += cPattern4;
										} else if (i == 15) {
											weight[i - 1][j] += cPattern4;
										} else {
											weight[y][x] += cPattern4;
											weight[i - 1][j] += cPattern4;
										}
										cnt = 0;
									}
								} else {
									cnt = 0;
								}
							}
						}
						if (j < 16) { // ���� 4
							cnt = 0;
							y = i;
							x = j;
							for (int k = 0; k < 4; k++) {
								if (comArr[y][x++] == selNum) {
									cnt++;
									if (cnt == 4) {
										weightFixed = true;
										System.out.println("����4");
										if (j == 0) {
											weight[y][x] += cPattern4;
										} else if (j == 15) {
											weight[i][j - 1] += cPattern4;
										} else {
											weight[y][x] += cPattern4;
											weight[i][j - 1] += cPattern4;
										}
										cnt = 0;
									}
								} else {
									cnt = 0;
								}
							}
						}
						if (i < 16 && j < 16) { // ������ �Ʒ� �밢�� 4
							cnt = 0;
							y = i;
							x = j;
							for (int k = 0; k < 4; k++) {
								if (comArr[y++][x++] == selNum) {
									cnt++;
									if (cnt == 4) {
										weightFixed = true;
										if ((j == 0 && i != 15) || (i == 0 && j != 15)) { // ������ �Ʒ��� ����ġ
											weight[y][x] += cPattern4;
										} else if ((j == 15 && i != 0) || (i == 15 && j != 0)) { // ���� ���� ����ġ
											weight[i - 1][j - 1] += cPattern4;
										} else if (j != 15 && i != 15) {
											weight[y][x] += cPattern4;
											weight[i - 1][j - 1] += cPattern4;
										}
										cnt = 0;
									}
								} else {
									cnt = 0;
								}
							}
						}
						if (i > 2 && j < 16) { // ������ �� �밢�� 4
							cnt = 0;
							y = i;
							x = j;
							for (int k = 0; k < 4; k++) {
								if (comArr[y--][x++] == selNum) {
									cnt++;
									if (cnt == 4) {
										weightFixed = true;
										if ((j == 0 && i != 3) || (i == 18 && j != 15)) { // ������ ���� ����ġ
											weight[y][x] += cPattern4;
										} else if ((j == 15 && i != 18) || (j != 0 && i == 3)) { // ���� �Ʒ��� ����ġ
											weight[i + 1][j - 1] += cPattern4;
										} else if (j != 15 && i != 18 && j != 0) { // ���� ����ġ
											weight[y][x] += cPattern4;
											weight[i + 1][j - 1] += cPattern4;
										}
										cnt = 0;
									}
								} else {
									cnt = 0;
								}
							}
						}
						if (i < 15) { // ���� 2 1 ����
							cnt = cCnt = 0;
							y = i;
							x = j;
							for (int k = 0; k < 4; k++) {
								if (comArr[y][x] == selNum) { // ��� �� ����
									cnt++;
								} else if (k != 3 && comArr[y][x] == 0) { // ��ĭ ����
									cCnt++;
									l = y;
									h = x;
								}
								y++;
								if (cnt == 3 && cCnt == 1) {
									weightFixed = true;
									if (i != 0 && comArr[i - 1][j] != nSelNum && comArr[y][x] != nSelNum) {
										weight[l][h] += pattern3 + 20;
									}
								}
							}
							cnt = cCnt = 0;
						}
						if (j < 15) { // ���� 2 1 ����
							y = i;
							x = j;
							for (int k = 0; k < 4; k++) {
								if (comArr[y][x] == selNum) { // ��� �� ����
									cnt++;
								} else if (k != 3 && comArr[y][x] == 0) { // ��ĭ ����
									cCnt++;
									l = y;
									h = x;
								}
								x++;
								if (cnt == 3 && cCnt == 1) {
									weightFixed = true;
									if (j != 0 && comArr[i][j - 1] != nSelNum && comArr[y][x] != nSelNum) {
										weight[l][h] += pattern3 + 20;
									}
								}
							}
							cnt = cCnt = 0;
						}
						if (i < 15 && j < 15) { // ���� 2 1 ������ �Ʒ� �밢��
							y = i;
							x = j;
							for (int k = 0; k < 4; k++) {
								if (comArr[y][x] == selNum) { // ��� �� ����
									cnt++;
								} else if (k != 3 && comArr[y][x] == 0) { // ��ĭ ����
									cCnt++;
									l = y;
									h = x;
								}
								x++;
								y++;
								if (cnt == 3 && cCnt == 1) {
									weightFixed = true;
									if (j != 0 && comArr[i - 1][j + 1] != nSelNum && comArr[y][x] != nSelNum) {
										weight[l][h] += pattern3 + 20;
									}
								}
							}
							cnt = cCnt = 0;
						}
						if (i > 3 && j < 15) { // ���� 2 1 ������ �� �밢��
							y = i;
							x = j;
							for (int k = 0; k < 4; k++) {
								if (comArr[y][x] == selNum) { // ��� �� ����
									cnt++;
								} else if (k != 3 && comArr[y][x] == 0) { // ��ĭ ����
									cCnt++;
									l = y;
									h = x;
								}
								x++;
								y--;
								if (cnt == 3 && cCnt == 1) {
									weightFixed = true;
									if (i != 18 && comArr[i + 1][j - 1] != nSelNum && comArr[y][x] != nSelNum) {
										weight[l][h] += pattern3 + 20;
									}
								}
							}
							cnt = cCnt = 0;
						}
						if (i > 0 && i < 16) { // ���� ����3
							cnt = 0;
							y = i;
							x = j;
							for (int k = 0; k < 3; k++) {
								if (comArr[y++][x] == selNum) {
									cnt++;
									if (cnt == 3) {
										weightFixed = true;
										if (i == 1 && comArr[y][x] != nSelNum && comArr[i - 1][j] != nSelNum) {
											weight[y][x] += pattern3 + 10;
										} else if (i == 15 && comArr[y][x] != nSelNum && comArr[i - 1][j] != nSelNum) {
											weight[i - 1][j] += pattern3 + 10;
										} else if (comArr[i - 1][j] != nSelNum && comArr[y][x] != nSelNum) {
											weight[y][x] += pattern3 + 10;
											weight[i - 1][j] += pattern3 + 10;
										}
										cnt = 0;
									}
								} else {
									cnt = 0;
								}
							}
						}
						if (j > 0 && j < 16) { // ���� ���� 3
							cnt = 0;
							y = i;
							x = j;
							for (int k = 0; k < 3; k++) {
								if (comArr[y][x++] == selNum) {
									cnt++;
									if (cnt == 3) {
										weightFixed = true;
										if (j == 1 && comArr[y][x] != nSelNum && comArr[i][j - 1] != nSelNum) {
											weight[y][x] += pattern3 + 10;
										} else if (j == 15 && comArr[y][x] != nSelNum && comArr[i][j - 1] != nSelNum) {
											weight[i][j - 1] += pattern3 + 10;
										} else if (comArr[i][j - 1] != nSelNum && comArr[y][x] != nSelNum) {
											weight[y][x] += pattern3 + 10;
											weight[i][j - 1] += pattern3 + 10;
										}
										cnt = 0;
									}
								} else {
									cnt = 0;
								}
							}
						}
						if (i > 0 && i < 16 && j < 16 && j > 0) { // ������ �Ʒ� �밢�� ���� 3
							cnt = 0;
							y = i;
							x = j;
							for (int k = 0; k < 3; k++) {
								if (comArr[y++][x++] == selNum) {
									cnt++;
									if (cnt == 3) {
										weightFixed = true;
										if (i != 15 && j == 1 && comArr[i - 1][j - 1] != nSelNum
												&& comArr[y][x] != nSelNum) {
											weight[y][x] += pattern3 + 10;
										} else if (i != 1 && j == 15 && comArr[i - 1][j - 1] != nSelNum
												&& comArr[y][x] != nSelNum) {
											weight[i - 1][j - 1] += pattern3 + 10;
										} else if (i == 1 && j != 15 && comArr[i - 1][j - 1] != nSelNum
												&& comArr[y][x] != nSelNum) {
											weight[y][x] += pattern3 + 10;
										} else if (i == 15 && j != 1 && comArr[i - 1][j - 1] != nSelNum
												&& comArr[y][x] != nSelNum) {
											weight[i - 1][j - 1] += pattern3 + 10;
										} else if (comArr[i - 1][j - 1] != nSelNum && comArr[y][x] != nSelNum) {
											weight[y][x] += pattern3 + 10;
											weight[i - 1][j - 1] += pattern3 + 10;
										}
										cnt = 0;
									}
								} else {
									cnt = 0;
								}
							}
						}
						if (i > 2 && j < 16) { // ������ �� �밢�� ���� 3
							cnt = 0;
							y = i;
							x = j;
							for (int k = 0; k < 3; k++) {
								if (comArr[y--][x++] == selNum) {
									cnt++;
									if (cnt == 3) {
										weightFixed = true;
										if (i != 17 && j == 15 && comArr[i + 1][j - 1] != nSelNum
												&& comArr[y][x] != nSelNum) {
											weight[i + 1][j - 1] += pattern3 + 10;
										} else if (i == 17 && j != 15 && comArr[i + 1][j - 1] != nSelNum
												&& comArr[y][x] != nSelNum) {
											weight[y][x] += pattern3 + 10;
										} else if (i == 3 && j != 1 && comArr[i + 1][j - 1] != nSelNum
												&& comArr[y][x] != nSelNum) {
											weight[i + 1][j - 1] += pattern3 + 10;
										} else if (i != 3 && j == 1 && comArr[i + 1][j - 1] != nSelNum
												&& comArr[y][x] != nSelNum) {
											weight[y][x] += pattern3 + 10;
										} else if (comArr[i + 1][j - 1] != nSelNum && comArr[y][x] != nSelNum) {
											weight[y][x] += pattern3 + 10;
											weight[i + 1][j - 1] += pattern3 + 10;
										}
										cnt = 0;
									}
								} else {
									cnt = 0;
								}
							}
						}
						if (i < 15 && i > 2) { // ���� 2
							cnt = 0;
							y = i;
							x = j;
							for (int k = 0; k < 2; k++) {
								if (comArr[y++][x] == selNum) {
									cnt++;
									if (cnt == 2 && comArr[y][x] != nSelNum && comArr[y + 1][x] != nSelNum
											&& comArr[i - 1][j] != nSelNum && comArr[i - 2][j] != nSelNum) {
										// ���Ʒ� 2ĭ
										weight[y][x] += pattern2;
										weight[y + 1][x] += pattern2_1;
										weight[i - 1][j] += pattern2;
										weight[i - 2][j] += pattern2_1;
										cnt = 0;
									}
								} else {
									cnt = 0;
								}
							}
						}
						if (j < 15 && j > 2) { // ���� 2
							cnt = 0;
							y = i;
							x = j;
							for (int k = 0; k < 2; k++) {
								if (comArr[y][x++] == selNum) {
									cnt++;
									if (cnt == 2 && comArr[y][x] != nSelNum && comArr[y][x + 1] != nSelNum
											&& comArr[i][j - 1] != nSelNum && comArr[i][j - 2] != nSelNum) {
										// �翷 2ĭ
										weight[y][x] += pattern2;
										weight[y][x + 1] += pattern2_1;
										weight[i][j - 1] += pattern2;
										weight[i][j - 2] += pattern2_1;
										cnt = 0;
									}
								} else {
									cnt = 0;
								}
							}
						}
						if (i > 2 && i < 15 && j < 15 && j > 2) { // ������ �Ʒ� �밢�� 2
							cnt = 0;
							y = i;
							x = j;
							for (int k = 0; k < 2; k++) {
								if (comArr[y++][x++] == selNum) {
									cnt++;
									if (cnt == 2 && comArr[y][x] != nSelNum && comArr[y + 1][x + 1] != nSelNum
											&& comArr[i - 1][j - 1] != nSelNum && comArr[i - 2][j - 2] != nSelNum) {
										// ���� �Ʒ� 2ĭ, ���� �� 2ĭ
										weight[y][x] += pattern2;
										weight[y + 1][x + 1] += pattern2_1;
										weight[i - 1][j - 1] += pattern2;
										weight[i - 2][j - 2] += pattern2_1;
										cnt = 0;
									}
								} else {
									cnt = 0;
								}
							}
						}
						if (i < 16 && i > 3 && j < 15 && j > 2) { // ������ �� �밢�� 2
							cnt = 0;
							y = i;
							x = j;
							for (int k = 0; k < 2; k++) {
								if (comArr[y--][x++] == selNum) {
									cnt++;
									if (cnt == 2 && comArr[y][x] != nSelNum && comArr[y - 1][x + 1] != nSelNum
											&& comArr[i + 1][j - 1] != nSelNum && comArr[i + 2][j - 2] != nSelNum) {
										// ���� �� �밢 2ĭ, �� �Ʒ� 2ĭ
										weight[y][x] += pattern2;
										weight[y - 1][x + 1] += pattern2_1;
										weight[i + 1][j - 1] += pattern2;
										weight[i + 2][j - 2] += pattern2_1;
										cnt = 0;
									}
								} else {
									cnt = 0;
								}
							}
						}
					}
				}
			}
			// ����ġ ���� ��
			// ���� �ִ°� ����ġ �ʱ�ȭ
			for (int i = 0; i < 19; i++) {
				for (int j = 0; j < 19; j++) {
					if (comArr[i][j] == selNum) {
						weight[i][j] = selNum;
					} else if (comArr[i][j] == nSelNum) {
						weight[i][j] = nSelNum;
					}
				}
			}
			for (int i = 0; i < 19; i++) { // ����ġ �ʱ�ȭ
				for (int j = 0; j < 19; j++) {
					System.out.printf("%3d", weight[i][j]);
				}
				System.out.println();
			}
			System.out.println();
			int max = -1; // �ִ� : ����ġ�� ���� ū �� ����
			for (int i = 0; i < 19; i++) {
				for (int j = 0; j < 19; j++) {
					if (max <= weight[i][j] && weight[i][j] != selNum && weight[i][j] != nSelNum && weight[i][j] != 0) {
						max = weight[i][j];
						l = i;
						h = j;
						if (turnCnt == 1) { // ù ���� �� ���
							fL = l;
							fH = h;
						}
					}
				}
			}
			if (weightFixed) {
				if (turnCnt == 1) {
					l = fL;
					h = fH;
					turnCnt=0;
					break;
				}
			}
			// ������ ������ �� ������
			comArr[l][h] = selNum;
			System.out.println("ComArr");
			for (int i = 0; i < 19; i++) { // comArr
				for (int j = 0; j < 19; j++) {
					System.out.printf("%2d", comArr[i][j]);
				}
				System.out.println();
			}
			System.out.println("ComArr" + " pWin : " + pWin);
			if (winCheck(comArr) || stoneCnt < 3) { // ��ǻ�Ͱ� �̱�� ����
				l = fL;
				h = fH;
				break;
			} else if (!winCheck(comArr) && pWin) { // ����� �̱�� ó������ �ٸ��� ����
				firstChoice[fL][fH] = weightCheck--;
				for (int i = 0; i < 19; i++) {
					for (int j = 0; j < 19; j++) {
						comArr[i][j] = comRealArr[i][j];
					}
				}
				turnCnt = 0;
			} else {// �ƴϸ� ���� �� ������ ���� �� ������
				temp = selNum;
				selNum = nSelNum;
				nSelNum = temp;
				for (int i = 0; i < 19; i++) { // ����ġ �ʱ�ȭ
					for (int j = 0; j < 19; j++) {
						weight[i][j] = 0;
					}
				}
			}
		} // �� ���� ���� ����
		System.out.println("turnCnt : " + turnCnt);
		selNum = OmokClient.selNum;
		nSelNum = OmokClient.nSelNum;
		comRealArr[l][h] = selNum;
		OmokClient.omokArr[l][h] = nSelNum;
		OmokClient.stones[l][h].setIcon(new ImageIcon(OmokClient.nSelStone));
		OmokClient.stones[l][h].setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		if (winCheck(comRealArr)) {
			OmokClient.vsComWinCheck = 2;
			System.out.println("��ǻ�� �¸�!!\n������ �����մϴ�.");
		} else if (!winCheck(comRealArr) && pWin) {
			OmokClient.vsComWinCheck = 1;
			System.out.println("�÷��̾� �¸�!!\n������ �����մϴ�.");
		}
	}

	public boolean winCheck(int[][] arr) {
		System.out.println("�¸� üũ");
		int pCnt = 0, cCnt = 0, i, j, selNum = 9, nSelNum = 10;
		for (i = 0; i < 19; i++) {
			for (j = 0; j < 19; j++) {
				if (arr[i][j] == selNum) {
					if (i < 15) { // ���� üũ
						cCnt = 0;
						int l = i, h = j;
						for (int k = 0; k < 5; k++) {
							if (arr[l++][h] == selNum) {
								cCnt++;
							}
						}
					}
					if (cCnt == 5) {
						break;
					}
					if (j < 15) { // ���� üũ
						cCnt = 0;
						int l = i, h = j;
						for (int k = 0; k < 5; k++) {
							if (arr[l][h++] == selNum) {
								cCnt++;
							}
						}
					}
					if (cCnt == 5) {
						break;
					}
					if (i < 15 && j < 15) { // ������ �Ʒ� �밢�� üũ
						cCnt = 0;
						int l = i, h = j;
						for (int k = 0; k < 5; k++) {
							if (arr[l++][h++] == selNum) {
								cCnt++;
							}
						}
					}
					if (cCnt == 5) {
						break;
					}
					if (i > 3 && j <= 14) { // ������ �� �밢�� üũ
						cCnt = 0;
						int l = i, h = j;
						for (int k = 0; k < 5; k++) {
							if (arr[l--][h++] == selNum) {
								cCnt++;
							}
						}
					}
					if (cCnt == 5) {
						break;
					}
				}
				if (arr[i][j] == nSelNum) {
					if (i < 15) { // ���� üũ
						pCnt = 0;
						int l = i, h = j;
						for (int k = 0; k < 5; k++) {
							if (arr[l++][h] == nSelNum) {
								pCnt++;
							}
						}
					}
					if (pCnt == 5)
						break;
					if (j <= 14) { // ���� üũ
						pCnt = 0;
						int l = i, h = j;
						for (int k = 0; k < 5; k++) {
							if (arr[l][h++] == nSelNum) {
								pCnt++;
							}
						}
					}
					if (pCnt == 5)
						break;
					if (i < 15 && j < 15) { // ������ �Ʒ� �밢�� üũ
						pCnt = 0;
						int l = i, h = j;
						for (int k = 0; k < 5; k++) {
							if (arr[l++][h++] == nSelNum) {
								pCnt++;
							}
						}
					}
					if (pCnt == 5)
						break;
					if (i > 3 && j < 15) { // ������ �� �밢�� üũ
						pCnt = 0;
						int l = i, h = j;
						for (int k = 0; k < 5; k++) {
							if (arr[l--][h++] == nSelNum) {
								pCnt++;
							}
						}
					}
					if (pCnt == 5)
						break;
				}
			}
			if (cCnt == 5) {
				return true;
			}
			if (pCnt == 5) {
				pWin = true;
				return false;
			}
			cCnt = 0;
			pCnt = 0;
		}
		return false;
	}
}
