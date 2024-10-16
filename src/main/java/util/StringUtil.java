package util;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class StringUtil {
    public static boolean isNonAccent(String str) {
        return str != null && str.equals(removeAccent(str));
    }

    // Loại bỏ dấu trong chuỗi
    public static String removeAccent(String str) {
        if (str == null) {
            return null;
        }
        // Sử dụng Normalizer để loại bỏ dấu
        String normalizedStr = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        // Loại bỏ ký tự không phải ASCII
        return pattern.matcher(normalizedStr).replaceAll("").replaceAll("[^\\p{ASCII}]", ""); 
    }
}
