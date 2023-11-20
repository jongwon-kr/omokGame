package p2p;

import java.util.regex.Pattern;

public class test {

	public static void main(String[] args) {
		String[] exceptWords = { "^[가-힣]+니다$", "^[가-힣]+는$", "^[가-힣]+요$", "[$서]", "[$고]", "[$는]", "[$로]", "[$의]", "[$을]", "[$와]",
				"[다*$]", "[$때문]", "[이]$", "큰", "진짜", "이유", "\"", "'", "  ", "   " };
		String result = " \"안녕하세요\" '저는' 가장...김종원입니다 반갑습니다 하이요...";
		result = result.replaceAll("[.]", " ");
		result = result.replaceAll("\"", "");
		result = result.replaceAll("'", "");
		String[] resultWords;
		resultWords = result.split(" ");
		Pattern p;
		result = "";
		for (int j = 0; j < exceptWords.length; j++) {
			result = result.replaceAll(exceptWords[j], "");
		}
//		for (int i = 0; i < resultWords.length; i++) {
//			for (int j = 0; j < exceptWords.length; j++) {
//				if (resultWords[i].matches(exceptWords[j]))
//					resultWords[i] = "";
//			}
//			if (!resultWords[i].equals(""))
//				result += resultWords[i] + " ";
//		}
		System.out.println(result);
	}
}
