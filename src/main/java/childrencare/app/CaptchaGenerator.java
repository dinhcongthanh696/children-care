package childrencare.app;

public class CaptchaGenerator {
	private static final int CAPTCHASIZE = 7;
	
	
	public static String generateCaptchaCode() {
		String captcha = "";
		String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		
		for(int i = 0 ; i < CAPTCHASIZE ; i++) {
			captcha += characters.charAt( (int)  (Math.random() * characters.length())  );
		}
		
		return captcha;
	}
}
